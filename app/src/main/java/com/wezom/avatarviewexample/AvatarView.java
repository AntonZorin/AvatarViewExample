package com.wezom.avatarviewexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created: Zorin A.
 * Date: 01.06.2016.
 */
public class AvatarView extends ImageView {
    private static final int START_ANGLE = 270;

    final Paint mBoarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint mProgressFrontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint mProgressFrontCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint mLevelsCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    TextPaint mLevelTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    TextPaint mLikeTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int mBoarderWidth;
    private int mProgressDegrees = 0;
    private int mPadding;

    private float mTextSize = 26;

    private RectF mBoundaryRect;
    private RectF mArcRect;
    private Rect mLevelTextRect;
    private Rect mLikeBitmapRect;
    private Rect mLevelBitmapRect;
    private Rect mLikeTextRect;
    private Rect mShadowBitmapRect;

    private String mLevelText = "0";
    private String mLikeText = "0";

    private Bitmap mLevelBitmap;
    private Bitmap mLikeBitmap;
    private Bitmap mAvatarBitmap;
    private Bitmap mShadowBitmap;

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(Context context) {
        super(context);
        init();
    }

    private void init() {

        //overall ops
        Resources r = getResources();
        mBoarderWidth = (int) r.getDimension(R.dimen.progress_bar_width);
        mPadding = (int) r.getDimension(R.dimen.progress_bar_padding);

        mBoarderPaint.setColor(r.getColor(R.color.avatar_border));
        mBoarderPaint.setStrokeWidth(mBoarderWidth);
        mProgressBackgroundPaint.setColor(r.getColor(R.color.avatar_progress_back));
        mProgressFrontPaint.setColor(r.getColor(R.color.avatar_progress_front));
        mProgressFrontPaint.setStrokeWidth(mBoarderWidth);
        mProgressFrontCapPaint.setColor(r.getColor(R.color.avatar_progress_front_cap));

        mLevelsCirclePaint.setColor(r.getColor(R.color.avatar_progress_front));

        //mLevelText ops
        mLevelTextPaint.setColor(r.getColor(R.color.colorWhite));
        mLevelTextPaint.setTextSize(mTextSize);
        mLevelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        calculateLevelTextRect();

        //likeText ops
        mLikeTextPaint.setColor(r.getColor(R.color.colorWhite));
        mLikeTextPaint.setTextSize(mTextSize);
        mLikeTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        calculateLikesTextRect();

        mLikeBitmap = BitmapFactory.decodeResource(r, R.drawable.bg_heart);
        mLikeBitmapRect = new Rect(0, 0, mLikeBitmap.getWidth(), mLikeBitmap.getHeight());

        mLevelBitmap = BitmapFactory.decodeResource(r, R.drawable.bg_level);
        mLevelBitmapRect = new Rect(0, 0, mLevelBitmap.getWidth(), mLevelBitmap.getHeight());

        mShadowBitmap = BitmapFactory.decodeResource(r, R.drawable.avatar_shadow);
        mShadowBitmapRect = new Rect(0, 0, mShadowBitmap.getWidth(), mShadowBitmap.getHeight());

    }

    public void setLevel(String level) {
        mLevelText = level;
        calculateLevelTextRect();
        invalidate();
    }

    public void setLikes(String likes) {
        mLikeText = likes;
        calculateLikesTextRect();
        invalidate();
    }

    public void setProgressPercent(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        if (progress < 0) {
            progress = 0;
        }

        mProgressDegrees = (int) (progress * 0.01f * 360);
        invalidate();
    }

    private void calculateLikesTextRect() {
        mLikeTextRect = new Rect();
        mLikeTextPaint.getTextBounds(mLikeText, 0, mLikeText.length(), mLikeTextRect);
    }

    private void calculateLevelTextRect() {
        mLevelTextRect = new Rect();
        mLevelTextPaint.getTextBounds(mLevelText, 0, mLevelText.length(), mLevelTextRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(width, height);
        setMeasuredDimension(minSize, minSize);

        mBoundaryRect = new RectF(0, 0, minSize, minSize);

        mArcRect = new RectF(mBoarderWidth + mPadding, mBoarderWidth + mPadding, (mBoundaryRect.right - mBoarderWidth) - mPadding, (mBoundaryRect.bottom - mBoarderWidth) - mPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //shadow
        canvas.drawBitmap(mShadowBitmap, mShadowBitmapRect, mBoundaryRect, mBitmapPaint);

        //borders
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), mBoundaryRect.centerX() - mPadding, mBoarderPaint); // 1 layer
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), (mBoundaryRect.centerX() - mBoarderWidth) - mPadding, mProgressBackgroundPaint); //2 layer
        canvas.drawArc(mArcRect, START_ANGLE, mProgressDegrees, true, mProgressFrontPaint); // ARC layer, front progress
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), mBoundaryRect.centerX() - mBoarderWidth * 3 - mPadding, mBoarderPaint); // 3 layer
        //cap
        canvas.drawCircle(mBoundaryRect.centerX(), mBoundaryRect.centerY(), mBoundaryRect.centerX() - mBoarderWidth * 4 - mPadding, mProgressFrontCapPaint); // 4 layer

        //draw avatar
        handleAvatarImage();
        if (mAvatarBitmap != null) {
            canvas.drawBitmap(mAvatarBitmap, mBoundaryRect.centerX() - mAvatarBitmap.getWidth() * 0.5f,
                    mBoundaryRect.centerY() - mAvatarBitmap.getHeight() * 0.5f, null);
        }

        //level circle background if needed
       /* canvas.save();
        canvas.rotate(mProgressDegrees, mBoundaryRect.centerX(), mBoundaryRect.centerY()); //rotate center of canvas
        canvas.drawCircle(mBoundaryRect.centerX(), mPadding - mLevelTextRect.exactCenterY(), mLevelTextRect.width() * 0.6f, mLevelsCirclePaint); //draw levels circle
        canvas.restore();*/

        //level text background
        canvas.save();
        canvas.rotate(mProgressDegrees, mBoundaryRect.centerX(), mBoundaryRect.centerY());
        canvas.rotate(-mProgressDegrees, mBoundaryRect.centerX(), mPadding - mLevelTextRect.centerY());
        canvas.drawBitmap(mLevelBitmap, mBoundaryRect.centerX() - mLevelBitmapRect.centerX(), mLevelBitmapRect.centerY() + mLevelTextRect.centerY() / 2, mBitmapPaint);
        canvas.restore();


        //rotation for text
        float magicMargin = 1.3f;
        canvas.save();
        canvas.rotate(mProgressDegrees, mBoundaryRect.centerX(), mBoundaryRect.centerY());
        canvas.rotate(-mProgressDegrees, mBoundaryRect.centerX(), mPadding - mLevelTextRect.centerY());
        canvas.drawText(mLevelText, mBoundaryRect.centerX() - mLevelTextRect.centerX(), mPadding * magicMargin - mLevelTextRect.centerY(), mLevelTextPaint); //magic margin is a miracle float!!!
        canvas.restore();

        //heart rotation
        canvas.save();
        canvas.rotate(mProgressDegrees + 180, mBoundaryRect.centerX(), mBoundaryRect.centerY()); //rotate center of canvas to mirror levels indicator
        canvas.rotate(-mProgressDegrees + 180, mBoundaryRect.centerX(), mLikeBitmapRect.centerY());
        canvas.drawBitmap(mLikeBitmap, mBoundaryRect.centerX() - mLikeBitmapRect.centerX(), 0, mBitmapPaint);
        canvas.restore();

        //like text rotation
        canvas.save();
        canvas.rotate(mProgressDegrees + 180, mBoundaryRect.centerX(), mBoundaryRect.centerY());
        canvas.rotate(-mProgressDegrees + 180, mBoundaryRect.centerX(), mPadding - (mLikeTextRect.centerY()));
        canvas.drawText(mLikeText, mBoundaryRect.centerX() - mLikeTextRect.centerX(), mPadding - mLikeTextRect.centerY(), mLikeTextPaint);
        canvas.restore();
    }

    private void handleAvatarImage() {
        Drawable drawable = getDrawable();
        int radius = ((int) mBoundaryRect.centerX() - mBoarderWidth * 4 - mPadding) * 2;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = Bitmap.createScaledBitmap(b.copy(Bitmap.Config.ARGB_8888, true), radius, radius, true);
        mAvatarBitmap = getRoundedCroppedBitmap(bitmap, radius);
    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            finalBitmap = bitmap;
        }
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        paint.setFilterBitmap(true);
        canvas.drawCircle(finalBitmap.getWidth() * 0.5f, finalBitmap.getHeight() * 0.5f, finalBitmap.getWidth() * 0.5f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }
}
