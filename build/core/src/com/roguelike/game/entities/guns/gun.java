package com.roguelike.game.entities.guns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.roguelike.game.RogueGame;
import com.roguelike.game.engine.Hero;
import com.roguelike.game.screens.GameScreen;

/**
 * Created by adam on 5/12/15.
 */
public class gun extends Sprite {

    private int gunID = 1;
    private Hero hero;
    private GameScreen screen;
    private TextureRegion currentGun;
    public int gunPosX = 2;


    public float strength = 6.0f;
    public float range = 5;
    public float rateOfFire = 2; //how many rounds per second

    public gun(Hero hero, GameScreen screen)
    {
        super(screen.getAtlas().findRegion("gun"));

        this.hero = hero;
        this.screen = screen;

        //get gun
        setBounds(0, 0, 32 / RogueGame.PPM, 32 / RogueGame.PPM);



    }

    public void update() {

        setPosition((hero.b2body.getPosition().x - getWidth() / 2)+gunPosX/RogueGame.PPM, (hero.b2body.getPosition().y - getHeight() / 2)+5/RogueGame.PPM);

    }


}
