package com.sever.ftr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.sever.ftr.FTRGame;

public class MainMenu implements Screen {

	private final FTRGame game;
	private OrthographicCamera camera;
	
	private Stage stage;
	private Table table;
	private TextButton playButton, settingsButton;
	private Label title;
	private Skin skin;
	private BitmapFont permanentMarkerFont;
	private TextureAtlas atlas;
	
	private static final int WIDTH = 300;
	
	public MainMenu(FTRGame game) {
		this.game = game;

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		/*camera = new OrthographicCamera(1f, aspectRatio);*/
		camera = new OrthographicCamera(WIDTH*aspectRatio, WIDTH);
        camera.position.set(WIDTH*aspectRatio / 2, WIDTH / 2, 0);
		
		/* Loading assets */
		permanentMarkerFont = new BitmapFont(Gdx.files.internal("fonts/permanent-marker_46_white.fnt"), false);
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/mainmenu.pack"));
		skin = new Skin(atlas);
		TextButtonStyle playButtonStyle = new TextButtonStyle(skin.getDrawable("play-up"),skin.getDrawable("play-down"),skin.getDrawable("play-up"), permanentMarkerFont);
		playButtonStyle.fontColor = Color.BLACK;
		playButton = new TextButton("Play", playButtonStyle);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		LabelStyle titleStyle = new LabelStyle(permanentMarkerFont, Color.WHITE);
		title = new Label("FEEL the RHYTHM", titleStyle);
		table = new Table(skin);
		table.add(title);
		table.row().padTop(30);
		table.add(playButton);
		table.setPosition(WIDTH/2, WIDTH*aspectRatio/2);
		// TODO table.debug(); // remove
		stage.addActor(table);
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);
		
		/* Drawing */
		game.getBatch().begin();
		stage.act();
		stage.draw();
		// TODO Table.drawDebug(stage); // TODO remove
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
