package com.hackato.GlassAppGame.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import com.hackato.GlassAppGame.R;
import com.hackato.GlassAppGame.anim.*;


public class BaseActivity extends FragmentActivity
{
	
	private final static boolean DEBUG = true;

	private ViewGroup contentView;
	private ViewGroup overlayView;	
	private TextView infoText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.content_and_overlay);
		contentView = (ViewGroup) findViewById(R.id.content);
		overlayView = (ViewGroup) findViewById(R.id.overlay);
	}
	
	@Override
	public void setContentView(int layoutResID)
	{
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

}
