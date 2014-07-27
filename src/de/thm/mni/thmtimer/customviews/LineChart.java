package de.thm.mni.thmtimer.customviews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



//
// Dritte Version...
//
public class LineChart extends View {
	
	public interface DataPointOnClickListener {
		
		public void onDataPointClicked(Object data);
	}
	
	private class DataPoint {
		
		private Float  mValue;
		private Object mData;
		private Boolean mIsClicked;
		
		public DataPoint(Float value, Object data) {
			
			mValue = value;
			mData  = data;
			mIsClicked = false;
		}
		
		public Float getValue() { return mValue; }
		public Object getData() { return mData; }
		
		public Boolean isClicked() { return mIsClicked; }
		public void setIsClicked(Boolean isClicked) { mIsClicked = isClicked; }
	}
	
	private class ClickableDataPoint {
		
		private DataPoint mDataPoint;
		private Rect mHitBox;
		
		public ClickableDataPoint(DataPoint dataPoint, Rect hitBox) {
			
			mDataPoint = dataPoint;
			mHitBox = hitBox;
		}
		
		public DataPoint getDataPoint() { return mDataPoint; }
		public Rect getHitBox() { return mHitBox; }
	}
	
	public final static Integer[] DEFAULT_LINECHART_COLORS = { 0xFF0099CC,
		                                                        0xFF9933CC,
		                                                        0xFF669900,
		                                                        0xFFFF8800,
		                                                        0xFFCC0000 };
	
	private final static Integer DEFAULT_LINECHART_ORIENTATION_COLOR = 0xFF808080;
	public final static Integer DEFAULT_LINECHART_TEXT_COLOR = 0xFF000000;
	
	private Paint mPaint;
	
	// Farben
	private Integer[] mChartColors;
	private Integer mChartOrientationColor;
	private Integer mTextColor;
	
	// Labels und Chartwerte
	private String[] mLabelsY;
	private String[] mLabelsX;
	private ArrayList<ArrayList<DataPoint>> mChartData;
	private ArrayList<DataPoint> mSeries;
	
	// Größen des sichtbaren Charts
	private boolean mBoundsInvalidated;
	private Integer mVisibleOnX, mTotalOnX;
	private Integer mVisibleOnY, mTotalOnY;
	private Rect mDrawingBound;
	private Rect mVisibleChartBound, mTotalChartBound;
	private Rect mVisibleYAxisBound, mTotalYAxisBound;
	private Rect mVisibleXAxisBound, mTotalXAxisBound;
	
	// Temporäre Variable für Textgrößenberechnungen
	private Rect mTextBounds;
	
	// Scrolling
	private PointF mTouchPosition;
	private PointF mScrollStart;
	private PointF mScrollCurrent;
	private boolean mGrabTouch;
	private boolean mScrolling;
	
	// Klickbare Datenpunkte
	private final int CLICKABLE_BOX_SIZE = 16;
	private ArrayList<ClickableDataPoint> mClickableDataPoints;
	private DataPoint mLastClickedDataPoint;
	private DataPointOnClickListener mListener;
	
	
	public LineChart(Context context,
			         AttributeSet attrs) {
		
		super(context, attrs);
		
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		mChartData = new ArrayList<ArrayList<DataPoint>>();
		
		mClickableDataPoints = new ArrayList<ClickableDataPoint>();
		mLastClickedDataPoint = null;
		mListener = null;
		
		mTouchPosition = new PointF();
		mScrollStart   = new PointF();
		mScrollCurrent = new PointF();
		
		mTextBounds = new Rect();
		mScrolling = false;
		
		setGrabTouch(false);
		setTextSize(22f);
		setChartSize(10, 30, 9, 9);
		setChartColors(DEFAULT_LINECHART_COLORS,
				       DEFAULT_LINECHART_ORIENTATION_COLOR,
				       DEFAULT_LINECHART_TEXT_COLOR);
	}
	
	
	//
	// Größe des Charts festlegen
	//
	public void setChartSize(Integer visibleOnX,
			                 Integer totalOnX,
			                 Integer visibleOnY,
			                 Integer totalOnY) {
		
		mVisibleOnX = visibleOnX;
		mTotalOnX   = totalOnX;
		mVisibleOnY = visibleOnY;
		mTotalOnY   = totalOnY;
		
		mBoundsInvalidated = true;
		invalidate();
	}

	
	//
	// Textgröße festlegen
	//
	public void setTextSize(Float textSize) {
		
		mPaint.setTextSize(textSize);
		
		mBoundsInvalidated = true;
		invalidate();
	}
	
	
	//
	// Farben
	//
	public void setChartColors(Integer[] chartColors,
			                   Integer chartOrientationColor,
			                   Integer textColor) {
		
		mChartColors = chartColors;
		mChartOrientationColor = chartOrientationColor;
		mTextColor = textColor;
		invalidate();
	}
	
	
	//
	// Setzen der Achsenbeschriftungen
	//
	public void setLabels(String[] labelsX,
			              String[] labelsY) {
		
		mLabelsX = labelsX;
		mLabelsY = labelsY;
		
		mBoundsInvalidated = true;
		invalidate();
	}
	
	private String getLabelX(int x) {
		
		return (x < mLabelsX.length) ? mLabelsX[x] : "";
	}
	
	private String getLabelY(int y) {
		
		return (y < mLabelsY.length) ? mLabelsY[y] : "";
	}

	
	//
	// Chartdaten
	//
	public void beginSeries() {
		
		mSeries = new ArrayList<DataPoint>();
	}
	
	public void addValueToSeries(Float value, Object data) {
		
		if(mSeries == null)
			throw new IllegalArgumentException("You have to start a chart series first with beginChartSeries()");
				
		mSeries.add(new DataPoint(value, data));
	}
	
	public void endSeries() {
		
		if(mSeries == null)
			throw new IllegalArgumentException("You have to start a chart series first with beginChartSeries()");
		
		if(mSeries.size() > 0) {
			
			mChartData.add(mSeries);
		}
		
		mBoundsInvalidated = true;
		invalidate();
	}
	
	
	//
	// Datapoint Listener
	//
	public void setDataPointOnClickListener(DataPointOnClickListener listener) {
		
		mListener = listener;
		
		invalidate();
	}
	
	
	//
	// Parents davon abhalten unsere TouchsEvents zu klauen
	//
	public void setGrabTouch(boolean enableGrab) {
		
		mGrabTouch = enableGrab;
	}
	
	
	
	
	//
	// Funktionen zum Zeichnen des Charts
	//
	
	@Override
	public void draw(Canvas canvas) {
		
		if(mChartData.isEmpty()) {
			
			drawNoDataMessage(canvas);
		}
		else {
			
			if(mBoundsInvalidated) {
				
				calculateBounds();
				mBoundsInvalidated = false;
			}
			
			drawYAxis(canvas);
			drawXAxis(canvas);
			drawChart(canvas);
		}
	}
	
	
	private float getRelativePosX(float valueX) {
		
		return (1.0f / mTotalOnX) * valueX;
	}
	
	private float getRelativePosY(float valueY) {
		
		return (1.0f / mTotalOnY) * valueY;
	}
	
	private void drawNoDataMessage(Canvas canvas) {
		
		mDrawingBound = new Rect(getPaddingLeft(),
				                 getPaddingTop(),
				                 getWidth() - getPaddingRight(),
				                 getHeight() - getPaddingBottom());
		
		String message = "No chart data available";
		

		mPaint.setColor(mTextColor);;
		mPaint.setStyle(Style.FILL);
		
		mPaint.getTextBounds(message, 0, message.length(), mTextBounds);
		
		canvas.drawText(message,
				        -mTextBounds.left + ((mDrawingBound.width() - mTextBounds.width()) / 2),
				        -mTextBounds.top  + ((mDrawingBound.height() - mTextBounds.height()) / 2),
				        mPaint);
	}
	
	private void drawXAxis(Canvas canvas) {
		
		canvas.clipRect(mVisibleXAxisBound, Region.Op.REPLACE);
		
		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		
		Float x;
		
		for(int i = 0;
				i < mTotalOnX;
				i++) {
			
			mPaint.getTextBounds(getLabelX(i), 0, getLabelX(i).length(), mTextBounds);
			
			x  = (((getRelativePosX(i) + getRelativePosX(i + 1)) / 2) * mTotalXAxisBound.width()) - (mTextBounds.width() / 2);
			x += mScrollCurrent.x;
			
			canvas.drawText(getLabelX(i),
					        mVisibleXAxisBound.left + x.intValue(),
					        mVisibleXAxisBound.bottom,
					        mPaint);
		}
	}
	
	private void drawYAxis(Canvas canvas) {
		
		canvas.clipRect(mVisibleYAxisBound, Region.Op.REPLACE);
		
		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		
		Float y;
		
		for(int i = 0;
				i < mTotalOnY;
				i++) {
			
			y  = getRelativePosY(i) * mTotalYAxisBound.height();
			y -= mScrollCurrent.y;
			
			canvas.drawText(getLabelY(i),
					        mVisibleYAxisBound.left,
					        mVisibleYAxisBound.bottom - y.intValue(),
					        mPaint);
		}
	}
	
	private void drawChart(Canvas canvas) {
		
		canvas.clipRect(mVisibleChartBound, Region.Op.REPLACE);
		
		//
		// Orientierungshilfen zeichnen
		//
		mPaint.setStrokeWidth(2f);
		mPaint.setColor(mChartOrientationColor);
		
		Float y;
		
		for(int i = 0;
				i < mTotalOnY;
				i++) {
			
			y  = getRelativePosY(i) * mTotalYAxisBound.height();
			y -= mScrollCurrent.y;
			
			canvas.drawLine(mVisibleChartBound.left,
					        mVisibleChartBound.bottom - y.intValue(),
					        mVisibleChartBound.right,
					        mVisibleChartBound.bottom - y.intValue(),
					        mPaint);
		}
		
		//
		// Chart zeichnen
		//
		mPaint.setStrokeWidth(3f);
		
		int colorIdx = 0;
		
		for(ArrayList<DataPoint> chartData : mChartData) {
			
			Float x0 = 0f;
			Float y0 = 0f;
			Float x1 = 0f;
			Float y1 = 0f;
			
			mPaint.setColor(mChartColors[colorIdx]);
			
			for(int i = 0;
					i < chartData.size();
					i++) {
				
				DataPoint data = chartData.get(i);
				
				
				x1  = ((getRelativePosX(i) + getRelativePosX(i + 1)) / 2f) * mTotalXAxisBound.width();
				x1 += mScrollCurrent.x;
				
				y1  = getRelativePosY(data.getValue()) * mTotalYAxisBound.height();
				y1 -= mScrollCurrent.y;
				
				if(i > 0) {

					mPaint.setStyle(Style.STROKE);
					
					canvas.drawLine(mVisibleChartBound.left + x0.intValue(),
							        mVisibleChartBound.bottom - y0.intValue(),
							        mVisibleChartBound.left + x1.intValue(), 
							        mVisibleChartBound.bottom - y1.intValue(),
							        mPaint);
				}
				
				mPaint.setStyle(Style.FILL);
				canvas.drawCircle(mVisibleChartBound.left + x1.intValue(),
						          mVisibleChartBound.bottom - y1.intValue(),
						          data.isClicked() ? 12f : 6f,
						          mPaint);
				
				// Neues Feature: Klickbare Datenpunkte
				if((mListener != null) && !mScrolling) {
					
					if(mVisibleChartBound.contains(mVisibleChartBound.left + x1.intValue(),
							                       mVisibleChartBound.bottom - y1.intValue())) {
						
						Rect hitbox = new Rect(mVisibleChartBound.left + x1.intValue() - CLICKABLE_BOX_SIZE,
								               mVisibleChartBound.bottom - y1.intValue() - CLICKABLE_BOX_SIZE,
								               mVisibleChartBound.left + x1.intValue() + CLICKABLE_BOX_SIZE,
								               mVisibleChartBound.bottom - y1.intValue() + CLICKABLE_BOX_SIZE);
						
						mClickableDataPoints.add(new ClickableDataPoint(data, hitbox));
					}	
				}
				
				x0 = x1;
				y0 = y1;
			}
			
			colorIdx = (colorIdx + 1) % mChartColors.length;
		}
	}

	
	
	//
	// Funktionen um die Größe des Charts zu berechnen
	//	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
		
		mBoundsInvalidated = true;
		invalidate();
	}
	
	@Override
	public void setPadding(int left, int top, int right, int bottom) {

		super.setPadding(left, top, right, bottom);
		
		mBoundsInvalidated = true;
		invalidate();
	}



	private void calculateBounds() {
		
		final int EXTRA_PAD = 15; // Padding zwischen Achsen und Graphen
		
		//
		// Maximale Breite der Labels innerhalb der Y-Achse bestimmen sowie
		// maximale Höhe der Labels innehalb der X-Achse bestimmen
		//
		int maxWidthYAxis  = 0;
		int maxHeightXAxis = 0;
		
		for(int i = 0;
				i < mLabelsY.length;
				i++) {
			
			mPaint.getTextBounds(getLabelY(i), 0, getLabelY(i).length(), mTextBounds);
			
			if(mTextBounds.width() > maxWidthYAxis)
				maxWidthYAxis = mTextBounds.width();
		}
		for(int i = 0;
				i < mLabelsX.length;
				i++) {
			
			mPaint.getTextBounds(getLabelX(i), 0, getLabelX(i).length(), mTextBounds);
			
			if(mTextBounds.height() > maxHeightXAxis)
				maxHeightXAxis = mTextBounds.height();
		}
		
		maxWidthYAxis  += EXTRA_PAD;
		maxHeightXAxis += EXTRA_PAD;
		
		
		//
		// Bounding Boxen für alle Bereiche des Graphen erstellen
		//
		mDrawingBound = new Rect(getPaddingLeft(),
				                 getPaddingTop(),
				                 getWidth() - getPaddingRight(),
				                 getHeight() - getPaddingBottom());
		
		mVisibleXAxisBound = new Rect(mDrawingBound.left + maxWidthYAxis,
				                      mDrawingBound.bottom - maxHeightXAxis,
				                      mDrawingBound.right,
				                      mDrawingBound.bottom);
		
		mVisibleYAxisBound = new Rect(mDrawingBound.left,
				                      mDrawingBound.top,
				                      mDrawingBound.left + maxWidthYAxis,
				                      mVisibleXAxisBound.top);
		
		mVisibleChartBound = new Rect(mVisibleYAxisBound.right,
				                      mDrawingBound.top,
				                      mDrawingBound.right,
				                      mVisibleXAxisBound.top);
		
		Float totalHeightY = ((float)mVisibleYAxisBound.height() / mVisibleOnY) * mTotalOnY;
		Float totalWidthX  = ((float)mVisibleXAxisBound.width()  / mVisibleOnX) * mTotalOnX;
		
		mTotalXAxisBound = new Rect(mVisibleXAxisBound.left,
				                    mVisibleXAxisBound.top,
				                    mVisibleXAxisBound.left + totalWidthX.intValue(),
				                    mVisibleXAxisBound.bottom);
		
		mTotalYAxisBound = new Rect(mVisibleYAxisBound.left,
									mVisibleYAxisBound.top - (totalHeightY.intValue() - mVisibleYAxisBound.height()),
				                    mVisibleYAxisBound.right,
				                    mVisibleYAxisBound.bottom);
		
		mTotalChartBound = new Rect(mVisibleChartBound.left,
				                    mTotalYAxisBound.top,
				                    mTotalXAxisBound.right,
				                    mVisibleChartBound.bottom);
	}
	
	
	
		
	//
	// Funktionen zum Scrollen des Charts
	//
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(mBoundsInvalidated)
			return false;

		// Parents abhalten uns TouchEvents zu klauen
		if(mGrabTouch) {
			
			switch(event.getAction()) {
			
			case MotionEvent.ACTION_MOVE: 
				getParent().requestDisallowInterceptTouchEvent(true);
		        break;
		        
		    case MotionEvent.ACTION_UP:
				performClick();
		    case MotionEvent.ACTION_CANCEL:
		    	getParent().requestDisallowInterceptTouchEvent(false);
		        break;
			}
		}
		else {
			
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		
		// Scrollen / Klicken
		switch(event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			mTouchPosition.set(event.getX(),
					           event.getY());
			mScrollStart.set(mScrollCurrent.x,
					         mScrollCurrent.y);
			break;
			
		case MotionEvent.ACTION_MOVE:
			mScrollCurrent.set(mScrollStart.x + (event.getX() - mTouchPosition.x),
					           mScrollStart.y + (event.getY() - mTouchPosition.y));
			
			// Scrolling auf tatsächliche Chartgröße begrenzen
			if(mScrollCurrent.x + mTotalChartBound.width() < mVisibleChartBound.width())
				mScrollCurrent.x = mVisibleChartBound.width() - mTotalChartBound.width();
			
			if(mScrollCurrent.x > 0f)
				mScrollCurrent.x = 0f;
			
			if(mScrollCurrent.y + mTotalChartBound.bottom > mTotalChartBound.height())
				mScrollCurrent.y = mTotalChartBound.height() - mTotalChartBound.bottom;
				
			if(mScrollCurrent.y < 0f)
				mScrollCurrent.y = 0f;
			
			mScrolling = true;
			invalidate();
			break;
			
		case MotionEvent.ACTION_UP:
			if(mScrolling) {
				
				mScrolling = false;			
				
				if(mClickableDataPoints.size() > 0)
					mClickableDataPoints.clear();
				
				invalidate();
			}
			else {
				
				if(mListener != null)
					handleClick((int)event.getX(),
							    (int)event.getY());
			}
		}
		
		return true;
	}
	
	private void handleClick(int x, int y) {
		
		for(ClickableDataPoint point : mClickableDataPoints) {
			
			if(point.getHitBox().contains(x, y)) {
				
				DataPoint p = point.getDataPoint();
				
				p.setIsClicked(true);
				mListener.onDataPointClicked(p.getData());
				
				if(mLastClickedDataPoint != null) {

					if(p != mLastClickedDataPoint)
						mLastClickedDataPoint.setIsClicked(false);
				}
				mLastClickedDataPoint = p;
				
				invalidate();
				break;
			}
		}
	}
}