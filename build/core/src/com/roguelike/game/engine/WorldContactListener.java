package com.roguelike.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.roguelike.game.RogueGame;
import com.roguelike.game.entities.enemies.Enemy;

/**
 * Created by adam on 15/11/15.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case RogueGame.HERO_BIT | RogueGame.ENEMY_BULLET_BIT:
                if(fixA.getFilterData().categoryBits == RogueGame.HERO_BIT)
                   ((Hero) fixA.getUserData()).onHit((enemybullet) fixB.getUserData());
                else
                    ((Hero) fixB.getUserData()).onHit((enemybullet) fixA.getUserData());
                break;

            case RogueGame.ENEMY_BIT | RogueGame.HERO_BULLET_BIT:
                if(fixA.getFilterData().categoryBits == RogueGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).onHit((herobullet) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).onHit((herobullet) fixA.getUserData());
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
