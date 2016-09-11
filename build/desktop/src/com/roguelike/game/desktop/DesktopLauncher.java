package com.roguelike.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.roguelike.game.RogueGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


		config.title = RogueGame.TITLE;
		config.width = RogueGame.WIDTH;
		config.height = RogueGame.HEIGHT;


		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		TexturePacker.process(settings, "spritedata", "sprites", "sprites");

		new LwjglApplication(new RogueGame(), config);
	}
}
