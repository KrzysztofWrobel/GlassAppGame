package com.github.krzysztofwrobel.glassappgame.activities;

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
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.krzysztofwrobel.glassappgame.NetworkService;
import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.fragments.HomeSlideFragment;
import com.github.krzysztofwrobel.glassappgame.models.Challenge;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String TAG = "glassappgame";
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
        gestureDetector.setOnDoubleTapListener(this);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mChallenges = new ArrayList<Challenge>();

        mSlideViewPager = (ViewPager) findViewById(R.id.vp_home_slides);
        mSlidePagerAdapter = new ScreenSlidePagerAdapter(mSupportFragmentManager);
        mSlideViewPager.setAdapter(mSlidePagerAdapter);
        mSlideViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
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
                    } else {

                        mChallenges = intent.getParcelableArrayListExtra("challenges");
                        for (int i = 0; i < mChallenges.size(); i++) {
                            mSlidePagerAdapter.addSlideFragment(HomeSlideFragment.getInstance(mChallenges.get(i)));
                        }
                        mSlidePagerAdapter.notifyDataSetChanged();
                    }

                    dismissInfoDialog();
                    Log.d(TAG, "onReceive=" + mChallenges);
                }
            }
        };

        if (savedInstanceState == null) {
            NetworkService.run(this, NetworkService.ACTION_CHALLENGES, null);
            showInfoDialog(0, R.string.challanges_loading);
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO start connection
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
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

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
    	startDescriptionActivity();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Intent recognize = new Intent(this, RecognizeActivity.class);
        startActivity(recognize);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> mSlideFragments;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            mSlideFragments = new ArrayList<Fragment>();
        }

        public void addSlideFragment(Fragment fragment) {
            mSlideFragments.add(fragment);
        }

        public void removeSlideFragment(Fragment fragment) {
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

    @Override
    public void onBackPressed() {
        Log.d("Gesture Example", "onBackPressed");
        Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture Example", "onDown");
        return true;
    }

    private void startDescriptionActivity() {
        Challenge selectedChallenge = mSlidePagerAdapter.getItem(mSlideViewPager.getCurrentItem()).getChallenge();
        Intent newIntent = new Intent(HomeActivity.this, DescriptionActivity.class);
        newIntent.putExtra("challenge", selectedChallenge);
        startActivity(newIntent);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Gesture Example", "onFling: velocityX:" + velocityX + "   velocityY:" + velocityY);
        int currentPosition = mSlideViewPager.getCurrentItem();
        int nextPosition = Math.min(mSlidePagerAdapter.getCount(), currentPosition + 1);
        int previousePosition = Math.max(0, currentPosition - 1);
        if (velocityX < -3500) {
            mSlideViewPager.setCurrentItem(nextPosition);
        } else if (velocityX > 3500) {
            Log.d("Gesture Example", "OnFlingLeft");
            mSlideViewPager.setCurrentItem(previousePosition);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture Example", "onLongPress");

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Gesture Example", "onScroll: distanceX:" + distanceX + "   distanceY:" + distanceY);
        int currentPosition = mSlideViewPager.getCurrentItem();
        int nextPosition = Math.min(mSlidePagerAdapter.getCount(), currentPosition + 1);
        int previousePosition = Math.max(0, currentPosition - 1);
        if (distanceX < -1) {
            //Scroll Right or Scroll Up Command Here
//            mSlideViewPager.setCurrentItem(nextPosition);
        } else if (distanceX > 1) {
            Log.d("Gesture Example", "OnFlingLeft");
            //Scroll left or Scroll Down Command Here
//            mSlideViewPager.setCurrentItem(previousePosition);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture Test", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

}
