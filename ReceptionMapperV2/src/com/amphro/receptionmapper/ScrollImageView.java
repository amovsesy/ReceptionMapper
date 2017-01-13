package com.amphro.receptionmapper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ScrollImageView extends View {
	/* Padding around the image */
	private final int DEFAULT_PADDING = 10;
	/* Spacing between legends */
	private final int SPACING = 5;
	
	private Display mDisplay;
	private Bitmap mImage;
	private Bitmap mOverlayImage;
	private Bitmap mWarningImage;
	
	private Bitmap mAtt;
	private Bitmap mVerizon;
	private Bitmap mSprint;
	private Bitmap mTmobile;

	/* Current x and y of the touch */
	private float mCurrentX = 0;
	private float mCurrentY = 0;

	private float mTotalX = 0;
	private float mTotalY = 0;
	
	/* The touch distance change from the current touch */
	private float mDeltaX = 0;
	private float mDeltaY = 0;

	int mDisplayWidth;
	int mDisplayHeight;
	int mPadding;

	List<String> mCarriers;
	
	public ScrollImageView(Context context, List<String> carrier) {
		super(context);
		initScrollImageView(context);
	}
	public ScrollImageView(Context context, AttributeSet attributeSet) {
		super(context);
		initScrollImageView(context);
	}
	
	private void initScrollImageView(Context context) {
		mDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mPadding = DEFAULT_PADDING;
		
		mWarningImage = BitmapFactory.decodeResource(getResources(), R.drawable.fakedatawarning);
		mAtt = BitmapFactory.decodeResource(getResources(), R.drawable.att);
		mVerizon = BitmapFactory.decodeResource(getResources(), R.drawable.verizon);
		mSprint = BitmapFactory.decodeResource(getResources(), R.drawable.sprint);
		mTmobile = BitmapFactory.decodeResource(getResources(), R.drawable.tmobile);
		
		mCarriers = new ArrayList<String>();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureDim(widthMeasureSpec, mDisplay.getWidth());
		int height = measureDim(heightMeasureSpec, mDisplay.getHeight());
		setMeasuredDimension(width, height);
	}
	
	private int measureDim(int measureSpec, int size) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = size;
            if (specMode == MeasureSpec.AT_MOST) {
               result = Math.min(result, specSize);
            }
        }
        return result;
    }
	
	public Bitmap getImage() {
		return mImage;
	}
	
	public List<String> getCarriers() {
		return mCarriers;
	}
	
	public void setCarriers(List<String> carriers) {
		if (carriers != null) {
			mCarriers = carriers;
		} else {
			mCarriers = new ArrayList<String>();
		}
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}
	
	public Bitmap getOverlayImage() {
		return mImage;
	}
	
	public void setOverlayImage(Bitmap image) {
		mOverlayImage = image;
	}

	public int getPadding() {
		return mPadding;
	}

	public void setPadding(int padding) {
		this.mPadding = padding;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mCurrentX = event.getRawX();
			mCurrentY = event.getRawY();
		} 
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getRawX();
			float y = event.getRawY();

			// Update how much the touch moved
			mDeltaX = x - mCurrentX;
			mDeltaY = y - mCurrentY;

			mCurrentX = x;
			mCurrentY = y;

			invalidate();
		}
		// Consume event
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mImage == null) {
			return;
		}

		float newTotalX = mTotalX + mDeltaX;
		// Don't scroll off the left or right edges of the bitmap.
		if (mPadding > newTotalX && newTotalX > getMeasuredWidth() - mImage.getWidth() - mPadding)
			mTotalX += mDeltaX;

		float newTotalY = mTotalY + mDeltaY;
		// Don't scroll off the top or bottom edges of the bitmap.
		if (mPadding > newTotalY && newTotalY > getMeasuredHeight() - mImage.getHeight() - mPadding)
			mTotalY += mDeltaY;
		
		Paint paint = new Paint();
		if (mImage != null) {
			canvas.drawBitmap(mImage, mTotalX, mTotalY, paint);
		}
		if (mOverlayImage != null) {
			canvas.drawBitmap(mOverlayImage, mTotalX, mTotalY, paint);
		}
		int vPad = mPadding + SPACING;
		canvas.drawBitmap(mWarningImage, vPad, vPad, paint);
		
		int totalDown = mWarningImage.getHeight() + SPACING*2;
		
		if (mCarriers.contains("att")) {
			canvas.drawBitmap(mAtt, vPad, totalDown, paint);
			totalDown += mAtt.getHeight() + SPACING;
		}
		if (mCarriers.contains("verizon")) {
			canvas.drawBitmap(mVerizon, vPad, totalDown, paint);
			totalDown += mVerizon.getHeight() + SPACING;
		}
		if (mCarriers.contains("sprint")) {
			canvas.drawBitmap(mSprint, vPad, totalDown, paint);
			totalDown += mSprint.getHeight() + SPACING;
		}
		if (mCarriers.contains("tmobile")) {
			canvas.drawBitmap(mTmobile, vPad, totalDown, paint);
			totalDown += mTmobile.getHeight() + SPACING;
		}
		
	}
}