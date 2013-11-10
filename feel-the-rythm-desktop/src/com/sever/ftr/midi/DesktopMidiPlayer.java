package com.sever.ftr.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DesktopMidiPlayer implements MidiPlayer {

    private Sequence sequence;
    private Sequencer sequencer;

    public DesktopMidiPlayer() {

        try {
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
        	e.printStackTrace();
        }

    }

    public void open(String fileName) {

        FileHandle file = Gdx.files.internal(fileName);
        try {
            sequence = MidiSystem.getSequence(file.read());
            sequencer.open();
            sequencer.setSequence(sequence);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public boolean isLooping() {
        if(sequencer != null){
            return sequencer.getLoopCount() != 0;
        }
        return false;
    }

    public void setLooping(boolean loop) {
        if(sequencer != null){
            if(!loop){
                sequencer.setLoopCount(0);
                return;
            }
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }
    }

    public void play() {
        if(sequencer != null){
            sequencer.start();
        }
    }

    public void pause() {
        stop();
    }


    public void stop() {
        if(sequencer != null){
            sequencer.stop();
        }
    }

    public void release() {
        if(sequencer != null){
            sequencer.close();
        }
    }

    public boolean isPlaying() {
        return sequencer.isRunning();
    }

    public void setVolume(float volume) {
        //Not implemented
    }
}