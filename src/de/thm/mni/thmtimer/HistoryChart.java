package de.thm.mni.thmtimer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


//
// Erste Version... Noch einiges zu tun... Test
//
public class HistoryChart extends View {
	
	public final static Integer[] DEFAULT_LINE_COLORS = { 0xFF0099CC,
		                                                  0xFF9933CC,
		                                                  0xFF669900,
		                                                  0xFFFF8800,
		                                                  0xFFCC0000 };
	
	public final static Integer DEFAULT_BACKGROUND_COLOR = 0xFF2F2F2F;
	public final static Integer DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
	
	private Paint mPaint;
	private Integer[] mLineColors;
	private Integer mBackgroundColor;
	private Integer mTextColor;
	
	private String[] mLabelsY;
	private String[] mLabelsX;
	
	private Integer mNumVisiblePointsX;
	private Integer mNumVisiblePointsY;
	private Integer mNumTotalPointsX;
	private Integer mNumTotalPointsY;
	
	private PointF mTouch;
	private PointF mScrollStart;
	private PointF mScroll;
	
	private ArrayList<ArrayList<Float>> mChartData;
	private Boolean mChartSizesInvalid;
	
	private Integer mLabelPadLeft;
	private Integer mLabelPadBottom;
	private Integer mGraphPadLeft;
	private Integer mGraphPadBottom;
	
	private Integer mChartWidth;
	private Integer mChartHeight;
	
	
	
	public HistoryChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		mChartData = new ArrayList<ArrayList<Float>>();
		mChartSizesInvalid = true;
		
		mTouch = new PointF();
		mScrollStart = new PointF();
		mScroll = new PointF();
		
		setTextSize(22f);
		setPaddings(30, 30, 15, 15);
		setLineColors(DEFAULT_LINE_COLORS);
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
		setTextColor(DEFAULT_TEXT_COLOR);
	}
	
	
	
	//
	// Visible Size
	//
	public void setVisibleSize(Integer visibleX, Integer visibleY) {
		
		mNumVisiblePointsX = visibleX;
		mNumVisiblePointsY = visibleY;
		
		mChartSizesInvalid = true;
	}
	
	
	//
	// Paddings
	//
	public void setPaddings(Integer labelLeft, Integer labelBottom, Integer graphLeft, Integer graphBottom) {
		
		mLabelPadLeft = labelLeft;
		mLabelPadBottom = labelBottom;
		mGraphPadLeft = graphLeft;
		mGraphPadBottom = graphBottom;
		
		mChartSizesInvalid = true;
	}
	
	
	//
	// Text Size
	//
	public void setTextSize(Float textSize) {
		
		mPaint.setTextSize(textSize);
	}
	
	
	//
	// Colors
	//
	public void setLineColors(Integer[] colors) {
		
		mLineColors = colors;
	}
	
	public void setBackgroundColor(Integer color) {
		
		mBackgroundColor = color;
	}
	
	public void setTextColor(Integer color) {
		
		mTextColor = color;
	}
	
	
	//
	// Labels
	//
	public void setLabels(String[] labelsX, String[] labelsY) {
		
		mLabelsX = labelsX;
		mLabelsY = labelsY;
	}
	
	private String getLabelX(int x) {
		
		return (x < mLabelsX.length) ? mLabelsX[x] : "";
	}
	
	private String getLabelY(int y) {
		
		return (y < mLabelsY.length) ? mLabelsY[y] : "";
	}

	
	//
	// Data
	//
	public void addData(ArrayList<Float> data) {
		
		mChartData.add(data);
		mChartSizesInvalid = true;
	}

	
	
	private void calcChartSize() {
		
		mNumTotalPointsX = 0;
		mNumTotalPointsY = 0;
		
		for(ArrayList<Float> data : mChartData) {
			
			if(data.size() > mNumTotalPointsX)
				mNumTotalPointsX = data.size();
			
			for(Float value : data) {

				int val = Math.round(value);
				
				if(val > mNumTotalPointsY)
					mNumTotalPointsY = val;
			}
		}
		
		mNumTotalPointsY++;
		
		if(mNumTotalPointsX < mNumVisiblePointsX)
			mNumTotalPointsX = mNumVisiblePointsX;
		
		if(mNumTotalPointsY < mNumVisiblePointsY)
			mNumTotalPointsY = mNumVisiblePointsY;
		
		// Größe der sichtbaren Chart-Zeichenebene bestimmen
		mChartWidth  = Math.round(((1f / mNumVisiblePointsX) * (getWidth() - mLabelPadLeft - mGraphPadLeft)) * mNumTotalPointsX); 
		mChartHeight = Math.round(((1f / mNumVisiblePointsY) * (getHeight() - mLabelPadBottom - mGraphPadBottom)) * mNumTotalPointsY);
	}
	
	
	
	private void drawBackground(Canvas canvas) {
		
		// Hintergrund füllen
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		
		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
		
		// Zeilen zeichnen
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(0xFF808080);
		mPaint.setStrokeWidth(3f);
		
		for(int y = 0;
				y < mNumTotalPointsY;
				y++) {
			
			float pY = (getRelativePosY(y) * mChartHeight) + mScroll.y;
			
			if((pY > 0) && (pY < getHeight() - mLabelPadBottom))
				canvas.drawLine(mLabelPadLeft, pY, getWidth(), pY, mPaint);
		}
	}

	private void drawLabels(Canvas canvas) {
		
		Rect textBounds = new Rect();
		
		float pX, pY;
		String label;
		
		//
		// "Platz schaffen" für die Labels
		//
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		
		canvas.drawRect(0, 0, mLabelPadLeft, getHeight(), mPaint);
		canvas.drawRect(0, getHeight() - mLabelPadBottom, getWidth(), getHeight(), mPaint);
		

		//
		// Beschriftungen links und unten anbringen		
		//
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mTextColor);
		
		for(int y = 0;
				y < mNumTotalPointsY;
				y++) {
			
			label = getLabelY(y);
			
			mPaint.getTextBounds(label, 0, label.length(), textBounds);
			
			pX = (mLabelPadLeft - textBounds.width()) / 2f;
			pY = (getRelativePosY(y) * mChartHeight) + mScroll.y;
			
			canvas.drawText(label, pX, pY, mPaint);
		}
		
		for(int x = 0;
				x < mNumTotalPointsX;
				x++) {
			
			label = getLabelX(x);
			
			mPaint.getTextBounds(label, 0, label.length(), textBounds);
			
			pX = mLabelPadLeft + mGraphPadLeft + (((getRelativePosX(x) * mChartWidth) - mScroll.x) - (textBounds.width() / 2f));
			pY = getHeight() - ((mLabelPadBottom - textBounds.height()) / 2f);
			
			canvas.drawText(label, pX, pY, mPaint);
		}
		
		//
		// Überschneidungen der Labels in der linken unteren Ecke bereinigen
		//
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mBackgroundColor);
		
		canvas.drawRect(0, getHeight() - mLabelPadBottom, mLabelPadLeft, getHeight(), mPaint);
	}
	
	private void drawChart(Canvas canvas) {

		int colorIdx = 0;
		float x0, y0, x1, y1;
		
		//
		// Chart zeichnen
		//
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(3f);
		
		for(ArrayList<Float> data : mChartData) {
			
			mPaint.setColor(mLineColors[colorIdx]);
			colorIdx = (colorIdx + 1) % mLineColors.length;
			
			x0 = y0 = x1 = y1 = 0f;
						
			for(int i = 0;
					i < data.size();
					i++) {
				
				x1 = mChartWidth  * getRelativePosX(i);
				y1 = mChartHeight * getRelativePosY(data.get(i));
				
				if(i > 0) {
					
					canvas.drawLine(x0 + mLabelPadLeft + mGraphPadLeft - mScroll.x,
							        y0 + mScroll.y,
							        x1 + mLabelPadLeft + mGraphPadLeft - mScroll.x,
							        y1 + mScroll.y,
							        mPaint);
				}
					
				canvas.drawCircle(x1 + mLabelPadLeft + mGraphPadLeft - mScroll.x,
						          y1 + mScroll.y,
						          4f,
						          mPaint);
				
				x0 = x1;
				y0 = y1;
			}
		}
	}
	
	public float getRelativePosX(float valueX) {
		
		return (1.0f / (mNumTotalPointsX - 1)) * valueX;
	}
	
	public float getRelativePosY(float valueY) {
		
		return 1f - ((1.0f / mNumTotalPointsY) * valueY);
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		
		if(!mChartData.isEmpty()) {
			
			if(mChartSizesInvalid) {
				
				calcChartSize();
				mChartSizesInvalid = false;
			}
			
			drawBackground(canvas);
			drawChart(canvas);
			drawLabels(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			mTouch.set(event.getX(), event.getY());
			mScrollStart.set(mScroll.x, mScroll.y);
			break;
			
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			mScroll.set(mScrollStart.x + (mTouch.x - event.getX()),
					    mScrollStart.y + (mTouch.y - event.getY()));
			
			if(mScroll.x < 0f)
				mScroll.x = 0f;
			
			if(mScroll.x > mChartWidth - getWidth() + 2 * mLabelPadLeft)
				mScroll.x = mChartWidth - getWidth() + 2 * mLabelPadLeft;
			
			if(mScroll.y < -(mChartHeight - getHeight() + mLabelPadBottom + mGraphPadBottom))
				mScroll.y = -(mChartHeight - getHeight() + mLabelPadBottom + mGraphPadBottom);
			
			if(mScroll.y > 0f)
				mScroll.y = 0f;
			
			invalidate();
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		
		mChartSizesInvalid = true;
	}
}