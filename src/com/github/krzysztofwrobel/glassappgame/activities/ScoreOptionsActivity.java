package com.github.krzysztofwrobel.glassappgame.activities;

import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.fragments.NavigateFragment;
import com.github.krzysztofwrobel.glassappgame.fragments.ShareFragment;
import com.github.krzysztofwrobel.glassappgame.models.Reward;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Krzysztof on 06.07.13.
 */
public class ScoreOptionsActivity extends BaseActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    private ViewPager mSettingsViewPager;
    private OptionsPagerAdapter mOptionsPagerAdapter;
    private Reward receivedReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognize_settings_layout);

        mSettingsViewPager = (ViewPager) mSettingsViewPager.findViewById(R.id.vp_recognize_settings);
        mOptionsPagerAdapter = new OptionsPagerAdapter(getSupportFragmentManager());
        receivedReward = getIntent().getParcelableExtra("reward");
        if(receivedReward !=null){
            mOptionsPagerAdapter.addFragment(ShareFragment.getInstance(receivedReward));
            mOptionsPagerAdapter.addFragment(NavigateFragment.getInstance());
            mOptionsPagerAdapter.notifyDataSetChanged();
        }

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
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    private class OptionsPagerAdapter extends FragmentStatePagerAdapter{
        protected ArrayList<Fragment> optionFragments = new ArrayList<Fragment>();

        public void addFragment(Fragment fragment){
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
