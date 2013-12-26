package com.sever.ftr.interfaces;

public interface MidiPlayer {
    public void open(String fileName, boolean fromAssets);
    public boolean isLooping();
    public void setLooping(boolean loop);
    public void play();
    public void pause();
    public void stop();
    public void release();
    public boolean isPlaying();
    public void setVolume(float volume);
}