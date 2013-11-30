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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sever.ftr.FTRGame;
import com.sever.ftr.handlers.NoteButtonHandler;
import com.sever.ftr.handlers.ResizableImage;
import com.sever.ftr.handlers.Stave;

public class GameScreen implements Screen {

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;

	private Texture backgroundTex;
	
	private WidgetGroup widgetGroup;
	
	private Stave stave;
	
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
		for (Actor actor : widgetGroup.getChildren()) {
			((ResizableImage) actor).resize(width, height);
		}
		/* Resize stave */
		stave.resize(width, height);
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
		atlas = new TextureAtlas(Gdx.files.internal("textures/gamescreen.pack"));
		skin = new Skin(atlas);
		batch = new SpriteBatch();
		stage = new Stage();
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		
		ResizableImage doneButton = new ResizableImage(skin.getDrawable("done-button"), 0.95f, 0.05f, 0.2f, ResizableImage.BOTTOM_RIGHT);
		ResizableImage replayButton = new ResizableImage(skin.getDrawable("replay-button"), skin.getDrawable("replay-button-down"), 0.825f, 0.05f, 0.2f, ResizableImage.BOTTOM_RIGHT);
		ResizableImage leftArrowButton = new ResizableImage(skin.getDrawable("left-arrow"), skin.getDrawable("left-arrow-down"), 0.23f, 0.85f,  0.4f, ResizableImage.TOP_RIGHT);
		ResizableImage rightArrowButton = new ResizableImage(skin.getDrawable("right-arrow"), skin.getDrawable("right-arrow-down"), 0.92f, 0.85f,  0.4f, ResizableImage.TOP_LEFT);
		doneButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.switchScreen(FTRGame.MAIN_MENU_SCREEN);
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		replayButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				game.replayMidi("sound/Vaja1.mid");
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		
		widgetGroup = new WidgetGroup();
		widgetGroup.addActor(leftArrowButton);
		widgetGroup.addActor(rightArrowButton);
		widgetGroup.addActor(doneButton);
		widgetGroup.addActor(replayButton);
		new NoteButtonHandler(widgetGroup, skin); // Add note buttons to widgetGroup
		
		/* Stave */
		stave = new Stave(0.25f, 0.35f, 0.55f, 0.65f);
		stage.addActor(stave);
		stage.addActor(widgetGroup);
		// Stave movement
		leftArrowButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				stave.moveLeft();
				((ResizableImage) event.getListenerActor()).drawImageDown();
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((ResizableImage) event.getListenerActor()).drawOriginalImage();
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		rightArrowButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				stave.moveRight();
				((ResizableImage) event.getListenerActor()).drawImageDown();
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((ResizableImage) event.getListenerActor()).drawOriginalImage();
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		
		Gdx.input.setInputProcessor(stage);
		
		
		
		// MIDI stuff
		game.playMidi("sound/Vaja1.mid", false);
		/*MidiFile mf;
		try {
			mf = new MidiFile(Gdx.files.internal("sound/Vaja1.mid").read());
			MidiTrack mt = mf.getTracks().get(1);
			System.out.println(mt.getEvents().toString());
			System.out.println("************************");
			mf = new MidiFile(Gdx.files.internal("sound/Vaja2.mid").read());
			mt = mf.getTracks().get(1);
			System.out.println(mt.getEvents().toString());
			System.out.println("************************");
			MidiFile myFile = new MidiFile();
			MidiTrack myTrack = new MidiTrack();
			Tempo t = new Tempo();
		    t.setBpm(200);
		    myTrack.insertEvent(t);
	    	int delay = 0;
	    	int duration = 1000; // ms
	    	int[] notes = new int[]{60, 62, 64, 65, 67, 69, 71, 72};
	    	for (int i : notes) {
	    		NoteOn no = new NoteOn(delay, 0, i, 100);
	    		myTrack.insertEvent(no);
	    		NoteOff noff = new NoteOff(0, 0, i, 100);
	    		myTrack.insertEvent(noff);
	    		//myTrack.insertNote(0, i, 100, delay, duration);
	    	    delay += duration;
	    	}
			myFile.addTrack(myTrack);
			FileHandle fh = Gdx.files.external("feelTheRhythmSounds");
			fh.mkdirs();
			fh = Gdx.files.external("feelTheRhythmSounds/myMidi.mid");
			if (fh.exists()) {
				fh.delete();
			}
			fh.file().createNewFile();
			myFile.writeToFile(fh.file());
			System.out.print(myTrack.getEvents().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void hide() {
		game.stopMidi();
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
