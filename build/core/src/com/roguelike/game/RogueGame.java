package com.roguelike.game;


import com.badlogic.gdx.Game;
import com.roguelike.game.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RogueGame extends Game {

	public SpriteBatch batch;

	public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final String TITLE = "Rogue Game";


    //Box2D Collision Bits
    public static final short DEFAULT_BIT = 1;
    public static final short HERO_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short HERO_BULLET_BIT = 8;
    public static final short ENEMY_BULLET_BIT = 16;
    public static final short ITEM_BIT = 32;

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
