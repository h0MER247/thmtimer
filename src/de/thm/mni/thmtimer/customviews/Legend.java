package de.thm.mni.thmtimer.customviews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class Legend extends View {

	public final static Integer[] DEFAULT_LEGEND_BOX_COLORS = { 0xFF0099CC,
												  	    	    0xFF9933CC,
												  	    	    0xFF669900,
												  	    	    0xFFFF8800,
												  	    	    0xFFCC0000 };
	
	public final static Integer DEFAULT_LEGEND_TEXT_COLOR = 0xFF000000;
	
	private Paint mPaint;
	
	// Breite und Abstand eines Legendenelements
	private final Integer BOX_WIDTH = 15;
	private final Integer BOX_PAD   = 20;
	
	// Zeichenbereich
	private RectF mDrawingBounds;
	
	// Farben
	private Integer[] mElementColors;
	private Integer mTextColor;
	
	// Labels
	private ArrayList<String> mLabels;
	private Rect mTextBounds;
	
	// Legendenkästchen nebeneinander oder untereinander zeichnen
	private boolean mDrawSideBySide;
	
	
	
	public Legend(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDrawingBounds = new RectF();
		
		mTextBounds = new Rect();
		mLabels = new ArrayList<String>();
		
		setTextSize(24f);
		setDrawSideBySide(false);
		setLegendColors(DEFAULT_LEGEND_BOX_COLORS,
				        DEFAULT_LEGEND_TEXT_COLOR);
	}
	
	
	
	//
	// Textgröße festlegen
	//
	public void setTextSize(Float textSize) {
		
		mPaint.setTextSize(textSize);
	}
	
	
	//
	// Farben der Legendenelemente und des Textes
	//
	public void setLegendColors(Integer[] elementColors,
			                    Integer textColor) {
		
		mElementColors = elementColors;
		mTextColor = textColor;
	}
	
	
	//
	// Legendenelemente nebeneinander oder untereinander zeichnen
	//
	public void setDrawSideBySide(Boolean drawSideBySide) {
		
		mDrawSideBySide = drawSideBySide;
	}
	
	
	//
	// Ein Label hinzufügen
	//
	public void addLegendLabel(String label) {
		
		mLabels.add(label);
	}
	
	
	
	

	@Override
	public void draw(Canvas canvas) {

		// Zeichenbereich festlegen
		mDrawingBounds.set(getPaddingLeft(),
				           getPaddingTop(),
				           getWidth() - getPaddingRight(),
				           getHeight() - getPaddingBottom());
		
		canvas.clipRect(mDrawingBounds, Region.Op.REPLACE);
		
		//
		// Legende zeichnen
		//
		Float x = mDrawingBounds.left;
		Float y = mDrawingBounds.top;
		Integer maxTextHeight = 0;
		
		for(int i = 0;
				i < mLabels.size();
				i++) {
			
			String  text  = mLabels.get(i);
			Integer color = mElementColors[i % mElementColors.length];
			
			mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
			
			
			// Prüfen ob der Text noch in die aktuelle Zeile passt
			if(mDrawSideBySide) {
				
				if(x + mTextBounds.width() + BOX_WIDTH + 5f > mDrawingBounds.width()) {
					
					x = mDrawingBounds.left;
					y += maxTextHeight + 5;
					
					maxTextHeight = 0;
				}
				
				if(mTextBounds.height() > maxTextHeight)
					maxTextHeight = mTextBounds.height();
			}
			
			
			// Farbkästchen zeichnen
			mPaint.setColor(color);
			mPaint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawRect(x,
					        y,
					        x + BOX_WIDTH,
					        y + mTextBounds.height(),
					        mPaint);
			
			// Text zeichnen
			mPaint.setColor(mTextColor);
			canvas.drawText(text,
					        -mTextBounds.left + (x + BOX_WIDTH) + 5f,
					        -mTextBounds.top + y,
					        mPaint);
			
			
			if(mDrawSideBySide)
				x += mTextBounds.width() + BOX_WIDTH + BOX_PAD + 5f;
			else
				y += mTextBounds.height() + 5f;
		}
	}
}