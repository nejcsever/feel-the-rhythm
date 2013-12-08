package com.sever.ftr;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sever.ftr.interfaces.DesktopMidiPlayer;
import com.sever.ftr.interfaces.MidiPlayer;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Feel the Rhythm";
		cfg.useGL20 = true;
		cfg.width = 900;
		cfg.height = 600;
		MidiPlayer midiPlayer = new DesktopMidiPlayer();

		new LwjglApplication(new FTRGame(midiPlayer), cfg);
	}
}
