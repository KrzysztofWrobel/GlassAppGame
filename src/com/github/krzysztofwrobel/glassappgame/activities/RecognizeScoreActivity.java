package com.github.krzysztofwrobel.glassappgame.activities;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.itraff.TestApi.ItraffApi.ItraffApi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.NetworkService;
import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.models.Challenge;
import com.github.krzysztofwrobel.glassappgame.models.Reward;
import com.squareup.picasso.Picasso;

public class RecognizeScoreActivity extends BaseActivity implements GestureDetector.OnGestureListener
{
	private final static String TAG = "RecognizeScoreActivity";
	private static final int RESULT_BMP_DAMAGED = 128;

	private final static String CLIENT_API_KEY = "4166e20337";
	private final static Integer CLIENT_API_ID = 41933;
	
	private GestureDetector gestureDetector;

	private TextView textView;
	
	private LocalBroadcastManager mLocalBroadcastManager;
	private BroadcastReceiver mLocalReceiver;
    private TextView scoreTitleTextView;
    private TextView rewardDescriptionTextView;
    private ImageView rewardImageView;
	
	private String recognizedKey;
    
    private int failCounter;
    private Reward completedChallangeReward = null;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.recognize_score_activity);

        gestureDetector = new GestureDetector(this, this);

        scoreTitleTextView = (TextView) findViewById(R.id.tv_score_title);
        rewardDescriptionTextView = (TextView) findViewById(R.id.tv_reward_description);
        rewardImageView = (ImageView) findViewById(R.id.iv_reward_icon);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		mLocalReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (NetworkService.ACTION_CHALLENGE_COMPLETE.equals(intent.getAction())) {
                    boolean error = intent.getBooleanExtra("error", false);
                    if (error) {
                    	if(++failCounter < 3)
                    	{
                    		try
							{
								Thread.sleep(100);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
                    		Bundle params = new Bundle();
                    		params.putString("id", recognizedKey);
                    		NetworkService.run(RecognizeScoreActivity.this, NetworkService.ACTION_CHALLENGE_COMPLETE, params);
                    	} else
                    	{
                            showInfoDialog(0, getString(R.string.error_fetching_reward));
                    	}
                        return;
                    } else {

                        Reward reward = intent.getParcelableExtra("reward");
                        onRewardReceived(reward);
//                        showInfoDialog(0, reward.getDescription());
                    }
                }
            }
        };
        Challenge completedChallange = getIntent().getParcelableExtra("challenge");
        if(completedChallange!=null && completedChallange.getReward()!=null){
            completedChallangeReward = completedChallange.getReward();
            onRewardReceived(completedChallangeReward);
        } else {
		    makePhoto();
        }
	}

    private void onRewardReceived(Reward receivedReward) {
        if(receivedReward!=null){
        	List<Challenge> challenges = getChallenges();
        	for(Challenge c : challenges)
        	{
        		if(c.getId().equals(recognizedKey))
        			c.setFinished(true);
        	}
            scoreTitleTextView.setText("You've got a Reward!");
            rewardDescriptionTextView.setText(receivedReward.getDescription());
            Picasso.with(this).load(receivedReward.getImage_link()).resize(640, 320).into(rewardImageView);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(NetworkService.ACTION_CHALLENGE_COMPLETE);
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);
    }

	public void makePhoto()
	{
		// Intent to take a photo
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, true);
		takePictureIntent.putExtra(MediaStore.EXTRA_SHOW_ACTION_ICONS, false);
		startActivityForResult(takePictureIntent, 1234);
	}

	// handler that receives response from api
	@SuppressLint("HandlerLeak")
	private Handler itraffApiHandler = new Handler()
	{
		// callback from api
		@Override
		public void handleMessage(Message msg)
		{
			dismissInfoDialog();
			Bundle data = msg.getData();
			if (data != null)
			{
				Integer status = data.getInt(ItraffApi.STATUS, -1);
				String response = data.getString(ItraffApi.RESPONSE);
				// status ok
				if (status == 0)
				{
					String key = retrieveKey(response);
					onRecognized(key);
				} else if (status == -1)
				{
					// application error (for example timeout)
					showInfoDialog(0, getString(R.string.timeout));
				} else if (status == 2)
				{
					// application error (for example timeout)
					showInfoDialog(0, getString(R.string.try_again));
					new Handler().postDelayed(new Runnable()
					{
						
						@Override
						public void run()
						{
							finish();
						}
					}, 1000);
				} else
				{
					// error from api
					showInfoDialog(0, getString(R.string.itraff_error, response));
				}
			}
		}
	};

	private String retrieveKey(String message)
	{
		String key = null;
		HashMap<String, Integer> matchedKeys = new HashMap<String, Integer>();
		try
		{
			JSONObject o = new JSONObject(message);
			JSONArray a = o.getJSONArray("id");
			for (int i = 0; i < a.length(); ++i)
			{
				key = a.getString(i).split("_")[0];
				Integer value = matchedKeys.get(key);
				if (value == null)
					value = 0;
				matchedKeys.put(key, value + 1);
			}
			int max = 0;
			int temp;
			for (String s : matchedKeys.keySet())
			{
				temp = matchedKeys.get(s);
				if (temp > max)
				{
					max = temp;
					key = s;
				}
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return key;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		log("onActivityResult reqcode, resultcode: " + requestCode + "  " + resultCode);
		if (resultCode == Activity.RESULT_OK)
		{
			log("Activity.RESULT_OK");
			if (data != null)
			{
				log("data != null");
				Bundle bundle = data.getExtras();
				if (bundle != null)
				{
					log("bundle != null");

					// byte[] pictureData = bundle.getByteArray("pictureData");
					Bitmap image = (Bitmap) bundle.get("data");
					if (image != null)
					{
						log("image != null");

						// chceck internet connection
						if (ItraffApi.isOnline(getApplicationContext()))
						{
							showInfoDialog(0, R.string.upload);
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
							// send photo
							ItraffApi api = new ItraffApi(CLIENT_API_ID, CLIENT_API_KEY, TAG, true);
							Log.v("KEY", CLIENT_API_ID.toString());
							if (prefs.getString("mode", "single").equals("multi"))
							{
								api.setMode(ItraffApi.MODE_MULTI);
							} else
							{
								api.setMode(ItraffApi.MODE_SINGLE);
							}

							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
							byte[] pictureData = stream.toByteArray();
							api.sendPhoto(pictureData, itraffApiHandler, prefs.getBoolean("allResults", true));
						} else
						{
							// show message: no internet connection
							// available.

							showInfoDialog(0, getString(R.string.not_connected));
						}
					}
				}
			}
		} else if (resultCode == RESULT_BMP_DAMAGED)
		{
			log("RESULT_BMP_DAMAGEDl");
		}
	}
	
	private void onRecognized(String key)
	{
		recognizedKey = key;
		Bundle params = new Bundle();
		params.putString("id", key);
		NetworkService.run(this, NetworkService.ACTION_CHALLENGE_COMPLETE, params);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		Intent scoreOptions = new Intent(this, ScoreOptionsActivity.class);
        scoreOptions.putExtra("reward",completedChallangeReward);
		startActivity(scoreOptions);
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return false;
	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //We need this to ensure it works on normal android devices
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

}
