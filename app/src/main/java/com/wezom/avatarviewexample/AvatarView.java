package com.wezom.avatarviewexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created: Zorin A.
 * Date: 01.06.2016.
 */
public class AvatarView extends RelativeLayout {
    public AvatarView(Context context) {
        super(context);
        init(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_avatar, this);
    }
}
