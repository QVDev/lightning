package com.example.SignGreet;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
    private static final String TAG = GameThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private GameSurface gameSurface;
    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public GameThread(SurfaceHolder surfaceHolder, GameSurface gameSurface) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameSurface = gameSurface;
    }

    @Override
    public void run() {
        long tickCount = 0L;
        Log.d(TAG, "Starting game loop");
        while (running) {
            tickCount++;
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    gameSurface.onDraw(canvas);

                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        Log.d(TAG, "Game loop executed " + tickCount + " times");
    }
}
