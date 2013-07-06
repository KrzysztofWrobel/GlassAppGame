package com.hackato.GlassAppGame.activities;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.itraff.TestApi.ItraffApi.ItraffApi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hackato.GlassAppGame.R;
import com.hackato.GlassAppGame.R.id;
import com.hackato.GlassAppGame.R.layout;
import com.hackato.GlassAppGame.R.string;

public class RecognizeActivity extends BaseActivity
{
	private final static String TAG = "RecognizeActivity";
	private static final int RESULT_BMP_DAMAGED = 128;
	
	private final static String CLIENT_API_KEY = "4166e20337";
	private final static Integer CLIENT_API_ID = 41933;

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognize_layout);
		textView = (TextView) findViewById(R.id.text);
		makePhoto();
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
		private Handler itraffApiHandler = new Handler() {
			// callback from api
			@Override
			public void handleMessage(Message msg) {
				dismissInfoDialog();
				Bundle data = msg.getData();
				if (data != null) {
					Integer status = data.getInt(ItraffApi.STATUS, -1);
					String response = data.getString(ItraffApi.RESPONSE);
					// status ok
					if (status == 0) {
						String key = retrieveKey(response);
					} else if (status == -1) {
						// application error (for example timeout)
						textView.setText("-1" + response);
					} else {
						// error from api
						textView.setText("else" + response);
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
				for(int i = 0; i < a.length(); ++i)
				{
					key = a.getString(i).split("_")[0];
					Integer value = matchedKeys.get(key);
					if(value == null)
						value = 0;
					matchedKeys.put(key, value + 1);
				}
				int max = 0;
				int temp;
				for(String s : matchedKeys.keySet())
				{
					temp = matchedKeys.get(s);
					if(temp > max)
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
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			log("onActivityResult reqcode, resultcode: " + requestCode + "  "
					+ resultCode);
			if (resultCode == Activity.RESULT_OK) {
				log("Activity.RESULT_OK");
				if (data != null) {
					log("data != null");
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						log("bundle != null");

						// byte[] pictureData = bundle.getByteArray("pictureData");
						Bitmap image = (Bitmap) bundle.get("data");
						if (image != null) {
							log("image != null");

							// chceck internet connection
							if (ItraffApi.isOnline(getApplicationContext())) {
								showInfoDialog(0, R.string.upload);
								SharedPreferences prefs = PreferenceManager
										.getDefaultSharedPreferences(getBaseContext());
								// send photo
								ItraffApi api = new ItraffApi(CLIENT_API_ID,
										CLIENT_API_KEY, TAG, true);
								Log.v("KEY", CLIENT_API_ID.toString());
								if (prefs.getString("mode", "single").equals(
										"multi")) {
									api.setMode(ItraffApi.MODE_MULTI);
								} else {
									api.setMode(ItraffApi.MODE_SINGLE);
								}

								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								image.compress(Bitmap.CompressFormat.JPEG, 100,
										stream);
								byte[] pictureData = stream.toByteArray();
								api.sendPhoto(pictureData, itraffApiHandler,
										prefs.getBoolean("allResults", true));
							} else {
								// show message: no internet connection
								// available.

								Toast.makeText(
										getApplicationContext(),
										getResources().getString(
												R.string.not_connected),
										Toast.LENGTH_LONG).show();
							}
						}
					}
				}
			} else if (resultCode == RESULT_BMP_DAMAGED) {
				log("RESULT_BMP_DAMAGEDl");
			}
		}

}