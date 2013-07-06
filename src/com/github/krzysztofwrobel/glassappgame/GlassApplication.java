package com.github.krzysztofwrobel.glassappgame;

import java.util.ArrayList;
import java.util.List;

import com.github.krzysztofwrobel.glassappgame.models.Challenge;

import android.app.Application;

public class GlassApplication extends Application
{

    private List<Challenge> mChallenges;
    
    @Override
    public void onCreate()
    {
    	super.onCreate();

        mChallenges = new ArrayList<Challenge>();
    }

	public List<Challenge> getChallenges()
	{
		return mChallenges;
	}

	public void setChallenges(List<Challenge> challenges)
	{
		this.mChallenges = challenges;
	}
    
}
