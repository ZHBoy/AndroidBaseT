package com.ychd.ycwwz.base_library.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 监听粘贴的EditText
 */
public class PasteTwoEditText extends androidx.appcompat.widget.AppCompatEditText {

    private OnPasteTwoCallback mOnPasteTwoCallback;

    public interface OnPasteTwoCallback {
        void onPasteTwo();
    }

    public PasteTwoEditText(Context context) {
        super(context);
    }

    public PasteTwoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        super.onTextContextMenuItem(id);
        switch (id) {
            case android.R.id.cut:
                // 剪切
                break;
            case android.R.id.copy:
                // 复制
                break;
            case android.R.id.paste:
                // 粘贴
                if (mOnPasteTwoCallback != null) {
                    mOnPasteTwoCallback.onPasteTwo();
                }
        }
        return true;
    }

    public void setOnPasteTwoCallback(OnPasteTwoCallback onPasteTwoCallback) {
        mOnPasteTwoCallback = onPasteTwoCallback;
    }
}