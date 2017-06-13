package com.wezom.avatarviewexample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    AvatarView mAvatarView;
    SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAvatarView = (AvatarView)findViewById(R.id.circularView);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_default_user_big)).getBitmap();
        mAvatarView.setImageBitmap(bitmap);

        mAvatarView.setLikes("10k");
        mAvatarView.setLevel("2k");

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAvatarView.setProgressPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
