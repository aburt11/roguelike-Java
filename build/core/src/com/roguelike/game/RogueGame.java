package com.roguelike.game;


import com.badlogic.gdx.Game;
import com.roguelike.game.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RogueGame extends Game {

	public SpriteBatch batch;

	public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Rogue Game";

    public static final float PPM = 100;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

        setScreen(new GameScreen(this));
	}

    @Override
    public void dispose(){
        super.dispose();
        batch.dispose();

    }

	@Override
	public void render () {
		super.render();
	}
}
