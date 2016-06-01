package com.wezom.avatarviewexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created: Zorin A.
 * Date: 01.06.2016.
 */
public class CircularProgressView extends ImageView {
    private static final int START_ANGLE = 270;

    Paint mBoarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mProgressFrontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mProgressFrontCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mLevelsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int mBoarderWidth;
    private int mProgressDegrees = 90;
    private float mTextSize = 26;

    private int mPadding;
    private RectF mBoundaryRect;
    private RectF mArcRect;
    private RectF mLevelsOvalRect;
    private Rect mTextRect;

    private String mText = "25k";

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Resources r = getResources();
        mBoarderWidth = (int) r.getDimension(R.dimen.progress_bar_width);
        mPadding = (int) r.getDimension(R.dimen.progress_bar_padding);

        mBoarderPaint.setColor(r.getColor(R.color.avatar_border));
        mBoarderPaint.setStrokeWidth(mBoarderWidth);
        mProgressBackgroundPaint.setColor(r.getColor(R.color.avatar_progress_back));
        mProgressFrontPaint.setColor(r.getColor(R.color.avatar_progress_front));
        mProgressFrontPaint.setStrokeWidth(mBoarderWidth);
        mProgressFrontCapPaint.setColor(r.getColor(R.color.avatar_progress_front_cap));

        //mText ops
        mTextPaint.setColor(r.getColor(R.color.colorWhite));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mTextRect = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

        mLevelsPaint.setColor(r.getColor(R.color.avatar_progress_front));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mBoundaryRect = new RectF(0, 0, width, height);
        mArcRect = new RectF(mBoarderWidth + mPadding, mBoarderWidth + mPadding,
                (mBoundaryRect.right - mBoarderWidth) - mPadding,
                (mBoundaryRect.bottom - mBoarderWidth) - mPadding);
        int textLenght = (int) (mTextPaint.measureText(mText));

        mLevelsOvalRect = new RectF(mBoundaryRect.centerX() - textLenght + mBoarderWidth, mBoarderWidth * 2, mBoundaryRect.centerX() + textLenght - mBoarderWidth, mPadding * 2.5f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), mBoundaryRect.centerX() - mPadding, mBoarderPaint); // 1 layer
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), (mBoundaryRect.centerX() - mBoarderWidth) - mPadding, mProgressBackgroundPaint); //2 layer
        canvas.drawArc(mArcRect, START_ANGLE, mProgressDegrees, true, mProgressFrontPaint); // ARC layer, front progress
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), (mBoundaryRect.centerX() - mBoarderWidth * 3) - mPadding, mBoarderPaint); // 3 layer
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), (mBoundaryRect.centerX() - mBoarderWidth * 4) - mPadding, mProgressFrontCapPaint); // 4 layer

        canvas.save();
        canvas.rotate(mProgressDegrees, mBoundaryRect.centerX(), mBoundaryRect.centerY()); //rotate center of canvas
        canvas.rotate(-mProgressDegrees, mBoundaryRect.centerX() - mTextRect.centerX()+mPadding, mTextRect.height()+mPadding); //rotate where levels is

        canvas.drawOval(mLevelsOvalRect, mLevelsPaint);
        canvas.drawText(mText, mBoundaryRect.centerX() - mTextRect.centerX(), mTextRect.height()+mPadding, mTextPaint);

        canvas.restore();
    }
}
