package com.example.SignGreet;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The main view responsible for drawing the display, handling
 * the finger press events and creating the background thread.
 */
public class ThreadingView2 extends View
        implements Runnable
{
    private final Paint mPaint = new Paint();
    private List<CirclePoint> mCounterPositions = new ArrayList<CirclePoint>();
    private static final int MSG_UPDATE = 1;
    private Paint _paintSimple;
    private Paint _paintBlur;
    private int touchCircleRadius = 80;
    private Enemy enemy;
    private Drawable shape;
    private Paint paintEnergy;
    private float playersEnergy = 100;
    private SoundEffects sfx2;
    private SoundTrackFX sfx;
    private Paint mCirclePaint;
    private int mTicker;

    public ThreadingView2(Context context)
    {
        super(context);
        setFocusable(true);

        sfx = new SoundTrackFX(context);
        sfx2 = new SoundEffects(context);

        mPaint.setTextSize(16);
        mPaint.setTextAlign(Paint.Align.CENTER);

        enemy = new Enemy(context);
        enemy.drawable.setBounds(200, 200, 300, 300);

        setLinePaint();
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

    /**
     * Draw the display
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
//            counterDraw(canvas);
        enemy.drawable.draw(canvas);
//            initPoints();
        multiDraw(canvas);
        drawEnergyEnemy(canvas);
        drawPlayersEnergy(canvas);


        enemy.update(getWidth(),getHeight());
        enemy.drawable.invalidateSelf();

    }

    private void initPoints()
    {
        Point point1 = new Point((getWidth()/2), touchCircleRadius);
        CirclePoint circle1 = new CirclePoint(point1);
        mCounterPositions.add(circle1);

        Point point2 = new Point((getWidth()/2),getHeight()-touchCircleRadius);
        CirclePoint circle2 = new CirclePoint(point2);
        mCounterPositions.add(circle2);
    }

    private void drawPlayersEnergy(Canvas canvas)
    {
        if(paintEnergy == null)
        {
            paintEnergy = new Paint();
        }

        int width = getWidth();//100%
        int fullBar = 100;//Max Energy
        int percentage = width/fullBar;//1%

        int remaining = (int) (playersEnergy *percentage);
        int full = fullBar*percentage;

        paintEnergy.setStrokeWidth(3);
        paintEnergy.setStyle(Paint.Style.STROKE);
        paintEnergy.setColor(Color.RED);
        canvas.drawRect(33, 33+60+10, full, 60+10+33+60, paintEnergy);
        paintEnergy.setStyle(Paint.Style.FILL);
        paintEnergy.setColor(Color.GREEN);
        canvas.drawRect(33, 33+60+10, remaining, 60+10+33+60, paintEnergy);
    }

    private void drawEnergyEnemy(Canvas canvas)
    {
        if(paintEnergy == null)
        {
            paintEnergy = new Paint();
        }

        int width = getWidth();//100%
        int fullBar = 100;//Max Energy
        int percentage = width/fullBar;//1%

        int remaining = (int) (enemy.energy*percentage);
        int full = fullBar*percentage;

        paintEnergy.setStrokeWidth(3);
        paintEnergy.setStyle(Paint.Style.STROKE);
        paintEnergy.setColor(Color.RED);
        canvas.drawRect(33, 33, full, 33+60, paintEnergy);
        paintEnergy.setStyle(Paint.Style.FILL);
        paintEnergy.setColor(Color.GREEN);
        canvas.drawRect(33, 33, remaining, 33+60, paintEnergy);
    }

    private void multiDraw(Canvas canvas)
    {
        if (mCounterPositions.size() > 1)
        {
            Path path = drawLineBetweenCircles(mCounterPositions.get(0), mCounterPositions.get(1));

            drawTouchCircle(canvas, mCounterPositions.get(0));
            drawTouchCircle(canvas, mCounterPositions.get(1));

            canvas.drawPath(path, _paintBlur);
            canvas.drawPath(path, _paintSimple);

            detectEnemyHit(path);

        }
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

        calculateCharge(x1, y1, x2, y2);

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

    private void calculateCharge(float x1, float y1, float x2, float y2)
    {
        float xDist = x1 - x2;
        float yDist = y1 - y2;

        if ((xDist < 100 && xDist > -100) || (yDist < 100 && yDist > -100))
        {
            if(playersEnergy < 100)
            {
                playersEnergy +=0.5;
            }
            _paintBlur.setColor(Color.argb(235, 0, 255, 0));
        }
        else if(playersEnergy < 0)
        {
            _paintBlur.setColor(Color.argb(235, 255, 0, 0));
        }
        else
        {
            _paintBlur.setColor(Color.argb(235, 74, 138, 255));
            if(playersEnergy > 0)
            {
                playersEnergy -=0.2;
            }
        }
    }

    private void detectEnemyHit(Path path)
    {
        Region clip = new Region(0, 0, getWidth(), getHeight());

        Region region1 = new Region();
        region1.setPath(path, clip);

        if (region1.getBounds().contains(enemy.drawable.getBounds()))
        {
            if (enemy.energy <= 0)
            {
                Log.d("Collide", "Yes colliding");
                Random r = new Random();

                int xEn = r.nextInt(getWidth() - 400) + 200;
                int yEn = r.nextInt(getHeight() - 400) + 200;

                Log.d("Collision", "left::" + xEn +
                        " top::" + yEn);

                int width = 100;
                int right = xEn + width;
                int bottom = yEn + width;
                enemy.resetSpooky(new Rect(xEn, yEn, right, bottom));
                enemy.energy = 100;

            }
            else
            {
                //Log.d("Energy", "Enemy::" + enemy.energy);

                if(enemy.energy%30 == 0)
                {
                    enemy.setHitColor();
                    sfx2.hit();
                }
                //Log.d("Energy", "Player::" + playersEnergy);
                if(playersEnergy > 0)
                {
                    enemy.energy -= 0.5;

                }
            }
        }
        else {
            enemy.drawable.clearColorFilter();
        }
    }

    private void drawTouchCircle(Canvas canvas, CirclePoint circle)
    {
        circle.ticker++;

        int x = circle.point.x;
        int y = circle.point.y;

        Rect bounds = new Rect();
        bounds.set(x - touchCircleRadius, y - touchCircleRadius, x + touchCircleRadius, y + touchCircleRadius);

        if(mCirclePaint == null)
            mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.argb(127, 255, 255, 255));

        canvas.drawCircle(x, y, touchCircleRadius, mCirclePaint);
    }


    private int linearInterpolate(float y1, float y2, double mu)
    {
        return (int) (y1 * (1 - mu) + y2 * mu);
    }

    private void counterDraw(Canvas canvas)
    {
        // Don't try to draw anything if there are no items yet
        if (mCounterPositions.size() > 0)
        {
            Path path = new Path();
            path.moveTo(mCounterPositions.get(0).point.x, mCounterPositions.get(0).point.y);

            int counter = 0;
            for (CirclePoint circle : mCounterPositions)
            {
                if (circle.ticker > 100)
                {
                    mCounterPositions.remove(circle);
                    break;
                }

                circle.ticker++;

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                mPaint.setAntiAlias(true);
                mPaint.setColor(Color.YELLOW);
                canvas.drawCircle(circle.point.x, circle.point.y, circle.ticker, mPaint);

                // Draw the value inside the circle
                mPaint.setColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(0);
                String count = "" + ((counter++) + circle.ticker);
                canvas.drawText(count, circle.point.x, circle.point.y + 6, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                if (counter > 0)
                {
                    path.lineTo(circle.point.x, circle.point.y);
                    canvas.drawPath(path, mPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
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


    private void captureUp(MotionEvent event)
    {
        sfx.stopBeam();
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
        if(mCounterPositions.size() == 2)
        {
            sfx.playBeam();
        }
    }

    /**
     * Handle the updateSpooky message from the thread.
     * Invoke the main display updateSpooky each time
     * one is received.
     */
    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.arg1)
            {
                case MSG_UPDATE:
                {
                    invalidate();
                    break;
                }
            }
        }
    };

    /**
     * Create the background thread to updateSpooky the main
     * ticker value at the same time as the main programs
     * thread is running.
     */
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                Thread.sleep(50);
            } catch (Exception e)
            {
            }

            // Update the ticker
            mTicker++;

            // Tell the main app it needs updating
            if (mCounterPositions.size() > 0)
            {
                Message msg = Message.obtain();
                msg.arg1 = MSG_UPDATE;
                handler.sendMessageDelayed(msg, MSG_UPDATE);
            }
        }
    }
}
