package com.sever.ftr.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Stave extends WidgetGroup {

	private static final int NUMBER_OF_COLUMNS = 5;
	private static final int NUMBER_OF_ROWS = 11;
	private static final float FRONT_PADDING = 0.5f;
	
	private static final String FULL_NOTE = "full-note";
	private static final String EMPTY_NOTE = "empty-note";
	
	private float x;
	private float y;
	private float height;
	private float width;
	private int currentColumnPosition;
	private float columnWidthPercentage = (float) 1 / NUMBER_OF_COLUMNS;
	private float rowHeightPercentage = (float) 1 / NUMBER_OF_ROWS;
	private Image background;
	
	private TextureAtlas atlas;
	private Skin skin;
	
	private ArrayList<Note> noteList;
	private ResizableImage[] noteWindow;
	
	private NoteButtonHandler noteButtonHandler;
	
	public Stave(float x, float y, float height, float width, NoteButtonHandler noteButtonHandler) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.noteButtonHandler = noteButtonHandler;
		currentColumnPosition = 0;
		
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
			noteWindow[i] = new ResizableImage(this.height*0.15f, ResizableImage.CENTER_CENTER);
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
	
	public void resize(int width, int height) {
		background.setPosition((float)(width * x), (float)(height * y));
		background.setSize((float)(width * this.width), (float)(height * this.height));
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i].resize(width, height);
		}
	}
	
	public void updateNoteWindow() {
		/* determine which note to display */
		int currentNoteLength = noteButtonHandler.getSelectedButton().getNoteLength();
		Drawable currentNoteDrawable = skin.getDrawable(this.FULL_NOTE);
		if (currentNoteLength < 2)
			currentNoteDrawable = skin.getDrawable(this.EMPTY_NOTE);
		
		for (int i = 0; i < noteWindow.length; i++) {
			/* If noteList is not shorter then sum of values */
			if (i + currentColumnPosition < noteList.size()) {
				Note tempNote = noteList.get(i + currentColumnPosition);
				if (tempNote != null) {
					noteWindow[i].setDrawable(currentNoteDrawable);
					noteWindow[i].setyPercentage((height*rowHeightPercentage*tempNote.getPitch())+y);
					noteWindow[i].setxPercentage(x + width * (i+FRONT_PADDING)*columnWidthPercentage);
				}
			} else {
				noteWindow[i].resetImage();
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
		updateNoteWindow();
	}
	
	private void handleStaveTouch(Stave stave, float x, float y) {
		float widthPx = stave.width * Gdx.graphics.getWidth();
		float xPx = stave.x * Gdx.graphics.getWidth();
		int relativeIndex = Math.round((((x - xPx) / widthPx)/columnWidthPercentage) + FRONT_PADDING) - 1;
		
		float heightPx = stave.height * Gdx.graphics.getHeight();
		float yPx = stave.y * Gdx.graphics.getHeight();
		int relativePitch = Math.round(((y - yPx) / heightPx)/rowHeightPercentage);
		if (relativePitch > NUMBER_OF_ROWS) {
			relativePitch = NUMBER_OF_ROWS;
		} else if (relativePitch < 0) { // this may happen if touch is recieved on note object and not from background so it must be disabled
			relativePitch = 0;
		}
		System.out.println(noteButtonHandler.getSelectedButton().getNoteLength());
		if (relativeIndex + currentColumnPosition >= noteList.size()) {
			addNote(noteList.size(), new Note(Note.SEMIBREVE, relativePitch));
		} else {
			noteList.get(relativeIndex + currentColumnPosition).setPitch(relativePitch);
			updateNoteWindow();
		}
	}
}
