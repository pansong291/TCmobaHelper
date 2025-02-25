package com.a4455jkjh.colorpicker.view;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ColorPickerLayout extends ViewGroup implements OnColorChangedListener {
	private final ColorPickerView picker;
	private final TestView testView;
	private final LinearLayout hintLayout;
	private final EditText hint;
	private int color;
	
	public ColorPickerLayout(Context c) {
		this(c, null);
	}
	public ColorPickerLayout(Context c, AttributeSet a) {
		super(c, a);
		picker = new ColorPickerView(c, a);
		testView = new TestView(c, a);
		hintLayout = new LinearLayout(c, a);
		hint = new EditText(c, a);
		hintLayout.setOrientation(LinearLayout.VERTICAL);
		hint.setGravity(Gravity.CENTER);
		hint.setFilters(filters);
		ColorUtils u = new ColorUtils(this);
		hint.addTextChangedListener(u);
		hint.setKeyListener(u);
		hintLayout.addView(hint);
		picker.setOnColorChangedListener(this);
		addView(picker);
		addView(testView);
		addView(hintLayout);
        
	}
	@Override
	protected void onLayout(boolean p1, int p2, int p3, int p4, int p5) {
		View v = picker;
		int w = v.getMeasuredWidth();
		int h = v.getMeasuredHeight();
		v.layout(p2, p3, p2 + w, p3 + h);
        // 竖屏
        if(getHeight() >= getWidth()) {
		    v = hintLayout;
		    int hinth = v.getMeasuredHeight();
		    v.layout(p2 + w / 2, p3 + h, p2 + w, p3 + h + hinth);
		    v = testView;
		    v.layout(p2, p3 + h, p2 + w / 2, p3 + h + hinth);
        } else {            
            v = hintLayout;
            int hinth = v.getMeasuredHeight();
            int hintw = v.getMeasuredWidth();
            v.layout(p2 + w, p3 + h / 2, p2 + w + hintw, p3 + h / 2 + hinth);
            v = testView;
            v.layout(p2 + w, p3 + h / 2 - hinth, p2 + w + hintw, p3 + h / 2);
        }
	}
	public void setColor(int color) {
		picker.setColor(color);
		testView.setInitColor(color);
		onColorChanged(color);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View v = picker;
		v.measure(widthMeasureSpec, heightMeasureSpec);
		int w = v.getMeasuredWidth();
		int h = v.getMeasuredHeight();
		int widthAllowed =
            MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightAllowed =
			MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
        // 竖屏
        if(heightAllowed >= widthAllowed) {
		    int hint_h = heightAllowed - h;
            int wc = MeasureSpec.makeMeasureSpec(w / 2, MeasureSpec.EXACTLY);
            int hc = MeasureSpec.makeMeasureSpec(hint_h, MeasureSpec.AT_MOST);
	    	v = hintLayout;
		    v.measure(wc, hc);
		    h = h + v.getMeasuredHeight();
        } else {
            int hint_w = widthAllowed - w;
            int wc = MeasureSpec.makeMeasureSpec(hint_w, MeasureSpec.AT_MOST);
            int hc = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            v = hintLayout;
            v.measure(wc, hc);
            w += v.getMeasuredWidth();
        }
        setMeasuredDimension(w, h);
	}
	private boolean autoset = false;

	@Override
	public void beforeColorChanged() {
		autoset=true;
	}

	@Override
	public void afterColorChanged() {
		autoset=false;
	}
	
	void findColor(String text) {
		if (autoset) {
			return;
		}
		int color = Color.parseColor(text);
		picker.setColor(color);
		testView.setColor(color);
		this.color=color;
	}
	@Override
	public void onColorChanged(int color) {
		this.color=color;
		testView.setColor(color);
		hint.setText(String.format("#%08X", color));
	}
	public int getColor(){
		return color;
	}
	private static final InputFilter[]  filters;
	static{
		filters = new InputFilter[]{
			new InputFilter.LengthFilter(9)
		};
	}
}

