package com.example.SignGreet;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;

public class CustomDrawableView extends ShapeDrawable
{
    private Paint paint;

    public CustomDrawableView()
    {
        this.setBounds(200, 200, 300, 300);
    }

    @Override
    public void draw(Canvas canvas)
    {

        paint = new Paint();
        paint.setColor(0xff74AC23);
        int height = getBounds().height();
        int width = getBounds().width();
        RectF rect = new RectF(0.0f, 0.0f, width, height);
        canvas.drawRoundRect(rect, 30, 30, paint);

    }

    @Override
    public void setAlpha(int i)
    {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {

    }

    @Override
    public int getOpacity()
    {
        return 0;
    }
}
