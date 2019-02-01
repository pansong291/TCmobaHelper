package com.a4455jkjh.colorpicker;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
import com.a4455jkjh.colorpicker.view.ColorPickerLayout;
import com.a4455jkjh.colorpicker.view.OnColorChangedListener;
import android.content.ClipData;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

public class ColorPickerDialog implements DialogInterface.OnClickListener {
	private final AlertDialog.Builder builder;
	private final ColorPickerLayout picker;
	private AlertDialog dialog;
	private Context c;
	private OnColorChangedListener listener;
    

	public ColorPickerDialog(Context c) {
		this.c = c;
		listener = null;
		builder = new AlertDialog.Builder(c);
		picker = new ColorPickerLayout(c);
		//picker.setColor(color);
		init();
	}

	private void init() {
		builder.setView(picker).
			setPositiveButton("确定", this).
			setNeutralButton("确定并复制", this).
			setNegativeButton("取消", this);
	}
	public ColorPickerDialog setTitle(CharSequence title) {
		builder.setTitle(title);
		return this;
	}
	public ColorPickerDialog setTitle(int title) {
		return setTitle(c.getText(title));
	}
    public ColorPickerDialog create() {
        dialog = builder.create();
        return this;
    }
	public void show(int color) {
         picker.setColor(color);
        if(dialog != null) {
		    dialog.getWindow().setSoftInputMode(2);
		    dialog.show();
        }
	}
	public void dismiss() {
		dialog.dismiss();
		c = null;
		dialog = null;
	}
    public Window getWindow() {
        return dialog.getWindow();
    }
	public ColorPickerDialog setOnColorChangedListener(OnColorChangedListener listener) {
		this.listener = listener;
		return this;
	}
	@Override
	public void onClick(DialogInterface p1, int p2) {
		switch (p2) {
			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				int color = picker.getColor();
				if (p2 == DialogInterface.BUTTON_NEUTRAL) {
					ClipboardManager clip = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setPrimaryClip(ClipData.newPlainText("color", (String.format("#%08X", color))));
					Toast.makeText(c, "复制成功", 0).show();
				}
				if (listener != null)
					listener.onColorChanged(color);
				break;
			default:
				break;
		}
		//dismiss();
	}


}
