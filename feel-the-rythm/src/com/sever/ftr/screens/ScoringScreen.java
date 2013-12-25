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
import com.sever.ftr.HighScore;
import com.sever.ftr.handlers.Note;
import com.sever.ftr.handlers.ScoringHandler;
import com.sever.ftr.handlers.Stave;

public class ScoringScreen implements Screen {

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
	private BitmapFont smallFont;
	
	private Stave stave;

	public ScoringScreen(FTRGame game) {
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
		stave.resize(width, height);
    }


	@Override
	public void show() {
		/* Generate fonts */
	    FileHandle fontFile = Gdx.files.internal("fonts/Dosis-Light.ttf");
	    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
	    titleFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.1f));
	    buttonFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.08f));
	    smallFont = generator.generateFont(Math.round(Gdx.graphics.getHeight() * 0.03f));
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
		table.bottom();
		
		int score = ScoringHandler.getScorePercentage(game.getGameState().getUsersSolution(), game.getGameState().getSolution());
		int highScore = HighScore.updateHighScore(game.getGameState().getCurrentMidiPath(), score);
		
		final TextButtonStyle tbsBack = new TextButtonStyle(skin.getDrawable("white-level-button"),skin.getDrawable("white-level-button-down"),skin.getDrawable("white-level-button-down"), buttonFont);
		tbsBack.fontColor = Color.BLACK;
		TextButton backButton = new TextButton("   Continue   ", tbsBack);
		backButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.LEVEL_SELECT_SCREEN);
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
		
		LabelStyle lsTitle = new LabelStyle(titleFont, Color.BLACK);
		Label titleLabel = new Label((highScore < score) ? "NEW HIGHSCORE!" : "Your score:", lsTitle);
		table.add(titleLabel).top();
		table.row();
		LabelStyle lsScore = new LabelStyle(titleFont, new Color(0,0.6f,0,1)); // dark green
		if (score < 50) {
			lsScore.fontColor = Color.RED;
		} else if (score < 90) {
			lsScore.fontColor = new Color(0.9f,0.4f,0,1); // orange
		}
		table.add(new Label(score + "%", lsScore)).pad(Gdx.graphics.getHeight()*0.03f);
		if (highScore > score) {
			LabelStyle phs = new LabelStyle(smallFont, Color.DARK_GRAY);
			table.row();
			table.add(new Label("High score: " + highScore + "%", phs)).pad(Gdx.graphics.getHeight()*0.01f);
		}
		table.row();
		table.add(backButton).padBottom(Gdx.graphics.getHeight()*0.05f).bottom();
		
		stave = new Stave(0.15f, 0.55f, 0.4f, 0.7f, 7);
		/* Fill stave with users solution */
		for(Note note : game.getGameState().getUsersSolution()) {
			stave.addNote(note); // set first note of solution
		}
		stage.addActor(stave);
		stage.addActor(table);
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
		stave.dispose();
		smallFont.dispose();
	}

}
