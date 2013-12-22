package com.sever.ftr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

/**
 * Highscore storing - highscores are stored in JSON encoded with base64.
 * @author Nejc Sever
 */
public class HighScore {
	
	private static final String FILE_PATH = "highscores/highscore.json";
	
	/* So that we don't have to access files on every read operation. */
	/* Can be null! */
	private static HighScoreObject highscoreObject;
	
	private static HighScoreObject retrieveHighscoreObject() {
		if (highscoreObject != null)
			return highscoreObject;
				
		/* Create new one */
		FileHandle fh = Gdx.files.local(FILE_PATH);
		if (!fh.exists()) {
			highscoreObject = new HighScoreObject();
			return highscoreObject;
		}
		Json json = new Json();
		String base64Json = fh.readString();
        String text = Base64Coder.decodeString(base64Json);
        highscoreObject = json.fromJson(HighScoreObject.class, text); // save instance
        return highscoreObject;
	}
	
	/**
	 * @return Returns -1 if there's no highscore for given level.
	 */
	public static int readHighScore(String levelName) {
		if (highscoreObject == null) {
			retrieveHighscoreObject();
		}
		int result = -1;
		if (highscoreObject.getLevels().containsKey(levelName)) {
			result = highscoreObject.getLevels().get(levelName);
		}
        return result;
	}
	
	/**
	 * Checks if highscore for given level already exists.
	 * If it exists, then check if given highscore is greater then before.
	 * If it is, highscore is updated.
	 */
	public static void updateHighScore(String levelName, int newScore) {
		Json json = new Json();
		if (highscoreObject == null) {
			retrieveHighscoreObject();
		}
		boolean changed = false;
		if (highscoreObject.getLevels().containsKey(levelName)) {
			if (highscoreObject.getLevels().get(levelName) < newScore) {
				highscoreObject.getLevels().put(levelName, newScore);
				changed = true;
    		}
		} else {
			highscoreObject.getLevels().put(levelName, newScore);
			changed = true;
		}
        
        /* Save to file */
        
        if (changed) {
        	FileHandle fh = Gdx.files.local(FILE_PATH);
	        String jsonText = json.toJson(highscoreObject);
	        fh.writeString(Base64Coder.encodeString(jsonText), false);
        }
	}
	
	public static void removeHighScore(String levelName) {
		
	}
}
