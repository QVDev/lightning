package com.example.SignGreet;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundTrackFX
{
    private MediaPlayer mp;
    private Context context;

    private boolean isBeaming;

    public SoundTrackFX(Context context)
    {
        this.context = context;
        initMedia();

    }

    private void initMedia()
    {
        mp = MediaPlayer.create(context, R.raw.beam);
        mp.setLooping(true);
    }


    public void playBeam()
    {
        if(!isBeaming)
        {
            isBeaming = true;
            mp.start();
        }
    }

    public void stopBeam()
    {
        if (isBeaming)
        {
            isBeaming = false;
            mp.pause();
        }
    }

}
