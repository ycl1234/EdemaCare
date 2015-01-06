/**
 * Created by Timeszoro on 2014/12/16.
 */
package com.timeszoro.edemacare;


import android.content.Intent;
import com.example.edemacare.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import com.timeszoro.mode.BleDevicesLab;

public class ScanProFragment extends Fragment {
	private static final String TAG = "scan Fragement";
	CircularProgressDrawable mDrawable;
	Animator mCurrentAnimation;
    ImageView mScanImg;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scan, container, false);
		mScanImg = (ImageView)v.findViewById(R.id.bt_scan);
		mDrawable = new CircularProgressDrawable.Builder()
		.setContext(getActivity())
        .setRingColor(getResources().getColor(R.color.ring_color))
        .create();
        mScanImg.setImageDrawable(mDrawable);
        mScanImg.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi") @Override
			public void onClick(View v) {


                //begin scan device
                String scanBroad =  getString(R.string.ble_scan_broadcast);
                Intent intent = new Intent(scanBroad);
                intent.putExtra("BeginScan",true);
                getActivity().sendBroadcast(intent);

                //disable the scan button
                mScanImg.setEnabled(false);
				Log.d(TAG, "scan button is clicked");
                if(mCurrentAnimation != null){
                    mCurrentAnimation.cancel();
                }
                mCurrentAnimation = prepareStyle2Animation();
                mCurrentAnimation.addListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mScanImg.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
				mCurrentAnimation.start();

			}
		});
		return v;
	}

    public ImageView getmScanImg() {
        return mScanImg;
    }

    //**************different animation for the scan button*******************//
	@SuppressLint("NewApi") 
    private Animator prepareStyle2Animation() {


        AnimatorSet animation = new AnimatorSet();
        int  duration = Integer.valueOf(getString(R.string.scan_interval));
        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.PROGRESS_PROPERTY,
                0f, 1f);
        progressAnimation.setDuration(duration);
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());



        
        return progressAnimation;
//        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(mDrawable, CircularProgressDrawable.RING_COLOR_PROPERTY,
//                getResources().getColor(android.R.color.holo_red_dark),
//                getResources().getColor(android.R.color.holo_green_light));
//        colorAnimator.setEvaluator(new ArgbEvaluator());
//        colorAnimator.setDuration(duration);
//
//        animation.playTogether(progressAnimation, colorAnimator);
//        return animation;
    }
	
	
	@SuppressLint("NewApi") 
	private Animator preparePressedAnimation() {
        Animator animation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY,
                mDrawable.getCircleScale(), 0.65f);
        animation.setDuration(120);
        return animation;
    }
	@SuppressLint("NewApi") 
	 private Animator preparePulseAnimation() {
	        AnimatorSet animation = new AnimatorSet();

	        Animator firstBounce = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY,
	        		mDrawable.getCircleScale(), 0.88f);
	        firstBounce.setDuration(300);
	        firstBounce.setInterpolator(new CycleInterpolator(1));
	        Animator secondBounce = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY,
	                0.75f, 0.83f);
	        secondBounce.setDuration(300);
	        secondBounce.setInterpolator(new CycleInterpolator(1));
	        Animator thirdBounce = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY,
	                0.75f, 0.80f);
	        thirdBounce.setDuration(300);
	        thirdBounce.setInterpolator(new CycleInterpolator(1));

	        animation.playSequentially(firstBounce, secondBounce, thirdBounce);
	        return animation;
	    }
	 
	@SuppressLint("NewApi") 
	private Animator prepareStyle1Animation() {
        AnimatorSet animation = new AnimatorSet();

        final Animator indeterminateAnimation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0, 3600);
        indeterminateAnimation.setDuration(3600);

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 0.75f);
        innerCircleAnimation.setDuration(3600);
        innerCircleAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            	mDrawable.setIndeterminate(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                indeterminateAnimation.end();
                mDrawable.setIndeterminate(false);
                mDrawable.setProgress(0);
            }
        });

        animation.playTogether(innerCircleAnimation, indeterminateAnimation);
        return animation;
    }
	
    
    @SuppressLint("NewApi") 
    private Animator prepareStyle3Animation() {
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0.75f, 0f);
        progressAnimation.setDuration(1200);
        progressAnimation.setInterpolator(new AnticipateInterpolator());

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0.75f, 0f);
        innerCircleAnimation.setDuration(1200);
        innerCircleAnimation.setInterpolator(new AnticipateInterpolator());

        ObjectAnimator invertedProgress = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0f, 0.75f);
        invertedProgress.setDuration(1200);
        invertedProgress.setStartDelay(3200);
        invertedProgress.setInterpolator(new OvershootInterpolator());

        Animator invertedCircle = ObjectAnimator.ofFloat(mDrawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 0.75f);
        invertedCircle.setDuration(1200);
        invertedCircle.setStartDelay(3200);
        invertedCircle.setInterpolator(new OvershootInterpolator());

        animation.playTogether(progressAnimation, innerCircleAnimation, invertedProgress, invertedCircle);
        return animation;
    }



}
