package com.hackato.GlassAppGame.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;

/**
 * This animation class is animating the view to the center of parent (RelativeLayout).
 * 
 * @author Michal Kisiel
 * 
 */
public class CenterAnimation extends Animation
{
	private View mAnimatedView;
	private View mParent;
	private LayoutParams mViewLayoutParams;
	private int mTopStart, mTopEnd;
	private boolean mWasEndedAlready = false;

	/**
	 * Initialize the animation
	 * 
	 * @param view
	 *            The layout we want to animate
	 * @param duration
	 *            The duration of the animation, in ms
	 */
	public CenterAnimation(View view, int duration, int topEnd)
	{
		if(!(view.getParent() instanceof AbsoluteLayout))
			throw new RuntimeException("View's parent must be instance of RelativeLayout class");
		
		setDuration(duration);
		mAnimatedView = view;
		mParent = (View) view.getParent();
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();
		
		mTopStart = mViewLayoutParams.y;
		mTopEnd = topEnd;

		view.setVisibility(View.VISIBLE);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{
		super.applyTransformation(interpolatedTime, t);

		if (interpolatedTime < 1.0f)
		{

			// Calculating the new bottom margin, and setting it
			mViewLayoutParams.y = mTopStart + (int) ((mTopEnd - mTopStart) * interpolatedTime);

			// Invalidating the layout, making us seeing the changes we made
			mAnimatedView.requestLayout();

			// Making sure we didn't run the ending before (it happens!)
		} else if (!mWasEndedAlready)
		{
			mViewLayoutParams.y = mTopEnd;
			mAnimatedView.requestLayout();

			mWasEndedAlready = true;
		}
	}
}