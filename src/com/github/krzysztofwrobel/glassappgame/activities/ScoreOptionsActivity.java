package com.github.krzysztofwrobel.glassappgame.activities;

import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.fragments.NavigateFragment;
import com.github.krzysztofwrobel.glassappgame.fragments.OnFragmentClickedListener;
import com.github.krzysztofwrobel.glassappgame.fragments.ShareFragment;
import com.github.krzysztofwrobel.glassappgame.models.Reward;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class ScoreOptionsActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    private static final int SHARE_FRAGMENT_POSITION = 0;
    private static final int NAVIGATE_FRAGMENT_POSITION = 1;
    private ViewPager mSettingsViewPager;
    private OptionsPagerAdapter mOptionsPagerAdapter;
    private Reward receivedReward;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognize_settings_layout);
        gestureDetector = new GestureDetector(this,this);

        mSettingsViewPager = (ViewPager) findViewById(R.id.vp_recognize_settings);
        mOptionsPagerAdapter = new OptionsPagerAdapter(getSupportFragmentManager());
        mSettingsViewPager.setAdapter(mOptionsPagerAdapter);
        receivedReward = getIntent().getParcelableExtra("reward");
        if (receivedReward != null) {
            mOptionsPagerAdapter.addFragment(ShareFragment.getInstance(receivedReward));
            mOptionsPagerAdapter.addFragment(NavigateFragment.getInstance());
            mOptionsPagerAdapter.notifyDataSetChanged();
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
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        int currentPosition = mSettingsViewPager.getCurrentItem();
        if (currentPosition == SHARE_FRAGMENT_POSITION) {
            ((OnFragmentClickedListener)mOptionsPagerAdapter.getItem(SHARE_FRAGMENT_POSITION)).fragmentClicked();
        } else if (currentPosition == NAVIGATE_FRAGMENT_POSITION){
            takeDirection((double)receivedReward.getLatitude()/10e6,(double)receivedReward.getLongitude()/10e6);
        }

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
        int currentPosition = mSettingsViewPager.getCurrentItem();
        int nextPosition = Math.min(mOptionsPagerAdapter.getCount(), currentPosition + 1);
        int previousePosition = Math.max(0, currentPosition - 1);
        if (velocityX < -3500) {
            mSettingsViewPager.setCurrentItem(nextPosition);
        } else if (velocityX > 3500) {
            mSettingsViewPager.setCurrentItem(previousePosition);
        }

        return true;
    }

    private class OptionsPagerAdapter extends FragmentStatePagerAdapter {
        protected ArrayList<Fragment> optionFragments = new ArrayList<Fragment>();

        public void addFragment(Fragment fragment) {
            optionFragments.add(fragment);
        }

        public OptionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return optionFragments.get(i);
        }

        @Override
        public int getCount() {
            return optionFragments.size();
        }
    }
}
