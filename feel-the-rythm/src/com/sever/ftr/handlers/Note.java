package com.sever.ftr.handlers;

public class Note implements Comparable<Note> {

	/* Note pitch: C D E F G A H C D E F G */
	public static final Integer[] NOTE_PITCH = {60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79};

	public static final int SEMIBREVE = 0;
	public static final int MINIM = 1;
	public static final int CROCHET = 2;
	public static final int QUAVER = 3;
	public static final int SEMIQUAVER = 4;
	
	private int pitch; // index of NOTE_PITCH array
	private int length;

	private String type; // Look at NoteButtonHandler constants
	
	public Note(int length, int pitch, String type) {
		this.pitch = pitch;
		this.length = length;
		this.type = type;
	}
	
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getPitch() {
		return pitch;
	}

	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public int compareTo(Note note) {
		if (this.length < note.length) {
			return -1;
		} else if (this.length > note.length) {
			return 1;
		}
		if (this.pitch < note.pitch) {
			return -1;
		} else if (this.pitch > note.pitch) {
			return 1;
		}
		if (this.type.equals(note.type)) {
			return 0;
		}
		
		return -1;
	}
}
