package com.a4455jkjh.colorpicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.graphics.Rect;

public class TestView extends View {
	private final Paint paint;
	private final AlphaDrawable alphaDrawable;
	private final Rect rect;
	public TestView(Context c) {
		this(c, null);
	}
	public TestView(Context c, AttributeSet a) {
		super(c, a);
		alphaDrawable = new AlphaDrawable(8);
		paint = new Paint();
		rect = new Rect();
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int w1 = w / 10;
		int h1 = h / 20;
		rect.set(w1, h1, w - w1, h - h1);
		alphaDrawable.setBounds(rect);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		alphaDrawable.draw(canvas);
		canvas.drawRect(rect, paint);
	}
	private int color=0;
	public void setColor(int color) {
		int x = getWidth() / 2 ;
		LinearGradient s = new LinearGradient(x - 1, 0, x + 1, 0,
											  this.color, color, LinearGradient.TileMode.CLAMP);
		paint.setShader(s);
		invalidate();
	}

	public void setInitColor(int color) {
		this.color = color;
		int x = getWidth() / 2 ;
		LinearGradient s = new LinearGradient(x - 1, 0, x + 1, 0,
											  color, color, LinearGradient.TileMode.CLAMP);
		paint.setShader(s);
		invalidate();
	}
    
    public int getColor() {
        return color;
    }
}
