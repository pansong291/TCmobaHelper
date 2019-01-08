package com.a4455jkjh.colorpicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.ComposeShader;
import android.graphics.Bitmap;

public class ColorPickerView extends View {
	private static final int labelPaintSize = 3;
	private final Paint huePaint;
	private final Paint satPaint;
	private final Paint alphaPaint;
	private final Paint valPaint;

	private final Paint labelPaint;

	private final Rect hueRect;
	private final Rect satRect;
	private final Rect alphaRect;

	private final AlphaDrawable alpha;
	private Shader satShader,valShader;

	private float h,s,v;
	private int a;
	private float huey,satx,saty,alphax;

	private OnColorChangedListener listener;
	public ColorPickerView(Context c) {
		this(c, null);
	}
	public ColorPickerView(Context c, AttributeSet a) {
		super(c, a);
		huePaint = new Paint();
		satPaint = new Paint();
		alphaPaint = new Paint();
		alpha = new AlphaDrawable(8);
		satRect = new Rect();
		hueRect = new Rect();
		alphaRect = new Rect();
		valPaint = new Paint();
		listener = null;
		valPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		Paint labelPaint = new Paint();
		labelPaint.setAntiAlias(true);
		labelPaint.setStrokeWidth(labelPaintSize);
		labelPaint.setStyle(Paint.Style.STROKE);
		this.labelPaint = labelPaint;
		h = 0f;
		s = 0f;
		v = 1f;
		this.a = 255;
		huey = satx = saty = alphax = 0;
	}
	public void setOnColorChangedListener(OnColorChangedListener listener) {
		this.listener = listener;
	}
	public void setColor(int color) {
		setColor(Color.alpha(color),
				 Color.red(color),
				 Color.green(color),
				 Color.blue(color));
	}
	public void setColor(int red, int green, int blue) {
		setColor(255, red, green, blue);

	}
	public void setColor(int alpha, int red, int green, int blue) {
		a = alpha;
		float[] hsv = new float[3];
		Color.RGBToHSV(red, green, blue, hsv);
		h = hsv[0];
		s = hsv[1];
		v = hsv[2];
		a();
		invalidate();
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int width = Math.min(w, h);
		width = width / 20;
		int w17 = width * 17;
		int w18 = width * 18;
		int w19 = width * 19;
		satRect.set(width, width, w17, w17);
		hueRect.set(w18, width, w19, w17);
		alphaRect.set(width, w18, w19, w19);
		alpha.setBounds(alphaRect);
		setupHue();
		setupVal();
		a();
	}

	private void a() {
		Rect r = hueRect;
		if (r.height() == 0)
			return;
		huey = r.top + (this.h / 360f) * r.height();
		r = alphaRect;
		alphax = r.right - ((float)this.a / 255) * r.width();
		r = satRect;
		satx = r.left + s * r.width();
		saty = r.bottom - v * r.height();
		refresh(true, false);
	}
	private void setupVal() {
		Rect r = satRect;
		valShader = new LinearGradient(0, 0, 0, r.height(), 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
		satShader = new LinearGradient(0, 0, r.width(), 0, 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
		valPaint.setShader(valShader);
	}
	private void setupHue() {
		Rect r = hueRect;
		Shader s=new LinearGradient(r.left, r.top, r.left, r.bottom, hueColors, null, Shader.TileMode.CLAMP);
		huePaint.setShader(s);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthAllowed = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightAllowed =
			MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
		int w = Math.min(widthAllowed, heightAllowed);
		setMeasuredDimension(w, w);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect r = satRect;
		Paint labelPaint = this.labelPaint;
		canvas.drawRect(r,satPaint);
		canvas.drawRect(r,valPaint);
		labelPaint.setStrokeWidth(1);
		canvas.drawRect(r, labelPaint);
		int labelPaintSize = ColorPickerView.labelPaintSize;
		labelPaint.setStrokeWidth(labelPaintSize);
		float[] hsv = new float[]{h,0.5f,1 - v};
		labelPaint.setColor(Color.HSVToColor(hsv));
		canvas.drawCircle(satx, saty, labelPaintSize * 2, labelPaint);
		labelPaint.setColor(0xff000000);
		r = hueRect;
		canvas.drawRect(r, huePaint);
		canvas.drawRect(r.left - labelPaintSize, huey - labelPaintSize,
						r.right + labelPaintSize, huey + labelPaintSize, labelPaint);
		alpha.draw(canvas);
		r = alphaRect;
		canvas.drawRect(r, alphaPaint);
		canvas.drawRect(alphax - labelPaintSize, r.top - labelPaintSize,
						alphax + labelPaintSize, r.bottom + labelPaintSize, labelPaint);
	}

	private boolean touch_hue,touch_sat,touch_alpha;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (listener != null)
					listener.beforeColorChanged();
				if (hueRect.contains(x, y))
					processHue(y);
				else if (satRect.contains(x, y))
					processSat(x, y);
				else if (alphaRect.contains(x, y))
					processAlpha(x);
				break;
			case MotionEvent.ACTION_MOVE:
				if (touch_hue)
					processHue(y);
				else if (touch_sat)
					processSat(x, y);
				else if (touch_alpha)
					processAlpha(x);
				break;
			case MotionEvent.ACTION_UP:
				touch_hue = false;
				touch_sat = false;
				touch_alpha = false;
				if (listener != null)
					listener.afterColorChanged();
				return true;
			default:
				break;
		}
		refresh(false, true);
		invalidate();
		return true;
	}
	private void processAlpha(float x) {
		touch_alpha = true;
		Rect r = alphaRect;
		if (x < r.left)
			x = r.left;
		if (x > r.right)
			x = r.right;
		alphax = x;
		x = x - r.left;
		a = (int)((1 - x / r.width()) * 255);
	}
	private void processSat(float x, float y) {
		touch_sat = true;
		Rect r = satRect;
		if (y < r.top)
			y = r.top;
		if (y > r.bottom)
			y = r.bottom;
		if (x < r.left)
			x = r.left;
		if (x > r.right)
			x = r.right;
		satx = x;
		saty = y;
		x = x - r.left;
		y = y - r.top;
		s = x / r.width();
		v = 1 - y / r.height();
	}
	private void processHue(float y) {
		touch_hue = true;
		Rect r = hueRect;
		if (y < r.top)
			y = r.top;
		if (y > r.bottom)
			y = r.bottom;
		huey = y;
		y = y - r.top;
		h = (y / r.height()) * 360f;
	}

	private void refresh(boolean fore, boolean autoset) {
		float[] hsv = new float[]{h,1f,1f};
		int color;
		Rect r;
		if (fore || touch_hue) {
			color = Color.HSVToColor(hsv);
			r = satRect;
			satShader = new LinearGradient(0, 0, r.width(), 0, 0xffffffff, color, Shader.TileMode.CLAMP);
			satPaint.setShader(satShader);
		}
		hsv[1] = s;
		hsv[2] = v;
		if (fore || touch_hue || touch_sat) {
			r = alphaRect;
			color = Color.HSVToColor(hsv);
			int color2 = color & 0xffffff;
			Shader s = new LinearGradient(r.left, r.top, r.right, r.top, color, color2, Shader.TileMode.CLAMP);
			alphaPaint.setShader(s);
		}
		if (autoset && listener != null) {
			color = Color.HSVToColor(a, hsv);
			listener.onColorChanged(color);
		}
	}

	private static final int[] hueColors = new int[]{
		0xFFFF0000,//red
		0xFFFFFF00,//yellow
		0xFF00FF00,//green
		0xFF00FFFF,
		0xFF0000FF,//blue
		0xFFFF00FF,
		0xFFFF0000
	};
}
