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

	private ArrayList<String> mLabels;
	private Integer[] mColors;
	private Paint mPaint;
	private Float mBulletSize;
	private Float mBulletPadding;

	public PieChartLegend(Context context, AttributeSet attrs) {
		super(context, attrs);

		mLabels = new ArrayList<String>();

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(1f);

		setTextSize(25f);
		setBulletSize(20f);
		setBulletPadding(15f);
		setColors(DEFAULT_COLORS);
	}

	public void setTextSize(Float textSize) {
		mPaint.setTextSize(textSize);
	}

	public void setBulletSize(Float bulletSize) {
		mBulletSize = bulletSize;
	}

	public void setBulletPadding(Float bulletPadding) {
		mBulletPadding = bulletPadding;
	}

	public void setColors(Integer[] colors) {
		mColors = colors;
	}

	private Integer getColor(int entryNumber) {
		return mColors[entryNumber % mColors.length];
	}

	public void addLabel(String label) {
		mLabels.add(label);
		invalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mLabels.size() != 0) {
			//
			// Position des ersten Bullets bzw. Labels berechnen
			//
			Float bulletX = (float) getPaddingLeft() + (mBulletSize / 2);
			Float bulletY = (float) getPaddingTop();
			Float labelX = bulletX + (mBulletSize / 2) + mBulletPadding;
			Float labelY = bulletY;

			//
			// Die Legende zeichnen
			//
			Rect r = new Rect();

			for (int i = 0; i < mLabels.size(); i++) {

				String label = mLabels.get(i);

				// Abmessungen des Textes holen
				mPaint.getTextBounds(label, 0, label.length(), r);

				if (i == 0) {

					// labelY so verschieben das (labelX, labelY) die obere
					// linke Ecke des Textes ist
					labelY += r.height() - r.bottom;
				}

				//
				// Zeichnen des Bullets
				//
				mPaint.setColor(getColor(i));

				canvas.drawRect(bulletX - (mBulletSize / 2), bulletY + ((r.height() - mBulletSize) / 2), bulletX
						+ (mBulletSize / 2), bulletY + ((r.height() - mBulletSize) / 2) + mBulletSize, mPaint);

				//
				// Zeichnen des Textes
				//
				mPaint.setColor(Color.BLACK);
				mPaint.setStyle(Paint.Style.FILL);
				canvas.drawText(label, labelX, labelY, mPaint);

				labelY += mBulletPadding + r.height();
				bulletY += mBulletPadding + r.height();
			}
		}
	}
}