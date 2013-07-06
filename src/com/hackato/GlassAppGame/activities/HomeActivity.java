package com.hackato.GlassAppGame.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hackato.GlassAppGame.NetworkService;
import com.hackato.GlassAppGame.R;
import com.hackato.GlassAppGame.activities.BaseActivity;
import com.hackato.GlassAppGame.fragments.HomeSlideFragment;
import com.hackato.GlassAppGame.models.Challenge;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements GestureDetector.OnGestureListener {

	private static final String TAG = "GlassAppGame";
	private ViewPager mSlideViewPager;
    private ScreenSlidePagerAdapter mSlidePagerAdapter;
    private GestureDetector gestureDetector;
    private FragmentManager mSupportFragmentManager;
	private LocalBroadcastManager mLocalBroadcastManager;
	private BroadcastReceiver mLocalReceiver;
	private ArrayList<Challenge> mChallenges;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true);
        setContentView(R.layout.home_activity);

        mSupportFragmentManager = getSupportFragmentManager();

        gestureDetector = new GestureDetector(this, this);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mChallenges = new ArrayList<Challenge>();

        mSlideViewPager = (ViewPager) findViewById(R.id.vp_home_slides);
        mSlidePagerAdapter = new ScreenSlidePagerAdapter(mSupportFragmentManager);
        mSlideViewPager.setAdapter(mSlidePagerAdapter);
        mSlideViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

		mLocalReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (NetworkService.ACTION_CHALLENGES.equals(intent.getAction())) {
					boolean error = intent.getBooleanExtra("error", false);
					if (error) {
						showInfoDialog(0, getString(R.string.error_fetching_challenges));
						return;
					}

					mChallenges = intent.getParcelableArrayListExtra("challenges");
					Log.d(TAG, "onReceive=" + mChallenges);
				}
			}
		};

		if (savedInstanceState == null) {
			NetworkService.run(this, NetworkService.ACTION_CHALLENGES, null);
		}
        
//        showInfoDialog(0, R.string.app_name);

    }
    
    @Override
    public void onLocationChanged(Location location)
    {
    	// TODO start connection
    	int lat = (int)(location.getLatitude() * 1E6);
    	int lng = (int)(location.getLongitude() * 1E6);
//    	takeDirection(location.getLatitude(), location.getLongitude());
    }

	@Override
	protected void onPause() {
		super.onPause();

		mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter(NetworkService.ACTION_CHALLENGES);
		mLocalBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> mSlideFragments;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            mSlideFragments = new ArrayList<Fragment>();
        }

        public void addSlideFragment(Fragment fragment){
            mSlideFragments.add(fragment);
        }

        public void removeSlideFragment(Fragment fragment){
            mSlideFragments.remove(fragment);
        }

        @Override
        public HomeSlideFragment getItem(int position) {
            return (HomeSlideFragment) mSlideFragments.get(position);
        }

        @Override
        public int getCount() {
            return mSlideFragments.size();
        }
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

    @Override
    public void onBackPressed() {
        Log.d("Gesture Example", "onBackPressed");
        Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture Example", "onDown");
        Challenge selectedChallenge = mSlidePagerAdapter.getItem(mSlideViewPager.getCurrentItem()).getChallenge();
        Intent newIntent = new Intent(HomeActivity.this,DescriptionActivity.class);
        newIntent.putExtra("challenge",selectedChallenge);
        startActivity(newIntent);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Gesture Example", "onFling: velocityX:" + velocityX + "   velocityY:" + velocityY);
        if (velocityX < -3500) {
            Toast.makeText(getApplicationContext(), "Fling Right", Toast.LENGTH_SHORT).show();
        } else if (velocityX > 3500) {
            Log.d("Gesture Example", "OnFlingLeft");
            Toast.makeText(getApplicationContext(), "Fling Left", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture Example", "onLongPress");
        Intent recognize = new Intent(this, RecognizeActivity.class);
        startActivity(recognize);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Gesture Example", "onScroll: distanceX:" + distanceX + "   distanceY:" + distanceY);
        if (distanceX < -1) {
            //Scroll Right or Scroll Up Command Here
        } else if (distanceX > 1) {
            Log.d("Gesture Example", "OnFlingLeft");
            //Scroll left or Scroll Down Command Here
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture Test", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Gesture Test", "onSingleTapUp");
        Toast.makeText(getApplicationContext(), "Single Tap Up", Toast.LENGTH_SHORT).show();
        return true;
    }

}
