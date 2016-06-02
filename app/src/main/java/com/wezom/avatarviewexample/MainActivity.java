package com.wezom.avatarviewexample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    AvatarView mAvatarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAvatarView = (AvatarView)findViewById(R.id.circularView);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_default_user_big)).getBitmap();
        mAvatarView.setImageBitmap(bitmap);

        mAvatarView.setLikes("10k");
        mAvatarView.setLevel("2k");
        mAvatarView.setProgressPercent(40);
    }
}
