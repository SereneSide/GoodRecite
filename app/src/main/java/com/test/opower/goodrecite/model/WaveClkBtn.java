package com.test.opower.goodrecite.model;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

/**
 * Created by opower on 16-6-24.
 */
public class WaveClkBtn extends Button
{
	private Paint pit = new Paint();
	private final int AnimDuration = 500;
	private AnimatorSet as = null;
	private boolean isDuringAnim = false;
	private int clkX = 0;
	private int clkY = 0;
	private float clkRds = 0;
	private Property<WaveClkBtn, Float> clkRdsPpt = new Property<WaveClkBtn, Float>(
			Float.class, "radius")
	{
		@Override
		public Float get(WaveClkBtn waveClkBtn)
		{
			return waveClkBtn.clkRds;
		}

		@Override
		public void set(WaveClkBtn object, Float value)
		{
			object.clkRds = value;
			//刷新画布
			invalidate();
		}
	};
	private Property<WaveClkBtn, Integer> clkClrPpt = new Property<WaveClkBtn, Integer>(
			Integer.class, "background_color")
	{
		@Override
		public Integer get(WaveClkBtn waveClkBtn)
		{
			return pit.getColor();
		}

		@Override
		public void set(WaveClkBtn object, Integer value)
		{
			object.pit.setColor(value);
		}
	};
	private OnClickListener cbas = null;

	public WaveClkBtn setOnClickListener(OnClickListener l)
	{
		cbas = l;	return this;
	}

	public WaveClkBtn(Context context)
	{
		this(context, null, 0);
	}

	public WaveClkBtn(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public WaveClkBtn(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		as = new AnimatorSet();
		as.addListener(new Animator.AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator animator)
			{
				isDuringAnim = true;
			}

			@Override
			public void onAnimationEnd(Animator animator)
			{
				isDuringAnim = false;
				if(cbas != null)
				{
					cbas.onClick(WaveClkBtn.this);
				}
			}

			@Override
			public void onAnimationCancel(Animator animator)
			{

			}

			@Override
			public void onAnimationRepeat(Animator animator)
			{

			}
		});
		//as.setDuration(1200/getHeight()*endRds);
		as.setDuration(AnimDuration);
		as.setInterpolator(new DecelerateInterpolator());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN && !isDuringAnim)
		{
			clkX = (int) event.getX();
			clkY = (int) event.getY();

			int begRds = 0;
			int endRds = getWidth()>>1;

			int begClr = Color.BLACK;
			int endClr = Color.TRANSPARENT;

			as.playTogether(
					ObjectAnimator.ofFloat(this, clkRdsPpt, begRds, endRds),
					ObjectAnimator.ofObject(this, clkClrPpt, new ArgbEvaluator(), begClr, endClr));
			as.start();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		canvas.save();
		canvas.drawCircle(clkX, clkY, clkRds, pit);
		canvas.restore();
	}

	public interface OnClickListener
	{
		void onClick(View vw);
	}
}
