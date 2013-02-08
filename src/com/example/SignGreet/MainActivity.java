package com.example.SignGreet;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
    private MultiTouchView dView;
    private ThreadingView2 mThreadingView;
    private AnimatedView mAnimatedView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        dView = new MultiTouchView(this);
//
//        setContentView(dView);

        GameSurface gameSurface = new GameSurface(MainActivity.this);
        setContentView(gameSurface);

//        mThreadingView = new ThreadingView2(this);
//        setContentView(mThreadingView);

//        mAnimatedView = new AnimatedView(this);
//        setContentView(mAnimatedView);


        // Start the background thread to increment the ticker
//        Thread thread = new Thread(mThreading2View);
//        thread.start();
    }

}
