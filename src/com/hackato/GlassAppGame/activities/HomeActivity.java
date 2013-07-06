package com.hackato.GlassAppGame.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hackato.GlassAppGame.R;
import com.hackato.GlassAppGame.fragments.HomeSlideFragment;
import com.hackato.GlassAppGame.models.Challenge;

public class HomeActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    private ViewPager mSlideViewPager;
    private ScreenSlidePagerAdapter mSlidePagerAdapter;
    private GestureDetector gestureDetector;
    private FragmentManager mSupportFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true);
        setContentView(R.layout.home_activity);

        mSupportFragmentManager = getSupportFragmentManager();

        gestureDetector = new GestureDetector(this, this);

        mSlideViewPager = (ViewPager) findViewById(R.id.vp_home_slides);
        mSlidePagerAdapter = new ScreenSlidePagerAdapter(mSupportFragmentManager);
        mSlideViewPager.setAdapter(mSlidePagerAdapter);
        mSlideViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        
//        showInfoDialog(0, R.string.app_name);

    }
    
    @Override
    public void onLocationChanged(Location location)
    {
    	// TODO start connection
    	int lat = (int)(location.getLatitude() * 1E6);
    	int lng = (int)(location.getLongitude() * 1E6);
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
