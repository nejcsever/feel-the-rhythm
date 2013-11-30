package com.sever.ftr.handlers;

public class Note {

	public static final String SEMIBREVE = "semibreve";
	public static final String MINIM = "minim";
	public static final String CROCHET = "crochet";
	public static final String QUAVER = "quaver";
	public static final String SEMIQUAVER = "semiquaver";
	
	private int pitch;
	private String name;
	
	public Note(String name, int pitch) {
		this.pitch = pitch;
		this.name = name;
	}
	
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getPitch() {
		return pitch;
	}

	public String getName() {
		return name;
	}
}
