package com.sever.ftr.handlers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NoteButtonHandler extends WidgetGroup {
	public static final String PAUSE = "pause";
	public static final String REMOVE = "remove";
	public static final String NOTE = "note";
	
	private static NoteButton selectedButton;
	private static String noteType = NOTE;
	
	public NoteButtonHandler(Skin skin) {
		final ResizableImage pauseButton = new ResizableImage(skin.getDrawable("pause-button"), skin.getDrawable("pause-button-down"),0.25f, 0.05f, 0.15f, ResizableImage.BOTTOM_LEFT);
		final ResizableImage removeButton = new ResizableImage(skin.getDrawable("dot-button"), skin.getDrawable("dot-button-down"),0.4f, 0.05f, 0.15f, ResizableImage.BOTTOM_LEFT);
		pauseButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				removeButton.drawOriginalImage();
				pauseButton.toggleImage();
				if (noteType == PAUSE) {
					noteType = NOTE;
				} else {
					noteType = PAUSE;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		removeButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				pauseButton.drawOriginalImage();
				removeButton.toggleImage();
				if (noteType == REMOVE) {
					noteType = NOTE;
				} else {
					noteType = REMOVE;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		
		
		NoteButton semibreveButton = new NoteButton(Note.SEMIBREVE, new ResizableImage(skin.getDrawable("semibreve-button"), skin.getDrawable("semibreve-button-down"),0.05f, 0.95f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton minimButton = new NoteButton(Note.MINIM, new ResizableImage(skin.getDrawable("minim-button"), skin.getDrawable("minim-button-down"),0.05f, 0.7625f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton crotchetButton = new NoteButton(Note.CROCHET, new ResizableImage(skin.getDrawable("crotchet-button"), skin.getDrawable("crotchet-button-down"),0.05f, 0.575f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton quaverButton = new NoteButton(Note.QUAVER, new ResizableImage(skin.getDrawable("quaver-button"), skin.getDrawable("quaver-button-down"),0.05f, 0.3875f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton semiquaverButton = new NoteButton(Note.SEMIQUAVER, new ResizableImage(skin.getDrawable("semiquaver-button"), skin.getDrawable("semiquaver-button-down"),0.05f, 0.2f, 0.15f, ResizableImage.TOP_LEFT));
		
		setSelectedButton(crotchetButton);
		
		this.addActor(pauseButton);
		this.addActor(removeButton);
		this.addActor(semibreveButton.image);
		this.addActor(minimButton.image);
		this.addActor(crotchetButton.image);
		this.addActor(quaverButton.image);
		this.addActor(semiquaverButton.image);
	}
	
	private static void setSelectedButton(NoteButton button) {
		if (selectedButton != null) {
			selectedButton.image.drawOriginalImage();
		}
		button.image.drawImageDown();
		selectedButton = button;
	}
	
	
	public NoteButton getSelectedButton() {
		return selectedButton;
	}
	
	public String getNoteType() {
		return noteType;
	}
	
	public class NoteButton extends ClickListener {
		private int noteLength;
		private ResizableImage image;

		public NoteButton(int noteLength, ResizableImage image) {
			this.noteLength = noteLength;
			this.image = image;
			this.image.addListener(this);
		}

		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			setSelectedButton(this);
			return super.touchDown(event, x, y, pointer, button);
		}
		
		public int getNoteLength() {
			return noteLength;
		}
	}
}
