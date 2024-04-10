package com.example.spaceadvent;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private int currentMusicResource;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int musicResource = intent.getIntExtra("musicResource", 0);
            boolean isLooping = intent.getBooleanExtra("isLooping", false);

            if (musicResource != 0 && musicResource != currentMusicResource) {
                playMusic(musicResource, isLooping);
            }
        }

        // Return START_STICKY to indicate that the service should be restarted if it gets terminated
        return START_STICKY;
    }

    private void playMusic(int musicResource, boolean isLooping) {
        stopMusic(); // Stop any currently playing music
        mediaPlayer = MediaPlayer.create(this, musicResource);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.start();
            currentMusicResource = musicResource;
        }

    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentMusicResource = 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic(); // Stop music playback and release resources when the service is destroyed
    }
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}

