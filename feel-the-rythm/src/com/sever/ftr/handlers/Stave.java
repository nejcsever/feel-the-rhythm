package com.sever.ftr.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;

public class Stave extends WidgetGroup {

	private static final int NUMBER_OF_COLUMNS = 5;
	private static final int NUMBER_OF_ROWS = 11;
	private static final String[] NOTE_DRAWABLE_STRINGS = {"full-note", "empty-stem", "full-stem", "stem-flag", "stem-doubleflag"};
	private static final String[] NOTE_DRAWABLE_DOWN_STRINGS = {"full-note-down", "empty-stem-down", "full-stem-down", "stem-flag-down", "stem-doubleflag-down"};
	
	private static final float STEM_NOTE_SIZE_PERCENTAGE = 0.7f;
	private static final float FRONT_PADDING = 0.6f; // paddign for first shown note
	
	/* Note pitch: C D E F G A H C D E F G */
	private static final int[] NOTE_PITCH = new int[]{60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79};
	
	/* Stave position on the screen in percentage */
	private float x;
	private float y;
	/* Stave height and width in percentage */
	private float height;
	private float width;
	/* Current position of noteWindow */
	private int currentColumnPosition = 0;
	private float columnWidthPercentage = (float) 1 / NUMBER_OF_COLUMNS;
	private float rowHeightPercentage = (float) 1 / NUMBER_OF_ROWS;
	private Image background;
	private TextureAtlas atlas;
	private Skin skin;
	private NoteButtonHandler noteButtonHandler;
	/* Contains list of all notes in stave */
	private ArrayList<Note> noteList;
	/* Contains ResizableImage's for notes that are currently visible. */
	private ResizableImage[] noteWindow;
	
	public Stave(float x, float y, float height, float width, NoteButtonHandler noteButtonHandler) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.noteButtonHandler = noteButtonHandler;
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/stave.pack"));
		skin = new Skin(atlas);
		
		System.out.println(height+y);
		
		background = new Image(skin.getDrawable("background"));
		this.addActor(background);
		
		/* Create empty note list */
		noteList = new ArrayList<Note>();
		
		/*create empty noteWindow */
		noteWindow = new ResizableImage[NUMBER_OF_COLUMNS];
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i] = new ResizableImage(0f, ResizableImage.TOP_CENTER);
			this.addActor(noteWindow[i]);
		}
		
		this.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				handleStaveTouch(((Stave)event.getListenerActor()), x, y);
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
		});
	}
	
	/**
	 * Resize background lines and all notes in noteWindow.
	 * @param width - Screen width
	 * @param height - Screen height
	 */
	public void resize(int width, int height) {
		background.setPosition((float)(width * x), (float)(height * y));
		background.setSize((float)(width * this.width), (float)(height * this.height));
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i].resize(width, height);
		}
	}
	
	public void updateNoteWindow() {
		for (int i = 0; i < noteWindow.length; i++) {
			/*If noteList is not shorter then sum of values*/
			if (i + currentColumnPosition >= noteList.size()) {
				noteWindow[i].resetImage();
				continue;
			}

			Note currentNote = noteList.get(i + currentColumnPosition);
			if (currentNote == null) {
				continue;
			}
			
			/* Determine which note to display */
			if (currentNote.getType().equals(NoteButtonHandler.PAUSE)){
				noteWindow[i].resetImage();
				return;
			} else {
				noteWindow[i].setImage(skin.getDrawable(NOTE_DRAWABLE_STRINGS[currentNote.getName()]));
				noteWindow[i].setImageDown(skin.getDrawable(NOTE_DRAWABLE_DOWN_STRINGS[currentNote.getName()]));
				noteWindow[i].setHeightPercentage(height * STEM_NOTE_SIZE_PERCENTAGE);
				
				float yPercentage = (height*rowHeightPercentage*currentNote.getPitch())+y;
				if (currentNote.getPitch() > 5) {
					yPercentage += (height*rowHeightPercentage);
					noteWindow[i].setOrigin(ResizableImage.TOP_CENTER);
					noteWindow[i].drawImageDown();
				} else {
					yPercentage -= (height*rowHeightPercentage);
					noteWindow[i].setOrigin(ResizableImage.BOTTOM_CENTER);
					noteWindow[i].drawOriginalImage();
				}

				noteWindow[i].setyPercentage(yPercentage);
				noteWindow[i].setxPercentage(x + width * (i+FRONT_PADDING)*columnWidthPercentage);
			}
		}
	}
	
	/**
	 * Move note window to the left if it's possible.
	 * @return False if end starting point is reached.
	 */
	public boolean moveLeft() {
		if (currentColumnPosition == 0) {
			return false;
		} else {
			currentColumnPosition--;
			updateNoteWindow();
			return true;
		}
	}
	
	public void moveRight() {
		currentColumnPosition++;
		updateNoteWindow();
	}
	
	public void addNote(int index, Note note) {
		noteList.add(index, note);
	}
	
	private void handleStaveTouch(Stave stave, float x, float y) {
		float widthPx = stave.width * Gdx.graphics.getWidth();
		float xPx = stave.x * Gdx.graphics.getWidth();
		xPx -= xPx*FRONT_PADDING*columnWidthPercentage*2; // TODO make this better :)
		int horizontalIndex = Math.round((((x - xPx) / widthPx)/(columnWidthPercentage))) - 1;
		if (horizontalIndex < 0) {
			horizontalIndex = 0;
		}
		
		float heightPx = stave.height * Gdx.graphics.getHeight();
		float yPx = stave.y * Gdx.graphics.getHeight();
		int verticalIndex = Math.round(((y - yPx) / heightPx)/rowHeightPercentage);
		if (verticalIndex > NUMBER_OF_ROWS) {
			verticalIndex = NUMBER_OF_ROWS;
		} else if (verticalIndex < 0) { // this may happen if touch is recieved on note object and not from background so it must be disabled
			verticalIndex = 0;
		}
		
		/* Add note or edit note */
		String noteType = noteButtonHandler.getNoteType();
		if (horizontalIndex + currentColumnPosition >= noteList.size()) {
			addNote(noteList.size(), new Note(noteButtonHandler.getSelectedButton().getNoteLength(), verticalIndex, noteType));
		} else {
			Note currentNote = noteList.get(horizontalIndex + currentColumnPosition);
			currentNote.setPitch(verticalIndex);
			currentNote.setType(noteType);
			currentNote.setName(noteButtonHandler.getSelectedButton().getNoteLength());
		}
		updateNoteWindow();
	}

	public void generateMidi() {
		try {
			MidiFile myFile = new MidiFile();
			MidiTrack myTrack = new MidiTrack();
			Tempo t = new Tempo();
		    t.setBpm(120);
		    myTrack.insertEvent(t);
	    	int durationSum = 0;
	    	int period = Math.round(60000 / t.getBpm()); // BPM to ms
	    	for (Note note : noteList) {
	    		int noteLength = period; // crochet
	    		switch (note.getName()) {
	    			case Note.SEMIBREVE: noteLength *= 4; break;
	    			case Note.MINIM: noteLength *= 2; break;
	    			case Note.QUAVER: noteLength /= 2; break;
	    			case Note.SEMIQUAVER: noteLength /= 4; break;
	    		}
	    		myTrack.insertNote(0, NOTE_PITCH[note.getPitch()], 120, durationSum, noteLength); // velocity: volume
	    		durationSum += noteLength;
	    	}
	    	/*for (int i = 0; i < NOTE_PITCH.length; i++) {
	    		
	    		myTrack.insertNote(0, NOTE_PITCH[i], 120, delay, duration);
	    		delay += duration;
	    	}*/
	    	/*NoteOn no = new NoteOn(delay, 0, 60, 100);
    		myTrack.insertEvent(no);
    		NoteOff noff = new NoteOff(delay+duration, 0, 60, 100);
    		myTrack.insertEvent(noff);
    		
    		NoteOn no1 = new NoteOn(duration + 1500, 0, 60, 100);
    		myTrack.insertEvent(no1);
    		NoteOff noff1 = new NoteOff(duration+duration+1500+duration, 0, 60, 100);
    		myTrack.insertEvent(noff1);
	    	/*for (int i : notes) {
	    		NoteOn no = new NoteOn(delay, 0, i, 100);
	    		myTrack.insertEvent(no);
	    		NoteOff noff = new NoteOff(delay+duration, 0, i, 100);
	    		myTrack.insertEvent(noff);
	    		//myTrack.insertNote(0, i, 100, delay, duration);
	    	    delay += duration;
	    	}*/
			myFile.addTrack(myTrack);
			FileHandle fh = Gdx.files.external("feelTheRhythmSounds");
			fh.mkdirs();
			fh = Gdx.files.external("feelTheRhythmSounds/myMidi.mid");
			if (fh.exists()) {
				fh.delete();
			}
			fh.file().createNewFile();
			System.out.println(fh.file().getAbsolutePath().toString());
			myFile.writeToFile(fh.file());
			System.out.print(myTrack.getEvents().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
