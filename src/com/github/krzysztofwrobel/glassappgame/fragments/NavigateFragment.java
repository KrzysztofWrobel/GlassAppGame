package com.github.krzysztofwrobel.glassappgame.fragments;

import com.github.krzysztofwrobel.glassappgame.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NavigateFragment extends Fragment
{

    public static NavigateFragment getInstance()
    {
    	NavigateFragment fragment = new NavigateFragment();
    	return fragment;
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.navigate_layout, container, false);
    	((TextView)v.findViewById(R.id.tv_navigate)).setText(R.string.navigate_to);
    	return v;
    }
}
