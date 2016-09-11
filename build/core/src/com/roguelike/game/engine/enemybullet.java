package com.roguelike.game.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.roguelike.game.RogueGame;
import com.roguelike.game.entities.enemies.Enemy;
import com.roguelike.game.screens.GameScreen;

/**
 * Created by adam on 10/12/15.
 */
public class enemybullet extends Sprite {

    GameScreen screen;
    World world;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;
    public Enemy enemy;
    Body b2body;

    private float timer = 1.0f;

    public enemybullet(Enemy enemy, GameScreen screen, float x, float y, boolean fireRight){
        super(screen.getAtlas().findRegion("bullet"));
        this.enemy = enemy;
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();


        //create sprite and physics object

        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(new Vector2(enemy.b2body.getPosition().x, enemy.b2body.getPosition().y + 0/ RogueGame.PPM));
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2 / RogueGame.PPM, 2 / RogueGame.PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 0.01f;
        fdef.density = 0.01f;
        fdef.filter.categoryBits = RogueGame.ENEMY_BULLET_BIT;
        //fdef.filter.maskBits =  RogueGame.HERO_BIT;


        this.b2body = world.createBody(bdef);
        this.b2body.createFixture(fdef).setUserData(this);

        b2body.setLinearVelocity(new Vector2(fireRight ? 20 : -20, 1));
        //get bullet
        setBounds(0, 0, 32 / RogueGame.PPM, 32 / RogueGame.PPM);

    }

    public void update(float dt){
        stateTime += dt;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 0.5 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }

        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}

