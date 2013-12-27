package com.sever.ftr.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Stave extends WidgetGroup {

	public static final int NUMBER_OF_ROWS = 11;
	protected static final String[] NOTE_DRAWABLE_STRINGS = {"full-note", "empty-stem", "full-stem", "stem-flag", "stem-doubleflag"};
	protected static final String[] NOTE_DRAWABLE_DOWN_STRINGS = {"full-note-down", "empty-stem-down", "full-stem-down", "stem-flag-down", "stem-doubleflag-down"};
	protected static final String[] PAUSE_DRAWABLE_STRINGS = {"full-rest", "full-rest", "quarter-rest", "eighth-rest", "sixteenth-rest"};
	protected static final float[] PAUSE_DRAWABLE_SIZES = {0.1f, 0.1f, 0.7f, 0.35f, 0.52f};
	protected static final float[] PAUSE_DRAWABLE_POSITIONS = {0.725f, 0.54f, 0.55f, 0.35f, 0.165f};
	protected static final int[] PAUSE_DRAWABLE_ORIGINS = {ResizableImage.TOP_CENTER, ResizableImage.BOTTOM_CENTER, ResizableImage.CENTER_CENTER, ResizableImage.BOTTOM_CENTER, ResizableImage.BOTTOM_CENTER};
	
	private static final float ARROW_PADDING = 0.02f;
	private static final float STEM_NOTE_SIZE_PERCENTAGE = 0.7f;
	public static final String LEFT_ARROW_NAME = "right-arrow";
	public static final String RIGHT_ARROW_NAME = "left-arrow";
	
	/* Stave position on the screen in percentage */
	protected float x;
	protected float y;
	/* Stave height and width in percentage */
	protected float height;
	protected float width;
	/* Current position of noteWindow */
	protected int currentColumnPosition = 0;
	protected float columnWidthPercentage;
	protected float rowHeightPercentage = (float) 1 / NUMBER_OF_ROWS;
	private Image background;
	private TextureAtlas atlas;
	protected Skin skin;
	private NoteButtonHandler noteButtonHandler;
	/* Contains list of all notes in stave */
	protected ArrayList<Note> noteList;
	/* Contains ResizableImage's for notes that are currently visible. */
	protected ResizableImage[] noteWindow;
	private Image[] cLines;
	
	private ResizableImage leftArrowButton;
	protected ResizableImage rightArrowButton;

	protected float frontPadding = 0.6f; // paddign for first shown note
	public Stave(float x, float y, float height, float width, int numberOfColumns) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.noteButtonHandler = null;
		this.frontPadding = 0.6f;
		columnWidthPercentage = (float) 1 / numberOfColumns;
		
		atlas = new TextureAtlas(Gdx.files.internal("textures/stave.pack"));
		skin = new Skin(atlas);
		
		background = new Image(skin.getDrawable("background"));
		this.addActor(background);
		
		/* Create empty note list */
		noteList = new ArrayList<Note>();
		
		/* Empty cLines */
		cLines = new Image[numberOfColumns];
		float windowWidth = Gdx.graphics.getWidth();
		float windowHeight = Gdx.graphics.getHeight();
		for(int i = 0; i < cLines.length; i++) {
			cLines[i] = new Image(skin.getDrawable("c-line"));
			cLines[i].setHeight(windowHeight * height * rowHeightPercentage * 0.08f);
			cLines[i].setWidth(columnWidthPercentage * windowWidth * width);
			cLines[i].setY(windowHeight * y);
			cLines[i].setX(windowWidth * (x + width * (i+frontPadding * 0.1f)*columnWidthPercentage));
			cLines[i].setVisible(false);
			this.addActor(cLines[i]);
		}
		/*create empty noteWindow */
		noteWindow = new ResizableImage[numberOfColumns];
		for(int i = 0; i < noteWindow.length; i++) {
			noteWindow[i] = new ResizableImage(0f, ResizableImage.TOP_CENTER);
			this.addActor(noteWindow[i]);
		}
		
		// Stave movement arrows
		leftArrowButton = new ResizableImage(skin.getDrawable("left-arrow"), skin.getDrawable("left-arrow-down"),  x - ARROW_PADDING * width, height*0.55f + y,  height*0.7f, ResizableImage.CENTER_RIGHT);
		leftArrowButton.setName(LEFT_ARROW_NAME); // for handling input
		rightArrowButton = new ResizableImage(skin.getDrawable("right-arrow"), skin.getDrawable("right-arrow-down"), width + x + ARROW_PADDING * width, height*0.55f + y,  height*0.7f, ResizableImage.CENTER_LEFT);
		rightArrowButton.setName(RIGHT_ARROW_NAME); // for handling input
		leftArrowButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				moveLeft();
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
				moveRight();
				((ResizableImage) event.getListenerActor()).drawImageDown();
				return super.touchDown(event, x, y, pointer, button);
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((ResizableImage) event.getListenerActor()).drawOriginalImage();
				super.touchUp(event, x, y, pointer, button);
			}
	    });
		this.addActor(leftArrowButton);
		this.addActor(rightArrowButton);
	}
	public Stave(float x, float y, float height, float width, int numberOfColumns, NoteButtonHandler noteButtonHandler) {
		this(x, y, height, width, numberOfColumns);
		this.noteButtonHandler = noteButtonHandler;
		/* Drag listener */
		this.addListener(new StaveDragListener(this));
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
		leftArrowButton.resize(width, height);
		rightArrowButton.resize(width, height);
	}
	
	public void updateNoteWindow() {
		/* Update arrow buttons */
		if (currentColumnPosition == 0) {
			leftArrowButton.setVisible(false);
		} else {
			leftArrowButton.setVisible(true);
		}
		if (noteList.size() - currentColumnPosition <= 1) {
			rightArrowButton.setVisible(false);
		} else {
			rightArrowButton.setVisible(true);
		}
			
		/* Update notes */
		for (int i = 0; i < noteWindow.length; i++) {
			cLines[i].setVisible(false);
			/*If noteList is not shorter then sum of values*/
			if (i + currentColumnPosition >= noteList.size()) {
				noteWindow[i].setVisible(false);
				continue;
			} else {
				noteWindow[i].setVisible(true);
			}

			Note currentNote = noteList.get(i + currentColumnPosition);
			if (currentNote == null) {
				continue;
			}
			/* Update cLines if note is C */
			if (currentNote.getPitch() == 0 && !currentNote.getType().equals(NoteButtonHandler.PAUSE)) {
				cLines[i].setVisible(true);
			}
			/* Determine which note to display */
			if (currentNote.getType().equals(NoteButtonHandler.PAUSE)){
				noteWindow[i].setImage(skin.getDrawable(PAUSE_DRAWABLE_STRINGS[currentNote.getLength()]));
				noteWindow[i].setHeightPercentage(height * PAUSE_DRAWABLE_SIZES[currentNote.getLength()]);
				noteWindow[i].setOrigin(PAUSE_DRAWABLE_ORIGINS[currentNote.getLength()]);
				noteWindow[i].setyPercentage(height*PAUSE_DRAWABLE_POSITIONS[currentNote.getLength()] + y);
				noteWindow[i].setxPercentage(x + width * (i+frontPadding)*columnWidthPercentage);
				noteWindow[i].drawOriginalImage();
			} else {
				noteWindow[i].setImage(skin.getDrawable(NOTE_DRAWABLE_STRINGS[currentNote.getLength()]));
				noteWindow[i].setImageDown(skin.getDrawable(NOTE_DRAWABLE_DOWN_STRINGS[currentNote.getLength()]));
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
				noteWindow[i].setxPercentage(x + width * (i+frontPadding)*columnWidthPercentage);
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
	
	public void addNote(Note note) {
		noteList.add(note);
		updateNoteWindow();
	}
	
	public void removeNote(int index) {
		if (noteList.size() <= index) {
			return;
		}
		noteList.remove(index);
		updateNoteWindow();
	}
	
	/**
	 * Changes properties of Note object and updates stave screen.
	 */
	public void updateNote(Note note, int pitch, String noteType, int noteLength) {
		note.setPitch(pitch);
		note.setType(noteType);
		note.setLength(noteLength);
		updateNoteWindow();
	}

	public ArrayList<Note> getNoteList() {
		return noteList;
	}
	
	public void dispose() {
		atlas.dispose();
		skin.dispose();
	}
	
	public NoteButtonHandler getNoteButtonHandler() {
		return noteButtonHandler;
	}
	
	public float getFrontPadding() {
		return frontPadding;
	}
	
	public float getRowHeightPercentage() {
		return rowHeightPercentage;
	}
	
	public float getColumnWidthPercentage() {
		return columnWidthPercentage;
	}

	public int getCurrentColumnPosition() {
		return currentColumnPosition;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
}
