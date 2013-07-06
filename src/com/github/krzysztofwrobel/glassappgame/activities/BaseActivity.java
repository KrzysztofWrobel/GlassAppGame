package com.github.krzysztofwrobel.glassappgame.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import com.github.krzysztofwrobel.glassappgame.GlassApplication;
import com.github.krzysztofwrobel.glassappgame.R;
import com.github.krzysztofwrobel.glassappgame.anim.CenterAnimation;
import com.github.krzysztofwrobel.glassappgame.models.Challenge;


public abstract class BaseActivity extends FragmentActivity implements LocationListener
{
	
	private final static boolean DEBUG = true;
	
	private GlassApplication app;
	
	private boolean mReceiveLocationUpdates;

	private ViewGroup contentView;
	private ViewGroup overlayView;	
	private TextView infoText;
    
    private LocationManager mLocationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		onCreate(savedInstanceState, false);
	}
	
	protected void onCreate(Bundle savedInstanceState, boolean receiveLocationUpdates)
	{
		super.onCreate(savedInstanceState);
		app = (GlassApplication) getApplication();
		super.setContentView(R.layout.content_and_overlay);
		mReceiveLocationUpdates = receiveLocationUpdates;
		contentView = (ViewGroup) findViewById(R.id.content);
		overlayView = (ViewGroup) findViewById(R.id.overlay);
	    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	if(mReceiveLocationUpdates)
    	{
        	try {
    			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1.0f, this);
    		} catch(IllegalArgumentException ignored) {}
    		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1.0f, this);
    	}
    }
    
    @Override
    protected void onPause()
    {
    	if(mReceiveLocationUpdates)
    	{
    		mLocationManager.removeUpdates(this);
    	}
    	super.onPause();
    }
	
	@Override
	public void setContentView(int layoutResID)
	{
		contentView.removeAllViews();
		getLayoutInflater().inflate(layoutResID, contentView);
	}

	protected void showInfoDialog(int imageId, int resId, Object... formatArgs) {
		showInfoDialog(imageId, getString(resId, formatArgs));
	}

	protected void showInfoDialog(final int imageId, final String text) {
		dismissInfoDialog();
		overlayView.post(new Runnable()
		{
			
			@Override
			public void run()
			{
				infoText = new TextView(BaseActivity.this);
				infoText.setCompoundDrawablesWithIntrinsicBounds(imageId, 0, 0, 0);
				infoText.setText(text);
				infoText.setTextSize(50);
				infoText.setGravity(Gravity.CENTER);
				AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(overlayView.getWidth(), overlayView.getHeight(), 0, overlayView.getHeight());
				overlayView.addView(infoText, lp);
				infoText.setAnimation(new CenterAnimation(infoText, 500, 0));
			}
		});
	}

	protected void dismissInfoDialog() {
		try {
			if (infoText != null) {
				overlayView.removeView(infoText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void log(String msg) {
		if (DEBUG) {
			Log.v(getClass().getSimpleName(), msg);
		}
	}

	@Override
	public void onLocationChanged(Location location)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}
	
	protected void takeDirection(double lat, double lng)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +lat+","+lng));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public List<Challenge> getChallenges()
	{
		return app.getChallenges();
	}

	public void setChallenges(List<Challenge> mChallenges)
	{
		app.setChallenges(mChallenges);
	}
	
}
