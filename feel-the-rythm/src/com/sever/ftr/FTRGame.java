package com.sever.ftr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.sever.ftr.midi.MidiPlayer;
import com.sever.ftr.screens.GameScreen;
import com.sever.ftr.screens.MainMenu;

public class FTRGame extends Game {
	
	/* Screen names */
	public static final String GAME_SCREEN = "gameScreen";
	public static final String MAIN_MENU_SCREEN = "mainMenuScreen";
	
	/* List of game screens */
	private Screen mainMenuScreen;
	private Screen gameScreen;
	
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
		midiPlayer.stop();
		midiPlayer.release();
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
        // TODO STOP PLAYING MIDI SOUND OR ANY SOUND! PAUSE EVERYTHING!
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
	
	public void switchScreen(String screenName) {
		if (screenName.equals(GAME_SCREEN)) {
			if (gameScreen == null)
				gameScreen = new GameScreen(this);
			this.setScreen(gameScreen);
		}
		if (screenName.equals(MAIN_MENU_SCREEN)) {
			this.setScreen(mainMenuScreen);
		}
	}
}
