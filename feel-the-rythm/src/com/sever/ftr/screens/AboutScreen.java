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

public class AboutScreen implements Screen {

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;
	private Texture backgroundTex;
	
	private Table table;
	
	private BitmapFont titleFont;
	private BitmapFont buttonFont;
	
	public AboutScreen(FTRGame game) {
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
		
		// TITLE
		LabelStyle lsTitle = new LabelStyle(titleFont, Color.BLACK);
		Label titleLabel = new Label("Feel the Rhythm", lsTitle);
		table.add(titleLabel).top().expand().padTop(Gdx.graphics.getHeight() * 0.05f);
		table.row();
		
		// DESCRIPTION
		LabelStyle lsText = new LabelStyle(buttonFont, Color.DARK_GRAY);
		table.add(new Label("Feel the Rhythm is a project for E-learning course", lsText));
		table.row();
		table.add(new Label("at the Faculty of Computer and Information Science, Ljubljana.", lsText));
		table.row();
		table.add(new Label("Start playing and FEEL THE RHYTHM!", lsText)).padTop(Gdx.graphics.getHeight() * 0.02f);
		table.row();
		LabelStyle ls = new LabelStyle(buttonFont, Color.BLACK);
		table.add(new Label("Author: Nejc Sever", ls)).padTop(Gdx.graphics.getHeight() * 0.05f);
		table.row();
		table.add(new Label("Email: sever.nejc@gmail.com", ls));
		table.row();
		table.add(new Label("January 2014, Slovenia", ls)).padTop(Gdx.graphics.getHeight() * 0.05f);;
		table.row();
		
		// BACKBUTTON
		final TextButtonStyle tbsBack = new TextButtonStyle(skin.getDrawable("white-level-button"),skin.getDrawable("white-level-button-down"),skin.getDrawable("white-level-button-down"), buttonFont);
		tbsBack.fontColor = Color.BLACK;
		TextButton backButton = new TextButton("Back to main menu", tbsBack);
		backButton.pad(Math.round(Gdx.graphics.getHeight() * 0.025f));
		backButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.MAIN_MENU_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				game.getClick().play(game.clickVolume);
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		table.add(backButton).padBottom(Gdx.graphics.getHeight()*0.05f).bottom().expand();
		
		stage.addActor(table);
	}

	@Override
	public void hide() {
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
		buttonFont.dispose();
	}

}
