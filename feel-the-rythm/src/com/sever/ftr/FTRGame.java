package com.sever.ftr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.sever.ftr.midi.MidiPlayer;
import com.sever.ftr.screens.MainMenu;

public class FTRGame extends Game {
	/* List of game screens */
	private Screen mainMenuScreen;
	
	private MidiPlayer midiPlayer;
	
	public FTRGame(MidiPlayer midiPlayer) {
		this.midiPlayer = midiPlayer;
	}
	
	@Override
	public void create() {
		mainMenuScreen = new MainMenu(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
        midiPlayer.stop(); // TODO STOP PLAYING MIDI SOUND OR ANY SOUND! PAUSE EVERYTHING!
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	public void playMidi(String source, boolean looping) {
		midiPlayer.setLooping(looping);
        midiPlayer.setVolume(100f);
        midiPlayer.open(source);
        midiPlayer.play();
	}
}
