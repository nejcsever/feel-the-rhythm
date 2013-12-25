package com.sever.ftr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sever.ftr.FTRGame;
import com.sever.ftr.handlers.ResizableImage;

public class MainMenu implements Screen {

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;

	private Texture backgroundTex;
	
	private WidgetGroup widgetGroup;

	public MainMenu(FTRGame game) {
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
		for (Actor actor : widgetGroup.getChildren()) {
			((ResizableImage) actor).resize(width, height);
		}
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
		atlas = new TextureAtlas(Gdx.files.internal("textures/mainmenu.pack"));
		skin = new Skin(atlas);
		batch = new SpriteBatch();
		stage = new Stage();
		
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);

		ResizableImage logo = new ResizableImage(skin.getDrawable("logo"), 0.5f, 1f, 0.5f, ResizableImage.TOP_CENTER);
		
		final ResizableImage playButton = new ResizableImage(skin.getDrawable("play-button"), skin.getDrawable("play-button-down"), 0.5f, 0.05f, 0.45f, ResizableImage.BOTTOM_CENTER);
		playButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.LEVEL_SELECT_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				playButton.drawImageDown();
				game.getClick().play();
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				playButton.drawOriginalImage();
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		
		ResizableImage tutorialButton = new ResizableImage(skin.getDrawable("settings-button"), 0.12f, 0.05f, 0.25f, ResizableImage.BOTTOM_LEFT);
		tutorialButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.TUTORIAL_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				game.getClick().play();
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		
		ResizableImage aboutButton = new ResizableImage(skin.getDrawable("about-button"), 0.88f, 0.05f, 0.25f, ResizableImage.BOTTOM_RIGHT);
		aboutButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.ABOUT_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				game.getClick().play();
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		widgetGroup = new WidgetGroup();
		widgetGroup.addActor(playButton);
		widgetGroup.addActor(logo);
		widgetGroup.addActor(tutorialButton);
		widgetGroup.addActor(aboutButton);
		stage.addActor(widgetGroup);
		
		Gdx.input.setInputProcessor(stage);
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
	}

}
