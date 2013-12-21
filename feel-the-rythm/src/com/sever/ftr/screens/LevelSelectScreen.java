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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sever.ftr.FTRGame;
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
		FileHandle dirHandle = Gdx.files.internal(FTRGame.LEVELS_DIR_PATH);
		table.add(new Label("Internal files: ", new LabelStyle(new BitmapFont(), Color.BLACK))).pad(10);
		for (FileHandle file: dirHandle.list()) {
			table.row();
			TextButton tb = new TextButton(" " + file.name() + " ", new TextButtonStyle(skin.getDrawable("level-button"), skin.getDrawable("level-button"), skin.getDrawable("level-button"),new BitmapFont()));
			table.add(tb).center().top().pad(10).height((float) Gdx.graphics.getHeight() * 0.1f);
			addLevelButtonListener(tb, file.name(), file.path(), MidiNoteConverter.INTERNAL_STORAGE);
		}
		dirHandle = Gdx.files.local(FTRGame.LEVELS_DIR_PATH);
		// local files
		if (Gdx.app.getType() == ApplicationType.Android && dirHandle.list().length != 0) {
			table.row();
			table.add(new Label("Local files: ", new LabelStyle(new BitmapFont(), Color.BLACK))).pad(10);
			for (FileHandle file: dirHandle.list()) {
				table.row();
				TextButton tb = new TextButton(" " + file.name() + " ", new TextButtonStyle(skin.getDrawable("level-button"), skin.getDrawable("level-button"), skin.getDrawable("level-button"),new BitmapFont()));
				table.add(tb).center().top().pad(10).height((float) Gdx.graphics.getHeight() * 0.1f);
				addLevelButtonListener(tb, file.name(), file.path(), MidiNoteConverter.LOCAL_STORAGE);
			}
		}
		stage.addActor(table);
	}

	private void addLevelButtonListener(TextButton levelButton, final String fileName, final String filePath, final String storageType) {
		levelButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {	
				/* Update gamestate and switch screen */

				System.out.println("LOCAL PATH: " + filePath);
				game.getGameState().setCurrentMidiPath(filePath);
				game.getGameState().setStorageType(storageType);
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
	}

}
