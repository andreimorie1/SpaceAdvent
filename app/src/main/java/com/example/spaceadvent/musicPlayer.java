package com.example.spaceadvent;
import android.content.Context;
import android.media.MediaPlayer;
public class musicPlayer {
    private static MediaPlayer mediaPlayer;

    public static void start(Context context, int resourceId, boolean loop){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(context, resourceId);
        if (loop){
            mediaPlayer.setLooping(loop);
        } else {
            mediaPlayer.setOnCompletionListener(MediaPlayer::start);
        }
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
