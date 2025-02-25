package com.a4455jkjh.colorpicker.view;

public interface OnColorChangedListener {
	void beforeColorChanged();
	void onColorChanged(int color);
	void afterColorChanged();
}
