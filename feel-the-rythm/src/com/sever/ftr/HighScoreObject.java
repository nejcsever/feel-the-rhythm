package com.sever.ftr;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class HighScoreObject implements Serializable {
	private Map<String, Integer> levels;

	public HighScoreObject() {
		levels = new HashMap<String, Integer>();
	}

	public Map<String, Integer> getLevels() {
		return levels;
	}

	public void addScore(String level, int score) {
		levels.put(level, score);
	}

	@Override
	public void write(Json json) {
		json.writeValue("levels", levels);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		levels = json.readValue("levels", HashMap.class,Integer.class, jsonData);
	}
}