package com.sever.ftr.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;

/**
 * Executes transformations between list of Notes and MIDI files.
 * 
 * @author Nejc Sever
 */
public class MidiNoteConverter {
	public static final String INTERNAL_STORAGE = "internal";
	public static final String LOCAL_STORAGE = "local";
	
	/**
	 * Saves list of notes to MIDI file.
	 * @param storageType Look at MidiNoteConverter.java constants
	 */
	public static void saveMidi(ArrayList<Note> noteList, String filePath, String storageType) {
		try {
			MidiFile myFile = new MidiFile();
			MidiTrack myTrack = new MidiTrack();
			Tempo t = new Tempo();
		    t.setBpm(120);
		    myTrack.insertEvent(t);
	    	int durationSum = 0;
	    	int period = Math.round(60000 / t.getBpm()); // BPM to ms
	    	for (Note note : noteList) {
	    		int noteLength = period; // crochet
	    		switch (note.getLength()) {
	    			case Note.SEMIBREVE: noteLength *= 4; break;
	    			case Note.MINIM: noteLength *= 2; break;
	    			case Note.QUAVER: noteLength /= 2; break;
	    			case Note.SEMIQUAVER: noteLength /= 4; break;
	    		}
	    		if (note.getType().equals(NoteButtonHandler.PAUSE)) {
	    			myTrack.insertNote(0, 0, 0, durationSum, noteLength); // velocity: volume
	    		} else {	    			
	    			myTrack.insertNote(0, Note.NOTE_PITCH[note.getPitch()], 120, durationSum, noteLength); // velocity: volume
	    		}
	    		durationSum += noteLength;
	    	}
			myFile.addTrack(myTrack);
			
			FileHandle fh = null;
			if (storageType.equals(INTERNAL_STORAGE))
				fh = Gdx.files.internal(filePath);
			else
				fh = Gdx.files.local(filePath);
			
			fh.mkdirs();
			if (fh.exists()) {
				fh.delete();
			}
			fh.file().createNewFile();
			myFile.writeToFile(fh.file());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retuns list of notes which are generated from MIDI. MIDI must be compatible with game (made from the game editor).
	 */
	public static ArrayList<Note> generateNotesFromMidi(String filePath, String storageType) {
		System.out.println("GENERATE NOTES FROM MIDI" + filePath);
		ArrayList<Note> result = new ArrayList<Note>();
		MidiFile mf;
		try {
			if (storageType.equals(LOCAL_STORAGE))
				mf = new MidiFile(Gdx.files.local(filePath).read());
			else
				mf = new MidiFile(Gdx.files.internal(filePath).read());
			
			MidiTrack mt = mf.getTracks().get(0); // There's only one track by default
			
			float bpm = ((Tempo) mt.getEvents().first()).getBpm(); // First event is always TEMPO! Default in generateMidi method.
			List<Integer> pitchList = Arrays.asList(Note.NOTE_PITCH);
			for(MidiEvent me : mt.getEvents()) {
				if (!(me instanceof NoteOn))
					continue;
				NoteOn noteEvent = ((NoteOn) me);
				if (noteEvent.getDelta() == 0) // Skip notes that present end of previous note
					continue;
				int period = Math.round(60000 / bpm / 4); // BPM to ms divided by 4 - presents SEMIQUAVER(1/16)
				
				int currentNoteLength = Note.SEMIBREVE;
				switch ((int)noteEvent.getDelta() / period) {
	    			case 8: currentNoteLength = Note.MINIM; break;
	    			case 4: currentNoteLength = Note.CROCHET; break;
	    			case 2: currentNoteLength = Note.QUAVER; break;
	    			case 1: currentNoteLength = Note.SEMIQUAVER; break;
				}
				String currentNoteType = NoteButtonHandler.NOTE;
				if (noteEvent.getNoteValue() == 0)
					currentNoteType = NoteButtonHandler.PAUSE;

				result.add(new Note(currentNoteLength, pitchList.indexOf(noteEvent.getNoteValue()), currentNoteType));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns song name from MIDI file name. (Replace "_" with " " and remove .mid extension)
	 */
	public static String convertFileToTitle(String fileName) {
		String result = fileName.replace(".mid", "");
		result = result.replace("_", " ");
		return result;
	}
	
	/**
	 * Returns MIDI file name from song name. (Replace " " with "_", trim and add .mid extension);
	 */
	public static String convertTitleToFile(String title) {
		String result = title.trim().replaceAll(" +", " ");
		result = result.replace(" ", "_");
		result = result.concat(".mid");
		return result;
	}
}
