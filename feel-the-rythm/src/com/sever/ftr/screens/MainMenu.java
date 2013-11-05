package com.sever.ftr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sever.ftr.FTRGame;

public class MainMenu implements Screen {

	private final FTRGame game;
	private OrthographicCamera camera;
	
	private Stage stage;
	private Table table;
	private Label title;
	private Skin skin;
	private BitmapFont openSansCondensed;
	private TextureAtlas atlas;
	private ImageButton playButton, settingsButton;
	
	private static final int WIDTH = 1000;
	
	public MainMenu(FTRGame game) {
		this.game = game;

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		/*camera = new OrthographicCamera(1f, aspectRatio);*/
		camera = new OrthographicCamera(WIDTH*aspectRatio, WIDTH);
        camera.position.set(WIDTH*aspectRatio / 2, WIDTH / 2, 0);
		
		/* Loading assets */
        openSansCondensed = new BitmapFont(Gdx.files.internal("fonts/opensans-condensed_100_black.fnt"), false);
        openSansCondensed.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        openSansCondensed.setColor(Color.WHITE);
        
		atlas = new TextureAtlas(Gdx.files.internal("textures/mainmenu.pack"));
		skin = new Skin(atlas);
		//TextButtonStyle playButtonStyle = new TextButtonStyle(skin.getDrawable("play-up"),skin.getDrawable("play-down"),skin.getDrawable("play-up"), openSansCondensed);
		//playButtonStyle.fontColor = Color.BLACK;
		//playButton = new TextButton("Play", playButtonStyle);
		ImageButtonStyle playButtonStyle = new ImageButtonStyle(skin.getDrawable("play-button"),skin.getDrawable("play-button"),skin.getDrawable("play-button"),skin.getDrawable("play-button"),skin.getDrawable("play-button"),skin.getDrawable("play-button"));
		playButton = new ImageButton(playButtonStyle);
		ImageButtonStyle settingsStyle = new ImageButtonStyle(skin.getDrawable("settings-button"),skin.getDrawable("settings-button"),skin.getDrawable("settings-button"),skin.getDrawable("settings-button"),skin.getDrawable("settings-button"),skin.getDrawable("settings-button"));
		settingsButton = new ImageButton(settingsStyle);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		LabelStyle titleStyle = new LabelStyle(openSansCondensed, Color.WHITE);
		title = new Label("FEEL the RHYTHM", titleStyle);
		table = new Table(skin);
		table.add(title).left().colspan(2);
		table.row().padTop(30);
		table.add(settingsButton).center().right();
		table.add(playButton).bottom().right();
		table.setPosition(WIDTH/2, WIDTH*aspectRatio/2);
		table.debug(); // TODO remove
		stage.addActor(table);
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		/* Drawing */
		game.getBatch().begin();
		stage.act();
		stage.draw();
		//Table.drawDebug(stage); // TODO remove
		game.getBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        stage.setViewport(WIDTH, WIDTH * aspectRatio);
		table.setPosition(WIDTH/2, WIDTH * aspectRatio/2);
	}

	@Override
	public void show() {
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
		stage.dispose();
	}

}
