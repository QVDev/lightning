package com.example.SignGreet;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Iterator;

public class MultiTouchView extends View implements View.OnTouchListener
{
    int animationRadius = 80; // Size of finger indicator
    private Drawable shape;

    HashMap<Integer, PointF> touchPoints = new HashMap<Integer, PointF>();
    private Paint _paintSimple;
    private Paint _paintBlur;

    protected Drawable getShape()
    {
        if (shape == null)
        {
            shape = getContext().getResources().getDrawable(
                    R.drawable.ic_launcher);
        }
        return shape;
    }

    public MultiTouchView(Context context)
    {
        super(context);
        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        int action = event.getActionMasked();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                captureDown(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                captureUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                capturePointerMoves(event);
                break;
        }
        invalidate();
        return true;
    }

    private void captureDown(MotionEvent event)
    {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        touchPoints.put(id, new PointF(event.getX(index), event.getY(index)));
    }

    private void captureUp(MotionEvent event)
    {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);
        touchPoints.remove(id);
    }

    private void capturePointerMoves(MotionEvent event)
    {
        int length = event.getPointerCount();
        int id;
        for (int i = 0; i < length; i++)
        {
            id = event.getPointerId(i);
            try
            {
                touchPoints.get(id).set(event.getX(i), event.getY(i));
            } catch (IndexOutOfBoundsException e)
            {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        if (touchPoints.size() > 0)
        {
            int radius = 80; // Size of finger indicator

            float x1 = 0;
            float y1 = 0;
            Path path = new Path();
            if (touchPoints.get(0) != null)
            {
                path.moveTo(touchPoints.get(0).x, touchPoints.get(0).y);
                x1 = touchPoints.get(0).x;
                y1 = touchPoints.get(0).y;
            }

            int index = 0;

            Iterator<Integer> iterator = touchPoints.keySet().iterator();
            while (iterator.hasNext())
            {
//                if(animationRadius == 80)
//                {
//                    animationRadius = 3;
//                }
//                animationRadius++;

                int key = iterator.next();

                PointF point = touchPoints.get(key);
                int x = (int) point.x;
                int y = (int) point.y;

                Rect bounds = new Rect();
                bounds.set(x - radius, y - radius, x + radius, y + radius);

                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(48);
                canvas.drawText(String.valueOf(key), x - 12, y + 12, paint);

                canvas.drawCircle(x, y, animationRadius, paint);

//                paint.setStyle(Paint.Style.STROKE);
//                paint.setStrokeWidth(3.0f);

                if (_paintSimple == null)
                {
                    _paintSimple = new Paint();
                    _paintSimple.setAntiAlias(true);
                    _paintSimple.setDither(true);
                    _paintSimple.setColor(Color.argb(248, 255, 255, 255));
                    _paintSimple.setStrokeWidth(20f);
                    _paintSimple.setStyle(Paint.Style.STROKE);
                    _paintSimple.setStrokeJoin(Paint.Join.ROUND);
                    _paintSimple.setStrokeCap(Paint.Cap.ROUND);
                }

                if (_paintBlur == null)
                {
                    _paintBlur = new Paint();
                    _paintBlur.set(_paintSimple);
                    _paintBlur.setColor(Color.argb(235, 74, 138, 255));
                    _paintBlur.setStrokeWidth(30f);
                    _paintBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
                }


                if (index > 0)
                {
                    float ypath = y1;
                    float xpath = x1;

                    float xDist = x1 - point.x;
                    float yDist = y1 - point.y;

                    if ((xDist < 100 && xDist > -100) || (yDist < 100 && yDist > -100))
                    {
                        _paintBlur.setColor(Color.argb(235, 255, 0, 0));
                    }
                    else
                    {
                        _paintBlur.setColor(Color.argb(235, 74, 138, 255));
                    }

                        double end = 0;

                    while (end < 1.0)
                    {
                        double changex = (Math.random() * 0.1 + 0.0);
                        double changey = (Math.random() * 0.1 + 0.0);

                        xpath = linearInterpolate(xpath, point.x, changex);
                        ypath = linearInterpolate(ypath, point.y, changey);

//                        Log.d("Dist", "changex::" + xpath + " changey::" + changey);

                        path.lineTo(xpath, ypath);

                        end += 0.01;
                    }


                    path.setLastPoint(point.x, point.y);
                    canvas.drawPath(path, _paintBlur);
                    canvas.drawPath(path, _paintSimple);

                    canvas.drawCircle(x, y, animationRadius, paint);

                    Drawable shape = getShape();
                    shape.setBounds(200, 200, 300, 300);
                    shape.draw(canvas);

                    Region clip = new Region(0, 0, getWidth(), getHeight());

                    Region region1 = new Region();
                    region1.setPath(path, clip);

                    if (region1.getBounds().contains(shape.getBounds()))
                    {
                        Log.d("Collide", "Yes colliding");
                    }

                }

                index++;
            }

        }


        super.onDraw(canvas);
    }

    private int linearInterpolate(float y1, float y2, double mu)
    {
        return (int) (y1 * (1 - mu) + y2 * mu);
    }


}