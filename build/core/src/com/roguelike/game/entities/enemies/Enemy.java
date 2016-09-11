package com.roguelike.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.roguelike.game.engine.Hero;
import com.roguelike.game.engine.herobullet;
import com.roguelike.game.screens.GameScreen;

import java.util.Random;

/**
 * Created by adam on 11/12/15.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected GameScreen screen;
    public Body b2body;
    public boolean isTargeted = false;
    public String EnemyName = "Enemy";

    Random damageCalc;

    //stats
    //health
    public float Health = 100.0f;
    //experience earned when killed
    public int expEarned = 10;
    //base damage it does on hit of hero
    public int Damage = 5;
    //range of damage it can do from base so hit = Damage + random number in range
    public int DamageRange = 3;
    //speed that enemy moves
    public float speed = 1.0f;

    //enemy type denotes types of items dropped and stat mods
    enum enemyType{
        COMMON,
        UNCOMMON,
        SPECIALIST,
        HEROIC,
        LEGENDARY
    }




    public Enemy(GameScreen screen, float x, float y){
        damageCalc = new Random();
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        b2body.setActive(true);
    }

    protected abstract void defineEnemy();
    public abstract void setTarget();
    public abstract void update(float dt);
    public abstract void onHit(herobullet bullet);

}
