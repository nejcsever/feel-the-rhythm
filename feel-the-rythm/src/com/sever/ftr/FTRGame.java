package com.sever.ftr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.sever.ftr.interfaces.MidiPlayer;
import com.sever.ftr.screens.GameScreen;
import com.sever.ftr.screens.LevelSelectScreen;
import com.sever.ftr.screens.MainMenu;
import com.sever.ftr.screens.ScoringScreen;

public class FTRGame extends Game {
	
	/* Screen names */
	public static final String GAME_SCREEN = "gameScreen";
	public static final String MAIN_MENU_SCREEN = "mainMenuScreen";
	public static final String LEVEL_SELECT_SCREEN = "levelSelectScreen";
	public static final String SCORING_SCREEN = "scoringScreen";
	
	public static final String LEVELS_DIR_PATH = "levels";
	
	/* List of game screens */
	private Screen mainMenuScreen;
	private Screen gameScreen;
	private Screen levelSelectScreen;
	private Screen scoringScreen;
	
	/* Game state (current midi, solution,...) */
	private GameState gameState;

	private MidiPlayer midiPlayer;
	
	public FTRGame(MidiPlayer midiPlayer) {
		this.midiPlayer = midiPlayer;
	}
	
	@Override
	public void create() {
		gameState = new GameState();
		mainMenuScreen = new MainMenu(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		try {
			midiPlayer.stop();
			midiPlayer.release();
		} catch (Exception e) {
			// No sequencer opened
		}
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
	
	public void playMidi(String source, boolean looping, boolean fromAssets) {
		midiPlayer.setLooping(looping);
        midiPlayer.setVolume(100f);
        midiPlayer.open(source, fromAssets);
        midiPlayer.play();
	}
	
	public void replayMidi(String source, boolean fromAssets) {
		if (midiPlayer.isPlaying()) {
			midiPlayer.stop();
		}
		playMidi(source, false, fromAssets);
	}
	
	public void stopMidi() {
		if (midiPlayer.isPlaying()) {
			midiPlayer.stop();
		}
	}
	
	public void switchScreen(String screenName) {
		if (screenName.equals(GAME_SCREEN)) {
			if (gameScreen == null)
				gameScreen = new GameScreen(this);
			this.setScreen(gameScreen);
		}
		if (screenName.equals(LEVEL_SELECT_SCREEN)) {
			if (levelSelectScreen == null)
				levelSelectScreen = new LevelSelectScreen(this);
			this.setScreen(levelSelectScreen);
		}
		if (screenName.equals(MAIN_MENU_SCREEN)) {
			this.setScreen(mainMenuScreen);
		}
		if (screenName.equals(SCORING_SCREEN)) {
			if (scoringScreen == null)
				scoringScreen = new ScoringScreen(this);
			this.setScreen(scoringScreen);
		}
	}
	
	public GameState getGameState() {
		return gameState;
	}
}
