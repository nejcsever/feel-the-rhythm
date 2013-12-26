package com.sever.ftr.handlers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class StaveDragListener extends DragListener {
	
	/* Index will be calculated on touch. Indexes present finger position on stave (in stave matrix - stave rows and columns). */
	/* Used for dragging. When user drags note - only one note will be effected. */
	private int currentXIndex;
	
	private Stave stave;
	
	public StaveDragListener(Stave stave) {
		this.stave = stave;
		currentXIndex = 0;
	}
	
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		/* Don't handle touch if arrows are pressed. */
		String targetName = event.getTarget().getName();
		if (targetName == null || !(targetName.equals(Stave.LEFT_ARROW_NAME) || targetName.equals(Stave.RIGHT_ARROW_NAME))) {
			currentXIndex = calculateXIndex(x, y);
			int yIndex = calculateYIndex(x, y);
			handleStaveTouch(yIndex);
		}
		return super.touchDown(event, x, y, pointer, button);
	}

	public void touchDragged(InputEvent event, float x, float y,
			int pointer) {
		/* Don't drag if pause or remove are selected */
		String noteType = stave.getNoteButtonHandler().getNoteType();
		if (noteType.equals(NoteButtonHandler.PAUSE) || noteType.equals(NoteButtonHandler.REMOVE)) {
			return;
		}
		/* Don't handle touch if arrows are pressed. */
		String targetName = event.getTarget().getName();
		if (targetName == null || !(targetName.equals(Stave.LEFT_ARROW_NAME) || targetName.equals(Stave.RIGHT_ARROW_NAME))) {
			int yIndex = calculateYIndex(x, y);
			dragNote(yIndex);
		}
	}
	
	private int calculateXIndex(float x, float y) {
		float widthPx = stave.getWidth() * Gdx.graphics.getWidth();
		float xPx = stave.getX() * Gdx.graphics.getWidth();
		xPx -= xPx*stave.getFrontPadding()*stave.getColumnWidthPercentage()*2; // TODO make this better :)
		int horizontalIndex = Math.round((((x - xPx) / widthPx)/(stave.getColumnWidthPercentage()))) - 1;
		if (horizontalIndex < 0) {
			horizontalIndex = 0;
		}
		return horizontalIndex;
	}
	
	private int calculateYIndex(float x, float y) {
		float heightPx = stave.getHeight() * Gdx.graphics.getHeight();
		float yPx = stave.getY() * Gdx.graphics.getHeight();
		int verticalIndex = Math.round(((y - yPx) / heightPx)/stave.getRowHeightPercentage());
		if (verticalIndex > Stave.NUMBER_OF_ROWS) {
			verticalIndex = Stave.NUMBER_OF_ROWS;
		} else if (verticalIndex < 0) { // this may happen if touch is recieved on note object and not from background so it must be disabled
			verticalIndex = 0;
		}
		return verticalIndex;
	}
	
	private void handleStaveTouch(int verticalIndex) {
		/* Add note or edit note */
		NoteButtonHandler nbh = stave.getNoteButtonHandler();
		int currColPos = stave.getCurrentColumnPosition();
		ArrayList<Note> noteList = stave.getNoteList();
		String noteType = nbh.getNoteType();
		if (nbh.getNoteType().equals(NoteButtonHandler.REMOVE)){
			stave.removeNote(currentXIndex + currColPos);
		} else if (currentXIndex + currColPos >= noteList.size()) {
			stave.addNote(new Note(nbh.getSelectedButton().getNoteLength(), verticalIndex, noteType));
			currentXIndex = noteList.size() - currColPos - 1; // when note is added it's also draggable
		} else {
			stave.updateNote(noteList.get(currentXIndex + currColPos), verticalIndex, noteType, nbh.getSelectedButton().getNoteLength());
		}
	}
	
	private void dragNote(int verticalIndex) {
		/* Add note or edit note */
		NoteButtonHandler nbh = stave.getNoteButtonHandler();
		int currColPos = stave.getCurrentColumnPosition();
		ArrayList<Note> noteList = stave.getNoteList();
		String noteType = nbh.getNoteType();
		if (nbh.getNoteType().equals(NoteButtonHandler.REMOVE)){
			return; // skip drag
		} else {
			stave.updateNote(noteList.get(currentXIndex + currColPos), verticalIndex, noteType, nbh.getSelectedButton().getNoteLength());
		}
	}
}
