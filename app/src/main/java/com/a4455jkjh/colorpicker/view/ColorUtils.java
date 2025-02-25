package com.a4455jkjh.colorpicker.view;
import android.text.TextWatcher;
import android.text.Editable;
import android.graphics.Color;
import android.text.method.NumberKeyListener;
import android.text.InputType;
import android.view.View;
import android.view.KeyEvent;

public class ColorUtils extends NumberKeyListener implements TextWatcher {
	private final ColorPickerLayout picker;
	private final String chars = "0123456789abcdefABCDEF#";

	public ColorUtils(ColorPickerLayout picker) {
		this.picker = picker;
	}
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
		// Empty
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
		// Empty
	}

	@Override
	public void afterTextChanged(Editable p1) {
		String text = p1.toString().toLowerCase();
		int len = text.length();
		if (!text.startsWith("#")) {
			p1.insert(0, "#");
			return;
		}
		if (len == 7 || len == 9) 
			picker.findColor(text);
	}

	@Override
	protected char[] getAcceptedChars() {
		return chars.toCharArray();
	}

	@Override
	public int getInputType() {
		return InputType.TYPE_CLASS_NUMBER;
	}




}
