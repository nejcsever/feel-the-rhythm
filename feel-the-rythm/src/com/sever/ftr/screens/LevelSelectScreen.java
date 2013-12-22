package com.sever.ftr.screens;

import com.badlogic.gdx.Application.ApplicationType;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sever.ftr.FTRGame;
import com.sever.ftr.HighScore;
import com.sever.ftr.handlers.MidiNoteConverter;

public class LevelSelectScreen implements Screen {

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;

	private Texture backgroundTex;
	
	private Table table;
	
	private ScrollPane scrollPane;
	private Table levelsTable;
	
	private BitmapFont titleFont;
	private BitmapFont buttonFont;
	private BitmapFont smallButtonFont;

	public LevelSelectScreen(FTRGame game) {
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
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
    }


	@Override
	public void show() {
		/* Generate fonts */
	    FileHandle fontFile = Gdx.files.internal("fonts/Dosis-Light.ttf");
	    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
	    titleFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.1f));
	    buttonFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.05f));
	    smallButtonFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.03f));
	    generator.dispose();
		
		/* Load background */
		backgroundTex = new Texture("textures/background.png");
		backgroundTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundSprite = new Sprite(backgroundTex);
		backgroundSprite.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		/* Load GUI */
		atlas = new TextureAtlas(Gdx.files.internal("textures/levelselect.pack"));
		skin = new Skin(atlas);
		batch = new SpriteBatch();
		stage = new Stage();
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		table.setFillParent(true);

		/* Show buttons for levels */
		// internal files
		levelsTable = new Table();
		
		FileHandle dirHandle = Gdx.files.internal(FTRGame.LEVELS_DIR_PATH);
		if (Gdx.app.getType() == ApplicationType.Desktop) {
		  dirHandle = Gdx.files.internal("./bin/" + FTRGame.LEVELS_DIR_PATH);
		}
		TextButtonStyle tbsWhite = new TextButtonStyle(skin.getDrawable("white-level-button"), skin.getDrawable("white-level-button-down"), skin.getDrawable("white-level-button-down"), buttonFont);
		TextButtonStyle tbsGreen = new TextButtonStyle(skin.getDrawable("green-level-button"), skin.getDrawable("green-level-button-down"), skin.getDrawable("green-level-button-down"), buttonFont);
		tbsWhite.fontColor = Color.BLACK;
		tbsGreen.fontColor = Color.BLACK;
		LabelStyle ls = new LabelStyle(titleFont, Color.BLACK);
		LabelStyle lsPerc = new LabelStyle(smallButtonFont, Color.BLACK);
		lsPerc.fontColor = Color.DARK_GRAY;
		// TODO MAKE IT PRETTIER!!!
		table.add(new Label("Level selection", ls)).pad(30);
		TextButtonStyle tempBtnStyle = tbsWhite; // for green for 100% completed stages
		for (FileHandle file: dirHandle.list()) {
			levelsTable.row();
			int highScore = HighScore.readHighScore(file.path());
			String labelText = (highScore < 0)? "No score yet": "High score: " + highScore + "%";
			if (highScore == 100) {
				tempBtnStyle = tbsGreen;
				labelText = "LEVEL CLEARED";
			} else {
				tempBtnStyle = tbsWhite;
			}
			TextButton tb = new TextButton(MidiNoteConverter.convertFileToTitle(file.name()),tempBtnStyle);
			tb.align(Align.left);
			
			tb.add(new Label(labelText, lsPerc)).center();
			levelsTable.add(tb).center().top().pad(10).width(Gdx.graphics.getWidth()*0.5f);
			addLevelButtonListener(tb, file.name(), file.path(), MidiNoteConverter.INTERNAL_STORAGE);
		}
		dirHandle = Gdx.files.local(FTRGame.LEVELS_DIR_PATH);
		// local files
		if (Gdx.app.getType() == ApplicationType.Android && dirHandle.list().length != 0) {
			for (FileHandle file: dirHandle.list()) {
				levelsTable.row();
				TextButton tb = new TextButton(MidiNoteConverter.convertFileToTitle(file.name()), tbsGreen);
				levelsTable.add(tb).center().top().pad(10).height((float) Gdx.graphics.getHeight() * 0.1f).width(Gdx.graphics.getWidth()*0.5f);
				addLevelButtonListener(tb, file.name(), file.path(), MidiNoteConverter.LOCAL_STORAGE);
			}
		}
		scrollPane= new ScrollPane(levelsTable);
		table.row();
		table.add(scrollPane).center();
		table.row();
		final TextButtonStyle tbsBack = new TextButtonStyle(null, null, null, buttonFont);
		tbsBack.fontColor = Color.BLACK;
		TextButton backButton = new TextButton("BACK", tbsBack);
		backButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.MAIN_MENU_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				tbsBack.fontColor = Color.GRAY;
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				tbsBack.fontColor = Color.BLACK;
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		table.add(backButton).pad(40).fill();
		stage.addActor(table);
	}

	private void addLevelButtonListener(TextButton levelButton, final String fileName, final String filePath, final String storageType) {
		levelButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {	
				/* Update gamestate and switch screen */
				game.getGameState().setCurrentMidiPath(filePath);
				game.getGameState().setStorageType(storageType);
				game.getGameState().setSongTitle(MidiNoteConverter.convertFileToTitle(fileName));
				game.getGameState().setSolution(MidiNoteConverter.generateNotesFromMidi(FTRGame.LEVELS_DIR_PATH + "/" + fileName, storageType));
				game.switchScreen(FTRGame.GAME_SCREEN);
				super.clicked(event, x, y);
	        }
	    });
	}

	@Override
	public void hide() {
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
		buttonFont.dispose();
		smallButtonFont.dispose();
	}

}
