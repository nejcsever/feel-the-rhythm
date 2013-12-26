package com.sever.ftr.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sever.ftr.GameState;


public class SolutionStave extends Stave {

	private static final String WRONG_STRING = "wrong-note";
	protected Image[] labelWindow;
	private GameState gameState;
	private int numberOfColumns;
	
	public SolutionStave(float x, float y, float height, float width, int numberOfColumns, GameState gameState) {
		super(x, y, height, width, numberOfColumns);
		labelWindow = new Image[numberOfColumns];
		this.gameState = gameState;
		this.numberOfColumns = numberOfColumns;
		/*create empty noteWindow */
		labelWindow = new Image[numberOfColumns];
		for(int i = 0; i < labelWindow.length; i++) {
			labelWindow[i] = new Image();
			this.addActor(labelWindow[i]);
		}
	}

	@Override
	public void updateNoteWindow() {
		super.updateNoteWindow();
		/* Hide right arrow if we come to the end */
		if (noteList.size() - currentColumnPosition <= numberOfColumns) {
			rightArrowButton.setVisible(false);
		} else {
			rightArrowButton.setVisible(true);
		}
		/* Update notes */
		for (int i = 0; i < labelWindow.length; i++) {
			/*If noteList is not shorter then sum of values*/
			if (i + currentColumnPosition >= noteList.size()) {
				labelWindow[i].setVisible(false);
				continue;
			}

			Note currentNote = noteList.get(i + currentColumnPosition);
			if (currentNote == null) {
				continue;
			}
			labelWindow[i].setVisible(true);
			/* Check right and wrong notes */
			if ((i + currentColumnPosition) < gameState.getSolution().size() && currentNote.compareTo(gameState.getSolution().get(i + currentColumnPosition)) == 0) {
				labelWindow[i].setVisible(false);
			} else {
				labelWindow[i].setDrawable(skin.getDrawable(WRONG_STRING));
			}
			float windowHeight = Gdx.graphics.getHeight();
			float windowWidth = Gdx.graphics.getWidth();
			labelWindow[i].setHeight(windowHeight * height * (1 + rowHeightPercentage*2));
			labelWindow[i].setWidth(columnWidthPercentage * windowWidth * width);
			labelWindow[i].setY(windowHeight * y - rowHeightPercentage * windowHeight * height);
			labelWindow[i].setX(windowWidth * (x + width * (i+frontPadding * 0.1f)*columnWidthPercentage));
		}
	}
}
