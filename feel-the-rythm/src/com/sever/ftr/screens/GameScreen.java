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
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.sever.ftr.FTRGame;
import com.sever.ftr.actors.ResizableImage;

public class GameScreen implements Screen {

	private final FTRGame game;
	
	private Stage stage;

	private SpriteBatch batch;

	private Skin skin;

	private TextureAtlas atlas;

	private Sprite backgroundSprite;

	private Texture backgroundTex;
	
	private Image playButton;
	private Image settingsButton;
	private Image aboutButton;
	private Image logo;
	
	private WidgetGroup wGroup;

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
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		for (Actor actor : wGroup.getChildren()) {
			((ResizableImage) actor).resize(width, height);
		}
		playButton.setSize(Math.round(height*0.45), Math.round(height*0.45));
		playButton.setPosition(Math.round(width*0.5) - playButton.getWidth()/2, Math.round(height*0.05));
		settingsButton.setSize(Math.round(height*0.3), Math.round(height*0.3));
		settingsButton.setPosition(Math.round(width*0.05), Math.round(height*0.05));
		aboutButton.setSize(Math.round(height*0.3), Math.round(height*0.3));
		aboutButton.setPosition(Math.round(width*0.95) - aboutButton.getWidth(), Math.round(height*0.05));
		logo.setSize(500, Math.round(height*0.5));
		logo.setPosition(width/2 - logo.getWidth()/2, height - logo.getHeight());
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
		
		playButton = new Image(skin.getDrawable("play-button"));
		playButton.setScaling(Scaling.fillY);
		playButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				playButton.setDrawable(skin.getDrawable("play-button-down"));
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				playButton.setDrawable(skin.getDrawable("play-button"));
			}
	    });
		settingsButton = new Image(skin.getDrawable("settings-button"));
		settingsButton.setScaling(Scaling.fillY);
		settingsButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("TEST" + x);
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				settingsButton.setDrawable(skin.getDrawable("settings-button"));
			}
	    });
		ResizableImage testButton = new ResizableImage(skin.getDrawable("logo"), 0.5f, 1f, 0.5f, ResizableImage.TOP_CENTER);
		testButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("TEST: " + x + ", " + y);
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		aboutButton = new Image(skin.getDrawable("about-button"));
		aboutButton.setScaling(Scaling.fillY);
		logo = new Image(skin.getDrawable("logo"));
		logo.setScaling(Scaling.fillY);
		logo.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y)
	        {
				super.clicked(event, x, y);
	        }
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("TEST" + x);
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				settingsButton.setDrawable(skin.getDrawable("settings-button"));
			}
	    });
		wGroup = new WidgetGroup();
		//wGroup.addActor(playButton);
		wGroup.addActor(testButton);
		//wGroup.addActor(testButton);
		//wGroup.addActor(logo);
		stage.addActor(wGroup);
		
		Gdx.input.setInputProcessor(stage);
		
		
		// MIDI stuff
		//game.playMidi("sound/Vaja1.mid", false);
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
