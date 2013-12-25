package com.sever.ftr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.sever.ftr.interfaces.MidiPlayer;
import com.sever.ftr.screens.AboutScreen;
import com.sever.ftr.screens.GameScreen;
import com.sever.ftr.screens.LevelSelectScreen;
import com.sever.ftr.screens.MainMenu;
import com.sever.ftr.screens.ScoringScreen;
import com.sever.ftr.screens.TutorialScreen;

public class FTRGame extends Game {
	
	/* Screen names */
	public static final String GAME_SCREEN = "gameScreen";
	public static final String MAIN_MENU_SCREEN = "mainMenuScreen";
	public static final String LEVEL_SELECT_SCREEN = "levelSelectScreen";
	public static final String SCORING_SCREEN = "scoringScreen";
	public static final String ABOUT_SCREEN = "aboutScreen";
	public static final String TUTORIAL_SCREEN = "tutorialScreen";
	
	public static final String LEVELS_DIR_PATH = "levels";
	
	/* List of game screens */
	private Screen mainMenuScreen;
	private Screen gameScreen;
	private Screen levelSelectScreen;
	private Screen scoringScreen;
	private Screen aboutScreen;
	private Screen tutorialScreen;
	
	/* Game state (current midi, solution,...) */
	private GameState gameState;

	private MidiPlayer midiPlayer;
	
	private Music backgroundMusic;
	private Sound click;
	
	public FTRGame(MidiPlayer midiPlayer) {
		this.midiPlayer = midiPlayer;
	}
	
	@Override
	public void create() {
		gameState = new GameState();
		mainMenuScreen = new MainMenu(this);
		this.setScreen(mainMenuScreen);
		/* Load sound */
		click = Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3"));
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/canon.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
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
		backgroundMusic.dispose();
		click.dispose();
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
			backgroundMusic.stop();
			if (gameScreen == null)
				gameScreen = new GameScreen(this);
			this.setScreen(gameScreen);
			return;
		}
		if (screenName.equals(LEVEL_SELECT_SCREEN)) {
			if (levelSelectScreen == null)
				levelSelectScreen = new LevelSelectScreen(this);
			this.setScreen(levelSelectScreen);
		}
		if (screenName.equals(MAIN_MENU_SCREEN)) {
			this.setScreen(mainMenuScreen);
			if (!backgroundMusic.isPlaying()) {
				backgroundMusic.stop();
				backgroundMusic.play();
			}
		}
		if (screenName.equals(SCORING_SCREEN)) {
			if (scoringScreen == null)
				scoringScreen = new ScoringScreen(this);
			this.setScreen(scoringScreen);
		}
		if (screenName.equals(ABOUT_SCREEN)) {
			if (aboutScreen == null)
				aboutScreen = new AboutScreen(this);
			this.setScreen(aboutScreen);
		}
		if (screenName.equals(TUTORIAL_SCREEN)) {
			if (tutorialScreen == null)
				tutorialScreen = new TutorialScreen(this);
			this.setScreen(tutorialScreen);
		}
	}
	
	public Music getBackgroundMusic() {
		return backgroundMusic;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public Sound getClick() {
		return click;
	}
}
