package de.thm.mni.thmtimer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;



public class PieChart extends View {
	
	public final static Integer[] DEFAULT_COLORS = { 0xFF0099CC, 0xFF9933CC, 0xFF669900, 0xFFFF8800, 0xFFCC0000 };
	
	private ArrayList<Float> m_values;
	private ArrayList<LinearGradient> m_gradients;
	
	private Integer[] m_colorsNormal;
	private Integer[] m_colorsBright;
	private RectF m_pieBounds;
	private Double m_radius;
	private Paint m_paint;
	
	
	
	public PieChart(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		m_values = new ArrayList<Float>();
		m_gradients = new ArrayList<LinearGradient>();
		
		m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		m_pieBounds = new RectF();
		
		setTextSize(24f);
		setColors(DEFAULT_COLORS);
	}
	
	
	
	public void setTextSize(Float textSize) {
		
		m_paint.setTextSize(textSize);
	}
	
	public void setColors(Integer[] colors) {
		
		m_colorsNormal = colors;
		m_colorsBright = new Integer[colors.length];
		
		for(int i = 0;
				i < colors.length;
				i++) {
			
			Integer r = Color.red(colors[i]);
			Integer g = Color.green(colors[i]);
			Integer b = Color.blue(colors[i]);
			
			m_colorsBright[i] = Color.rgb(Math.min((r + 255) / 2, 255),
					                      Math.min((g + 255) / 2, 255),
					                      Math.min((b + 255) / 2, 255));
		}
	}
	
	private Integer getColorNormal(int entryNumber) {
		
		return m_colorsNormal[entryNumber % m_colorsNormal.length];
	}
	
	private Integer getColorBright(int entryNumber) {
		
		return m_colorsBright[entryNumber % m_colorsBright.length];
	}
	
	public void addValue(Float value) {
		
		m_values.add(value);
		
		m_gradients.clear();
		invalidate();
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		
		if(m_values.size() != 0) {
			
			//
			// Größe des Tortendiagramms bestimmen und sicherstellen, dass es quadratisch wird
			//
			Integer w = getWidth()  - (getPaddingLeft() + getPaddingRight());
			Integer h = getHeight() - (getPaddingTop()  + getPaddingBottom());
			Integer d = w > h ? h : w;
			
			m_pieBounds.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + d, getPaddingTop() + d);
			m_radius = d / 2.0;
			
			
			//
			// Summe aller Werte berechnen
			//
			Float sum = 0f;
	 
			for(Float value : m_values) {
		 
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
						j < m_values.size();
						j++) {
				
					angleSweep = (360f / sum) * m_values.get(j);
		 		
					switch(i) {
					
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
									  String.format("%3.0f%%", (100.0 / sum) * m_values.get(j)));
					}
					
					angleStart += angleSweep;
				}
			}
		}
	}
	
	
	
	private void drawLabel(Canvas canvas, Float angleStart, Float angleSweep, String label) {

		Float angle = -90f + angleStart + (angleSweep / 2f);
		
		// Berechnen des Koordinaten Mittelpunkts des Labels, welches der Mitte unseres Tortenstücks entspricht
		Double x = -Math.sin(Math.toRadians(angle.doubleValue())) * (m_radius / 1.5);
		Double y =  Math.cos(Math.toRadians(angle.doubleValue())) * (m_radius / 1.5);
		
		// Transformation des Koordinatensystems in den Ursprungspunkt (Mitte des Tortendiagramms)
		x += m_pieBounds.centerX();
		y += m_pieBounds.centerY();
		
		// Abmessungen des Labels berechnen
		Rect r = new Rect();
		m_paint.getTextBounds(label, 0, label.length(), r);
		
		// Label mittig an (x, y) zeichnen
		m_paint.setStyle(Paint.Style.FILL);
		m_paint.setColor(Color.BLACK);
		canvas.drawText(label, x.floatValue() - (r.width() / 2f), y.floatValue() + (r.height() / 2f), m_paint);
	}
	
	
	
	private LinearGradient getGradient(Float angleStart, Float angleSweep, Integer pieceNumber) {
		
		LinearGradient gradient;
		
		if((pieceNumber < m_gradients.size()) && (m_gradients.get(pieceNumber) != null)) {
			
			gradient = m_gradients.get(pieceNumber);
		}
		else {
			
			// Berechnen des Koordinaten Endpunkts für den Farbverlauf
			Float angle = -90f + angleStart + (angleSweep / 2f);
			
			Double x = -Math.sin(Math.toRadians(angle.doubleValue())) * m_radius;
			Double y =  Math.cos(Math.toRadians(angle.doubleValue())) * m_radius;
			
			// Transformation des Koordinatensystems in den Ursprungspunkt (Mitte des Tortendiagramms)
			x += m_pieBounds.centerX();
			y += m_pieBounds.centerY();
			
			// Farbverlauf von (centerX, centerY) nach (x, y)
			gradient = new LinearGradient(m_pieBounds.centerX(),
				                          m_pieBounds.centerY(),
				                          x.floatValue(),
				                          y.floatValue(),
				                          getColorBright(pieceNumber),
				                          getColorNormal(pieceNumber),
				                          Shader.TileMode.CLAMP);
			
			m_gradients.add(gradient);
		}
		
		return gradient;
	}
	
	private void drawPiece(Canvas canvas, Float angleStart, Float angleSweep, Integer pieceNumber) {
		
		// Tortenstück zeichnen
		m_paint.setStyle(Paint.Style.FILL);
		m_paint.setShader(getGradient(angleStart, angleSweep, pieceNumber));
		canvas.drawArc(m_pieBounds, angleStart, angleSweep, true, m_paint);
		m_paint.setShader(null);
		
		// Rand des Tortenstücks zeichnen
		m_paint.setStrokeWidth(2f);
		m_paint.setStyle(Paint.Style.STROKE);
		m_paint.setColor(Color.BLACK);
		canvas.drawArc(m_pieBounds, angleStart, angleSweep, true, m_paint);
	}
}