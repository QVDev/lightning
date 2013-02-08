package com.example.SignGreet;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

public class AnimatedView
{
    private Context mContext;
    int x = -1;
    int y = -1;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private Handler h;
    private final int FRAME_RATE = 30;

    public BitmapDrawable ball;

    public float energy;

    private int width;
    private int height;

    public Canvas canvas;

    public AnimatedView(Context context, int width, int height)
    {
        mContext = context;
        h = new Handler();

        this.ball = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.spooky);

        this.width = width;
        this.height = height;

        r.run();
    }

    private Runnable r = new Runnable()
    {
        @Override
        public void run()
        {
            update();
        }
    };

    protected void update()
    {
        if (x < 0 && y < 0)
        {
            x = width / 2;
            y = height / 2;
        } else
        {
            x += xVelocity;
            y += yVelocity;
            if ((x > width - ball.getBitmap().getWidth()) || (x < 0))
            {
                xVelocity = xVelocity * -1;
            }
            if ((y > height - ball.getBitmap().getHeight()) || (y < 0))
            {
                yVelocity = yVelocity * -1;
            }
        }
//        c.drawBitmap(ball.getBitmap(), x, y, null);

//        if(canvas != null)
//            ball.draw(canvas);

        h.postDelayed(r, FRAME_RATE);
    }
}
