package com.example.SignGreet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;

import java.util.Random;

public class Enemy
{
    private static final long RETURN_COLOR = 600;
    public Drawable drawable;
    public int energy;
    private long mInterpolateTime;
    private MotionEvent.PointerCoords mImageTarget;
    private int COLOR2;
    private final Context mContext;
    private boolean isHit;

    private Handler h;

    public Enemy(Context context)
    {
        energy = 100;
        mContext = context;
        mImageTarget = new MotionEvent.PointerCoords();
        drawable = context.getResources().getDrawable(
                R.drawable.spooky);

        h = new Handler();
    }

    private Runnable r = new Runnable()
    {
        @Override
        public void run()
        {
            isHit = false;
            drawable.clearColorFilter();
        }
    };

    public void update(int width, int height)
    {
        final long INTERPOLATION_LENGTH = 1000;
        long time = SystemClock.uptimeMillis();
        if (time - mInterpolateTime > INTERPOLATION_LENGTH)
        {

            Random r = new Random();
            int xEn = r.nextInt(width - 400) + 200;
            int yEn = r.nextInt(height - 400) + 200;

            mImageTarget.x = xEn;//(float)(Math.random() * 100);
            mImageTarget.y = yEn;//(float)(Math.random() * 100);
            mInterpolateTime = time;
        }

        float t = (float) (time - mInterpolateTime) / INTERPOLATION_LENGTH;
        // For some smoothness uncomment this line;
        t = t * t * (3 - 2 * t);
        int newX = (int) (drawable.getBounds().left + (mImageTarget.x - drawable.getBounds().left) * t);
        int newY = (int) (drawable.getBounds().top + (mImageTarget.x - drawable.getBounds().top) * t);

        drawable.setBounds(newX, newY, newX + 100, newY + 100);
    }

    public void resetSpooky(Rect bounds)
    {
        drawable.setBounds(bounds);
    }

    public void setHitColor()
    {
        if (!isHit)
        {
            isHit = true;

            COLOR2 = Color.parseColor("#FF" + "453333");
            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
            drawable.setColorFilter(COLOR2, mMode);

            h.postDelayed(r, RETURN_COLOR);
        }
    }
}
