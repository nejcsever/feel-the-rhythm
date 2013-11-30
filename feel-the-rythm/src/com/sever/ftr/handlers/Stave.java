package com.sever.ftr.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Stave extends WidgetGroup {

	private static final int NUMBER_OF_COLUMNS = 5;
	
	private float x;
	private float y;
	private float height;
	private float width;
	private int currentColumnPosition;
	private float columnWidthPercentage = (float) 1 / NUMBER_OF_COLUMNS;
	private Image background;
	
	private TextureAtlas atlas;
	private Skin skin;
	
	private ArrayList<Note> noteList;
	private ResizableImage[] noteWindow;
	private HashMap<String, Drawable> noteDrawables;
	
	public Stave(float x, float y, float height, float width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		currentColumnPosition = 0;
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/stave.pack"));
		skin = new Skin(atlas);
		noteDrawables = new HashMap<String, Drawable>(){{
			put(Note.SEMIBREVE, skin.getDrawable("sample-note"));
		}};
		
		System.out.println(height+y);
		
		background = new Image(skin.getDrawable("background"));
		this.addActor(background);
		
		/* Create empty note list */
		noteList = new ArrayList<Note>();
		
		/*create empty noteWindow */
		noteWindow = new ResizableImage[NUMBER_OF_COLUMNS];
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i] = new ResizableImage(this.height*0.17f, ResizableImage.CENTER_CENTER);
			this.addActor(noteWindow[i]);
		}
		addNote(0, new Note(Note.SEMIBREVE, 0));
		addNote(1, new Note(Note.SEMIBREVE, 1));
		addNote(2, new Note(Note.SEMIBREVE, 2));
		addNote(3, new Note(Note.SEMIBREVE, 3));
		addNote(4, new Note(Note.SEMIBREVE, 4));
		addNote(5, new Note(Note.SEMIBREVE, 5));
		addNote(6, new Note(Note.SEMIBREVE, 6));
		addNote(7, new Note(Note.SEMIBREVE, 7));
		addNote(8, new Note(Note.SEMIBREVE, 8));
		addNote(9, new Note(Note.SEMIBREVE, 9));
	}
	
	public void resize(int width, int height) {
		background.setPosition((float)(width * x), (float)(height * (y + this.height*0.2f)));
		background.setSize((float)(width * this.width), (float)(height * (this.height*0.8f)));
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i].resize(width, height);
		}
	}
	
	public void updateNoteWindow() {
		for (int i = 0; i < noteWindow.length; i++) {
			/* If noteList is not shorter then sum of values */
			if (i + currentColumnPosition < noteList.size()) {
				Note tempNote = noteList.get(i + currentColumnPosition);
				System.out.println("i: " +i + "; pitch: " + tempNote.getPitch());
				if (tempNote != null) {
					noteWindow[i].setDrawable(noteDrawables.get(tempNote.getName()));
					noteWindow[i].setyPercentage((height*0.1f*tempNote.getPitch())+y);
					noteWindow[i].setxPercentage(x + width * (i+0.5f)*columnWidthPercentage);
				}
			} else {
				noteWindow[i].resetImage();
			}
		}
		/*skin.getDrawable("sample-note"), x + width * (i+0.5f)*tempPerc, (height*0.1f*i)+y, this.height*0.17f, */
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
}
