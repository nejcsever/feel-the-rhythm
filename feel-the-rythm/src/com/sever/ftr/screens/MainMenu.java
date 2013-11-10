package com.sever.ftr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.sever.ftr.FTRGame;

public class MainMenu implements Screen {

	private final FTRGame game;

	private Texture backgroundTex;
	private Sprite backgroundSprite;
	private SpriteBatch batch;
	private Image playButton;
	private Sound buttonClick;
	
	private Skin skin;
	private TextureAtlas atlas;

	private Stage stage;
	private Table table;
	
	private OrthographicCamera cam;

	public MainMenu(FTRGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		cam.update();
		stage.act(delta);
		batch.begin();
		backgroundSprite.draw(batch);
		batch.end();
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void show() {
		
		cam = new OrthographicCamera(1024, 512);
		
		backgroundTex = new Texture("textures/background.png");
		backgroundTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundSprite = new Sprite(backgroundTex);
		backgroundSprite.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		atlas = new TextureAtlas(Gdx.files.internal("textures/mainmenu.pack"));
		skin = new Skin(atlas);
		
		batch = new SpriteBatch();

		stage = new Stage();
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		table = new Table();
		table.top();
		Image logo = new Image(skin.getDrawable("logo"));
		logo.setScaling(Scaling.fillY);
		table.add(logo).expand().fill().colspan(3);
		table.row().padTop(-10).padBottom(50);
		
		playButton = new Image(skin.getDrawable("play-button"));
		playButton.setScaling(Scaling.fill);
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("sound/button-click.mp3"));
		playButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				super.clicked(event, x, y);
	            game.setScreen( new GameScreen(game));
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				playButton.setDrawable(skin.getDrawable("play-button-down"));
				buttonClick.play();
				System.out.print("DOWN");
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				playButton.setDrawable(skin.getDrawable("play-button"));
				System.out.print("UP");
			}
	    });
		Image settingsButton = new Image(skin.getDrawable("settings-button"));
		settingsButton.setScaling(Scaling.fillY);
		Image aboutButton = new Image(skin.getDrawable("about-button"));
		aboutButton.setScaling(Scaling.fillY);
		table.add(settingsButton).center().center().bottom().padRight(-20);
		table.add(playButton).expandY().fillY().center();
		table.add(aboutButton).center().bottom().padLeft(-20);
		table.debug();
		stage.addActor(table);
		table.setFillParent(true);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		this.dispose();
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
		skin.dispose();
		atlas.dispose();
		buttonClick.dispose();
		backgroundTex.dispose();
	}

}
