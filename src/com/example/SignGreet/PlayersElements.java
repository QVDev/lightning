package com.example.SignGreet;

import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PlayersElements implements View.OnTouchListener
{
    private List<CirclePoint> mCounterPositions = new ArrayList<CirclePoint>();
    private Paint _paintSimple;
    private Paint _paintBlur;
    private Paint mCirclePaint;
    private Paint paintEnergyStroke;
    private Paint paintEnergyFill;

    private int touchCircleRadius = 80;

    PlayersElements()
    {
        setLinePaint();
        setEnergyPaintStroke();
        setEnergyPaintFill();
    }

    private void setLinePaint()
    {
        _paintSimple = new Paint();
        _paintSimple.setAntiAlias(true);
        _paintSimple.setDither(true);
        _paintSimple.setColor(Color.argb(248, 167, 27, 130));
        _paintSimple.setStrokeWidth(20f);
        _paintSimple.setStyle(Paint.Style.STROKE);
        _paintSimple.setStrokeJoin(Paint.Join.ROUND);
        _paintSimple.setStrokeCap(Paint.Cap.ROUND);

        _paintBlur = new Paint();
        _paintBlur.set(_paintSimple);
        _paintBlur.setColor(Color.argb(235, 74, 138, 255));
        _paintBlur.setStrokeWidth(30f);
        _paintBlur.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
    }

    private void setEnergyPaintStroke()
    {
        paintEnergyStroke = new Paint();
        paintEnergyStroke.setStrokeWidth(3);
        paintEnergyStroke.setStyle(Paint.Style.STROKE);
        paintEnergyStroke.setColor(Color.RED);
    }

    private void setEnergyPaintFill()
    {
        paintEnergyFill = new Paint();
        paintEnergyFill.setStrokeWidth(3);
        paintEnergyFill.setStyle(Paint.Style.FILL);
        paintEnergyFill.setColor(Color.GREEN);
    }

    public void drawUserBeam(Canvas canvas)
    {
        if (mCounterPositions.size() > 1)
        {
            Path path = drawLineBetweenCircles(mCounterPositions.get(0), mCounterPositions.get(1));

            drawTouchCircle(canvas, mCounterPositions.get(0));
            drawTouchCircle(canvas, mCounterPositions.get(1));

            canvas.drawPath(path, _paintBlur);
            canvas.drawPath(path, _paintSimple);
        }
    }

    public void drawPlayersEnergy(Canvas canvas)
    {
        int width = canvas.getWidth();//100%
        int fullBar = 100;//Max Energy
        int percentage = width / fullBar;//1%

        int remaining = (int) (100 * percentage);
        int full = fullBar * percentage;

        canvas.drawRect(33, 33 + 60 + 10, full, 60 + 10 + 33 + 60, paintEnergyStroke);
        canvas.drawRect(33, 33 + 60 + 10, remaining, 60 + 10 + 33 + 60, paintEnergyFill);
    }

    private Path drawLineBetweenCircles(CirclePoint point1, CirclePoint point2)
    {
        //Point one
        float x1 = point1.point.x;
        float y1 = point1.point.y;

        //Point two
        float x2 = point2.point.x;
        float y2 = point2.point.y;

        //Path
        Path path = new Path();

        //set first point
        path.moveTo(x1, y1);

//        calculateCharge(x1, y1, x2, y2);

        double end = 0;

        //Extra stop for lightning effect
        while (end < 1.0)
        {
            double changeX = (Math.random() * 0.1 + 0.0);
            double changeY = (Math.random() * 0.1 + 0.0);

            x1 = linearInterpolate(x1, x2, changeX);
            y1 = linearInterpolate(y1, y2, changeY);

            path.lineTo(x1, y1);

            end += 0.01;
        }

        path.setLastPoint(x2, y2);

        return path;
    }

    private int linearInterpolate(float y1, float y2, double mu)
    {
        return (int) (y1 * (1 - mu) + y2 * mu);
    }

    private void drawTouchCircle(Canvas canvas, CirclePoint circle)
    {
        circle.ticker++;

        int x = circle.point.x;
        int y = circle.point.y;

        Rect bounds = new Rect();
        bounds.set(x - touchCircleRadius, y - touchCircleRadius, x + touchCircleRadius, y + touchCircleRadius);

        if (mCirclePaint == null)
            mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.argb(127, 255, 255, 255));

        canvas.drawCircle(x, y, touchCircleRadius, mCirclePaint);
    }

    /*
    Touch events
     */
    @Override
    public boolean onTouch(View view, MotionEvent event)
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
        return true;
    }

    private void captureUp(MotionEvent event)
    {
//        sfx.stopBeam();
        try
        {
            int index = event.getActionIndex();
            mCounterPositions.remove(index);

        } catch (IndexOutOfBoundsException e)
        {
        }

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
                mCounterPositions.get(id).point.x = (int) event.getX(i);
                mCounterPositions.get(id).point.y = (int) event.getY(i);

            } catch (IndexOutOfBoundsException e)
            {
            }
        }
    }

    private void captureDown(MotionEvent event)
    {
        CirclePoint circlePoint = new CirclePoint(new Point((int) event.getX(), (int) event.getY()));
        // Add the position
        if (mCounterPositions.size() < 2)
        {
            mCounterPositions.add(circlePoint);
        }
        if (mCounterPositions.size() == 2)
        {
//            sfx.playBeam();
        }
    }
}
