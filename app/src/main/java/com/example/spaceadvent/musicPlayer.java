package com.example.spaceadvent;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

public class musicPlayer {
    private static MediaPlayer mediaPlayer;

    public static void playMusic(Context context, int resourceId, boolean loop){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(context, resourceId);
        mediaPlayer.setLooping(loop);
        mediaPlayer.start();
    }
    public static void playRandom(Context context,int[] musicList){
        Random random = new Random();
        mediaPlayer = MediaPlayer.create(context, musicList[random.nextInt(4)]);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            int nextRandomIndex = random.nextInt(musicList.length);
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(context, musicList[random.nextInt(4)]);
            mediaPlayer.start();
        });
        mediaPlayer.start();
    }
    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void unpause() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    public static void stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
