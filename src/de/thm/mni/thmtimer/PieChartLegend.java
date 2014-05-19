package de.thm.mni.thmtimer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;



public class PieChartLegend extends View {

	public final static Integer[] DEFAULT_COLORS = { 0xFF0099CC, 0xFF9933CC, 0xFF669900, 0xFFFF8800, 0xFFCC0000 };
	
	private ArrayList<String> m_labels;
	private Integer[] m_colors;
	private Paint m_paint;
	private Float m_bulletSize;
	private Float m_bulletPadding;
	
	
	
	public PieChartLegend(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		m_labels = new ArrayList<String>();
		
		m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		m_paint.setStrokeWidth(1f);
		
		setTextSize(25f);
		setBulletSize(20f);
		setBulletPadding(15f);
		setColors(DEFAULT_COLORS);
	}
	

	
	public void setTextSize(Float textSize) {
		
		m_paint.setTextSize(textSize);
	}
	
	public void setBulletSize(Float bulletSize) {
		
		m_bulletSize = bulletSize;
	}
	
	public void setBulletPadding(Float bulletPadding) {
		
		m_bulletPadding = bulletPadding;
	}
	
	public void setColors(Integer[] colors) {
		
		m_colors = colors;
	}
	
	private Integer getColor(int entryNumber) {
		
		return m_colors[entryNumber % m_colors.length];
	}
	
	public void addLabel(String label) {
		
		m_labels.add(label);
		invalidate();
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		
		if(m_labels.size() != 0) {
			
			//
			// Position des ersten Bullets bzw. Labels berechnen
			//
			Float bulletX = (float)getPaddingLeft() + (m_bulletSize / 2);
			Float bulletY = (float)getPaddingTop();
			Float labelX  = bulletX + (m_bulletSize / 2) + m_bulletPadding; 
			Float labelY  = bulletY;
			
			//
			// Die Legende zeichnen
			//
			Rect r = new Rect();
			
			for(int i = 0;
					i < m_labels.size();
					i++) {
				
				String label = m_labels.get(i);
				
				
				// Abmessungen des Textes holen
				m_paint.getTextBounds(label, 0, label.length(), r);
				
				if(i == 0) {
					
					// labelY so verschieben das (labelX, labelY) die obere linke Ecke des Textes ist
					labelY += r.height() - r.bottom;
				}
				
				
				//
				// Zeichnen des Bullets
				//
				m_paint.setColor(getColor(i));
				
				canvas.drawRect(bulletX - (m_bulletSize / 2),
						        bulletY + ((r.height() - m_bulletSize) / 2),
						        bulletX + (m_bulletSize / 2),
						        bulletY + ((r.height() - m_bulletSize) / 2) + m_bulletSize,
						        m_paint);

				//
				// Zeichnen des Textes
				//
				m_paint.setColor(Color.BLACK);
				m_paint.setStyle(Paint.Style.FILL);
				canvas.drawText(label, labelX, labelY, m_paint);
				
				
				labelY  += m_bulletPadding + r.height();
				bulletY += m_bulletPadding + r.height();
			}
		}
	}
}