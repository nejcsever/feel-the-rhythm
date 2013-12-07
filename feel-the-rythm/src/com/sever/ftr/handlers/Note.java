package com.sever.ftr.handlers;

public class Note {

	public static final int SEMIBREVE = 0;
	public static final int MINIM = 1;
	public static final int CROCHET = 2;
	public static final int QUAVER = 3;
	public static final int SEMIQUAVER = 4;
	
	private int pitch;
	private int name;

	private String type; // Look at NoteButtonHandler constants
	
	public Note(int name, int pitch, String type) {
		this.pitch = pitch;
		this.name = name;
		this.type = type;
	}
	
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getPitch() {
		return pitch;
	}

	public int getName() {
		return name;
	}
	
	public void setName(int name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
