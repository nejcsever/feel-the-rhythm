package com.sever.ftr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sever.ftr.screens.MainMenu;

public class FTRGame extends Game {
	private SpriteBatch batch;

	/* List of game screens */
	private Screen mainMenuScreen;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		mainMenuScreen = new MainMenu(this);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
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
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	/* Getters and setters */
	public SpriteBatch getBatch() {
		return batch;
	}
}
