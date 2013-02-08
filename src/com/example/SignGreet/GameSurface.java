package com.example.SignGreet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback
{
    private final GameThread gameThread;
    private Spooky spooky;

    private PlayersElements playersElements;

    private int x = 0;
    private int y = 50;

    public GameSurface(Context context)
    {
        super(context);

        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this);
        setFocusable(true);

        playersElements = new PlayersElements();
        setOnTouchListener(playersElements);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        boolean retry = true;
        while (retry)
        {
            try
            {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e)
            {
                // try again shutting down the thread
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.BLACK);
        updateSpooky(canvas);

    }

    private void updateSpooky(Canvas canvas)
    {
        if (spooky == null)
        {
            spooky = new Spooky(getWidth(), getHeight(), this);
        }

        spooky.updateSpooky(canvas);
        playersElements.drawUserBeam(canvas);
        spooky.drawEnergy(canvas);
//        spooky.updateBrownian(canvas, x, y);
        playersElements.drawPlayersEnergy(canvas);
    }
}
