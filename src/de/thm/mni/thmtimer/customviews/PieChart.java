package de.thm.mni.thmtimer.customviews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {
	
	public final static Integer[] DEFAULT_PIECHART_COLORS = { 0xFF0099CC,
		                                                      0xFF9933CC,
		                                                      0xFF669900,
		                                                      0xFFFF8800,
		                                                      0xFFCC0000 };
	
	public final static Integer DEFAULT_PIECHART_TEXT_COLOR = 0xFF000000;

	private Paint mPaint;
	
	private ArrayList<Float> mValues;
	private ArrayList<LinearGradient> mGradients;

	private Integer[] mColorsNormal;
	private Integer[] mColorsBright;
	private Integer mTextColor;
	
	private RectF mDrawingBound;
	private Rect mTextBounds;
	
	private Double mRadius;
	

	public PieChart(Context context, AttributeSet attrs) {
		
		super(context, attrs);

		mValues = new ArrayList<Float>();
		mGradients = new ArrayList<LinearGradient>();

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		mDrawingBound = new RectF();
		mTextBounds = new Rect();

		setTextSize(24f);
		setColors(DEFAULT_PIECHART_COLORS,
				  DEFAULT_PIECHART_TEXT_COLOR);
	}

	
	public void setTextSize(Float textSize) {
		
		mPaint.setTextSize(textSize);
	}

	public void setColors(Integer[] chartColors, Integer textColor) {
		
		mColorsNormal = chartColors;
		mColorsBright = new Integer[chartColors.length];

		for (int i = 0;
				 i < chartColors.length;
				 i++) {
			
			Integer r = Color.red(chartColors[i]);
			Integer g = Color.green(chartColors[i]);
			Integer b = Color.blue(chartColors[i]);

			mColorsBright[i] = Color.rgb(Math.min((r + 255) / 2, 255),
					                     Math.min((g + 255) / 2, 255),
					                     Math.min((b + 255) / 2, 255));
		}
		
		mTextColor = textColor;
	}

	private Integer getColorNormal(int entryNumber) {
		
		return mColorsNormal[entryNumber % mColorsNormal.length];
	}

	private Integer getColorBright(int entryNumber) {
		
		return mColorsBright[entryNumber % mColorsBright.length];
	}
	
	public void addValue(Float value) {
		
		mValues.add(value);
		mGradients.clear();
		
		invalidate();
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		
		if(mValues.isEmpty()) {
			
			drawNoDataMessage(canvas);
		}
		else {
			
			//
			// Summe aller Werte berechnen
			//
			Float sum = 0f;

			for(Float value : mValues) {

				sum += value;
			}

			//
			// Das Tortendiagramm zeichnen
			//
			Float angleStart;
			Float angleSweep;

			for(int i = 0;
					i < 2;
					i++) {
				
				angleStart = 0f;
				angleSweep = 0f;

				for(int j = 0;
						j < mValues.size();
						j++) {
					
					angleSweep = (360f / sum) * mValues.get(j);

					switch (i) {
					
					// Tortenstück zeichnen
					case 0:
						drawPiece(canvas,
								  angleStart,
								  angleSweep,
								  j);
						break;
						
					// Beschriftung zeichnen
					case 1:
						drawLabel(canvas,
								  angleStart,
								  angleSweep,
								  String.format("%3.0f%%",
										        (100.0 / sum) * mValues.get(j)));
					}

					angleStart += angleSweep;
				}
			}
		}
	}
	
	private void drawNoDataMessage(Canvas canvas) {
		
		String message = "No chart data available";
		

		mPaint.setColor(mTextColor);;
		mPaint.setStyle(Style.FILL);
		
		mPaint.getTextBounds(message, 0, message.length(), mTextBounds);
		
		canvas.drawText(message,
				        -mTextBounds.left + ((mDrawingBound.width() - mTextBounds.width()) / 2),
				        -mTextBounds.top  + ((mDrawingBound.height() - mTextBounds.height()) / 2),
				        mPaint);
	}
	
	private void drawLabel(Canvas canvas, Float angleStart, Float angleSweep, String label) {
		
		Float angle = -90f + angleStart + (angleSweep / 2f);

		// Berechnen des Koordinaten Mittelpunkts des Labels, welches der Mitte
		// unseres Tortenstücks entspricht
		Double x = -Math.sin(Math.toRadians(angle.doubleValue())) * (mRadius / 1.5);
		Double y =  Math.cos(Math.toRadians(angle.doubleValue())) * (mRadius / 1.5);
		
		// Transformation des Koordinatensystems in den Ursprungspunkt (Mitte
		// des Tortendiagramms)
		x += mDrawingBound.centerX();
		y += mDrawingBound.centerY();

		// Abmessungen des Labels berechnen
		mPaint.getTextBounds(label, 0, label.length(), mTextBounds);
		
		// Label mittig an (x, y) zeichnen
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(mTextColor);
		
		canvas.drawText(label,
				        x.floatValue() - (mTextBounds.width() / 2f),
				        y.floatValue() + (mTextBounds.height() / 2f),
				        mPaint);
	}

	private LinearGradient getGradient(Float angleStart, Float angleSweep, Integer pieceNumber) {
		
		LinearGradient gradient;

		if((pieceNumber < mGradients.size()) && (mGradients.get(pieceNumber) != null)) {

			gradient = mGradients.get(pieceNumber);
		}
		else {

			// Berechnen des Koordinaten Endpunkts für den Farbverlauf
			Float angle = -90f + angleStart + (angleSweep / 2f);

			Double x = -Math.sin(Math.toRadians(angle.doubleValue())) * mRadius;
			Double y =  Math.cos(Math.toRadians(angle.doubleValue())) * mRadius;

			// Transformation des Koordinatensystems in den Ursprungspunkt
			// (Mitte des Tortendiagramms)
			x += mDrawingBound.centerX();
			y += mDrawingBound.centerY();

			// Farbverlauf von (centerX, centerY) nach (x, y)
			gradient = new LinearGradient(mDrawingBound.centerX(),
					                      mDrawingBound.centerY(),
					                      x.floatValue(),
					                      y.floatValue(),
					                      getColorBright(pieceNumber),
					                      getColorNormal(pieceNumber),
					                      Shader.TileMode.CLAMP);
			
			mGradients.add(gradient);
		}

		return gradient;
	}

	private void drawPiece(Canvas canvas, Float angleStart, Float angleSweep, Integer pieceNumber) {
		
		// Tortenstück zeichnen
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setShader(getGradient(angleStart, angleSweep, pieceNumber));
		canvas.drawArc(mDrawingBound, angleStart, angleSweep, true, mPaint);
		mPaint.setShader(null);

		// Rand des Tortenstücks zeichnen
		mPaint.setStrokeWidth(2f);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.BLACK);
		canvas.drawArc(mDrawingBound, angleStart, angleSweep, true, mPaint);
	}
	
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		super.onSizeChanged(w, h, oldw, oldh);
		
		mGradients.clear();
		calculateSize();
		
		invalidate();
	}
	
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		
		super.setPadding(left, top, right, bottom);
		
		mGradients.clear();
		calculateSize();
		
		invalidate();
	}
	
	private void calculateSize() {
		
		//
		// Größe des Tortendiagramms bestimmen und sicherstellen, dass es
		// quadratisch wird
		//
		Integer w = getWidth() - (getPaddingLeft() + getPaddingRight());
		Integer h = getHeight() - (getPaddingTop() + getPaddingBottom());
		Integer d = w > h ? h : w;
		
		int left = getPaddingLeft() + ((w - d) / 2);
		int top  = getPaddingTop()  + ((h - d) / 2);
		int right = left + d;
		int bottom = top + d;
		
		mDrawingBound.set(left, top, right, bottom);
		mRadius = d / 2.0;
	}
}