package com.example.SignGreet;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundEffects
{
    public static final int SOUND_EXPLOSION = 1;

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    private Context context;
    private AudioManager mgr;
    private float volume;
    private boolean isBeamPlaying;
    private int playing;

    public SoundEffects(Context context)
    {
        this.context = context;
        initSounds();
        initMgr();
    }

    private void initSounds()
    {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(context, R.raw.burst, 1));
    }

    private void initMgr()
    {
        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        /* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = streamVolumeCurrent / streamVolumeMax;
    }

    public void playSound(int sound)
    {
        /* Play the sound with the correct volume */
        playing = soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
    }

    public void hit()
    {
        playSound(SOUND_EXPLOSION);
    }


}
