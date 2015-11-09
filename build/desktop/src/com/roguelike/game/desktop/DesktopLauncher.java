package com.roguelike.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.roguelike.game.RogueGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.title = RogueGame.TITLE;
		config.width = RogueGame.WIDTH;
		config.height = RogueGame.HEIGHT;

		new LwjglApplication(new RogueGame(), config);
	}
}
