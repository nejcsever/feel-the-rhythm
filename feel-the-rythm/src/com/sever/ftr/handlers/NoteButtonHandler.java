package com.sever.ftr.handlers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NoteButtonHandler {
	private static final String PAUSE = "pause";
	private static final String DOT = "dot";
	private static final String NOTE = "note";
	
	private static NoteButton selectedButton;
	private static String noteType = NOTE;
	
	public NoteButtonHandler(WidgetGroup widgetGroup, Skin skin) {
		final ResizableImage pauseButton = new ResizableImage(skin.getDrawable("pause-button"), skin.getDrawable("pause-button-down"),0.325f, 0.05f, 0.15f, ResizableImage.BOTTOM_LEFT);
		final ResizableImage dotButton = new ResizableImage(skin.getDrawable("dot-button"), skin.getDrawable("dot-button-down"),0.2f, 0.05f, 0.15f, ResizableImage.BOTTOM_LEFT);
		pauseButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				dotButton.drawOriginalImage();
				pauseButton.toggleImage();
				if (noteType == PAUSE) {
					noteType = NOTE;
				} else {
					noteType = PAUSE;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		dotButton.addListener(new ClickListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				pauseButton.drawOriginalImage();
				dotButton.toggleImage();
				if (noteType == DOT) {
					noteType = NOTE;
				} else {
					noteType = DOT;
				}
				return super.touchDown(event, x, y, pointer, button);
			}
	    });
		
		
		NoteButton semibreveButton = new NoteButton("semibreve", new ResizableImage(skin.getDrawable("semibreve-button"), skin.getDrawable("semibreve-button-down"),0.05f, 0.95f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton minimButton = new NoteButton("minimButton", new ResizableImage(skin.getDrawable("minim-button"), skin.getDrawable("minim-button-down"),0.05f, 0.7625f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton crotchetButton = new NoteButton("crochetButton", new ResizableImage(skin.getDrawable("crotchet-button"), skin.getDrawable("crotchet-button-down"),0.05f, 0.575f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton quaverButton = new NoteButton("quaverButton", new ResizableImage(skin.getDrawable("quaver-button"), skin.getDrawable("quaver-button-down"),0.05f, 0.3875f, 0.15f, ResizableImage.TOP_LEFT));
		NoteButton semiquaverButton = new NoteButton("semiquaver", new ResizableImage(skin.getDrawable("semiquaver-button"), skin.getDrawable("semiquaver-button-down"),0.05f, 0.2f, 0.15f, ResizableImage.TOP_LEFT));
		
		setSelectedButton(crotchetButton);
		
		widgetGroup.addActor(pauseButton);
		widgetGroup.addActor(dotButton);
		widgetGroup.addActor(semibreveButton.image);
		widgetGroup.addActor(minimButton.image);
		widgetGroup.addActor(crotchetButton.image);
		widgetGroup.addActor(quaverButton.image);
		widgetGroup.addActor(semiquaverButton.image);
	}
	
	private static void setSelectedButton(NoteButton button) {
		if (selectedButton != null) {
			selectedButton.image.drawOriginalImage();
		}
		button.image.drawImageDown();
		selectedButton = button;
	}
	
	private class NoteButton extends ClickListener {
		private String name;
		private ResizableImage image;

		public NoteButton(String name, ResizableImage image) {
			this.name = name;
			this.image = image;
			this.image.addListener(this);
		}

		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			setSelectedButton(this);
			return super.touchDown(event, x, y, pointer, button);
		}
	}
}
