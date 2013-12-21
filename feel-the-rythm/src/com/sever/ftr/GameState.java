package com.sever.ftr;

import java.util.ArrayList;

import com.sever.ftr.handlers.Note;

/**
 * Singleton lass for holding game state data. State data: current path of midi playing,
 * solution for current midi, users solution,
 * 
 * @author Nejc Sever
 */
public class GameState {
	
	private String storageType;
	private String currentMidiPath;
	private ArrayList<Note> solution;
	private ArrayList<Note> usersSolution;
	
	public GameState() {
	}
	
	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getCurrentMidiPath() {
		return currentMidiPath;
	}

	public void setCurrentMidiPath(String currentMidiPath) {
		this.currentMidiPath = currentMidiPath;
	}

	public ArrayList<Note> getSolution() {
		return solution;
	}

	public void setSolution(ArrayList<Note> solution) {
		this.solution = solution;
	}

	public ArrayList<Note> getUsersSolution() {
		return usersSolution;
	}

	public void setUsersSolution(ArrayList<Note> usersSolution) {
		this.usersSolution = usersSolution;
	}
}
