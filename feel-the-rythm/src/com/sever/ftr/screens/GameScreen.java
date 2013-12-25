package com.sever.ftr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sever.ftr.FTRGame;
import com.sever.ftr.GameState;
import com.sever.ftr.handlers.MidiNoteConverter;
import com.sever.ftr.handlers.NoteButtonHandler;
import com.sever.ftr.handlers.ResizableImage;
import com.sever.ftr.handlers.Stave;

public class GameScreen implements Screen {
	
	private static final String TEMP_MIDI_PATH = "temp/temp.mid";

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;

	private Texture backgroundTex;
	
	private NoteButtonHandler noteButtonHandler;
	
	private BitmapFont titleFont;
	private Label songTitle;
	
	private Stave stave;
	ResizableImage doneButton;
	ResizableImage replayButton;
	private ResizableImage playButton;
	
	Image stoveBg;
	
	public GameScreen(FTRGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		batch.begin();
		backgroundSprite.draw(batch);
		batch.end();
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		for (Actor actor : noteButtonHandler.getChildren()) {
			((ResizableImage) actor).resize(width, height);
		}
		/* resize other buttons */
		doneButton.resize(width, height);
		replayButton.resize(width, height);
		playButton.resize(width, height);
		/* Resize stave */
		stave.resize(width, height);
    }


	@Override
	public void show() {
		/* Generate fonts */
		FileHandle fontFile = Gdx.files.internal("fonts/Dosis-Light.ttf");
	    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
	    titleFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.1f));
	    generator.dispose();
		/* Load background */
		backgroundTex = new Texture("textures/background.png");
		backgroundTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundSprite = new Sprite(backgroundTex);
		backgroundSprite.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		/* Load GUI */
		atlas = new TextureAtlas(Gdx.files.internal("textures/gamescreen.pack"));
		skin = new Skin(atlas);
		batch = new SpriteBatch();
		stage = new Stage();
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		
		doneButton = new ResizableImage(skin.getDrawable("done-button"), 0.95f, 0.05f, 0.2f, ResizableImage.BOTTOM_RIGHT);
		replayButton = new ResizableImage(skin.getDrawable("replay-button"), skin.getDrawable("replay-button-down"), 0.825f, 0.05f, 0.2f, ResizableImage.BOTTOM_RIGHT);
		playButton = new ResizableImage(skin.getDrawable("play-button"), skin.getDrawable("play-button-down"),0.55f, 0.05f, 0.15f, ResizableImage.BOTTOM_LEFT);
		doneButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				//TODO TRANSFER THIS TO EDITOR !!!
				// MidiNoteConverter.saveMidi(stave.getNoteList(), FTRGame.LEVELS_DIR_PATH + "/myLevel.mid", MidiNoteConverter.LOCAL_STORAGE);
				/* Save users solution to gamestate and switch screen */
				game.getGameState().setUsersSolution(stave.getNoteList());
				game.switchScreen(FTRGame.SCORING_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		replayButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.replayMidi(game.getGameState().getCurrentMidiPath(), game.getGameState().getStorageType().equals(MidiNoteConverter.INTERNAL_STORAGE));
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		playButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				MidiNoteConverter.saveMidi(stave.getNoteList(), TEMP_MIDI_PATH, MidiNoteConverter.LOCAL_STORAGE);
				System.out.println(Gdx.files.local(TEMP_MIDI_PATH).path().toString());
				game.playMidi(Gdx.files.local(TEMP_MIDI_PATH).path().toString(), false, false);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		
		noteButtonHandler = new NoteButtonHandler(skin); // Add note buttons to widgetGroup
		
		LabelStyle ls = new LabelStyle(titleFont, Color.BLACK);
		songTitle = new Label(game.getGameState().getSongTitle(), ls); // title label
		songTitle.setPosition(Gdx.graphics.getWidth()-songTitle.getWidth() - Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() - songTitle.getHeight());
		
		/* Stave */
		stave = new Stave(0.25f, 0.35f, 0.50f, 0.65f, 6, noteButtonHandler);
		stave.addNote(game.getGameState().getSolution().get(0)); // set first note of solution
		stage.addActor(stave);
		stage.addActor(noteButtonHandler);
		
		stage.addActor(doneButton);
		stage.addActor(replayButton);
		stage.addActor(songTitle);
		stage.addActor(playButton);
		
		Gdx.input.setInputProcessor(stage);
		
		// MIDI stuff
		GameState gameState = game.getGameState();
		game.playMidi(gameState.getCurrentMidiPath(), false, game.getGameState().getStorageType().equals(MidiNoteConverter.INTERNAL_STORAGE));
	}

	@Override
	public void hide() {
		game.stopMidi();
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		skin.dispose();
		backgroundTex.dispose();
		batch.dispose();
		stage.dispose();
		atlas.dispose();
		titleFont.dispose();
		stave.dispose();
	}
}
