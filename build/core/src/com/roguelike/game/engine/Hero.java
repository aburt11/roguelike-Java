package com.roguelike.game.engine;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.roguelike.game.RogueGame;
import com.roguelike.game.entities.enemies.Enemy;
import com.roguelike.game.entities.guns.gun;
import com.roguelike.game.screens.GameScreen;

import java.util.Random;

/**
 * Created by adam on 9/11/15.
 */
public class Hero extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private GameScreen screen;

    private TextureRegion heroIdle;
    private Animation heroRunAnim;
    private Animation heroJumpAnim;

    private float stateTimer;

    private boolean canjump = true;
    public boolean facingRight = true;

    private float bulletTimer = 0.2f;
    private float buletTimerTime = 0.2f;

    //game mechanic variables
    public float health;
    public float experience;
    public int score;

    public gun gun;

    private int xRegion = 164;
    private int yRegion = 2;

    //GUI Settings
    public float GUIRed = 0.0f;
    public float GUIGreen = 0.7f;
    public float GUIBlue = 0.0f;
    public float GUIAlpha = 0.6f;
    public boolean GUIChanged = false;

    Random damageCalc;
    public boolean isDead = false;

    public Hero(GameScreen screen){
        super(screen.getAtlas().findRegion("herosheet"));

        damageCalc = new Random();
        //set mechanic vars
        score = 0;
        health = 100;
        experience = 0;


        this.screen = screen;
        this.world = screen.getWorld();

        //get idle regions
        heroIdle = new TextureRegion(getTexture(),xRegion,yRegion,32,32);
        setBounds(0, 0, 32 / RogueGame.PPM, 32 / RogueGame.PPM);
        setRegion(heroIdle);

        //set state
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(),i * 32 +xRegion, 0 + yRegion, 32,32));
        heroRunAnim = new Animation(0.1f,frames);
        frames.clear();
        //jump animation
        frames.add(new TextureRegion(getTexture(),3 * 32 +xRegion,0 +yRegion,32,32));
        heroJumpAnim = new Animation(0.1f,frames);
        frames.clear();



        definePhysics();

        gun = new gun(this,screen);


    }

    public void update(float delta){

        if(health <=0)
        {
            isDead = true;
        }

        //set sprite position
        setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2)+8/RogueGame.PPM);
        setRegion(getFrame(delta));
        gun.update();

        bulletTimer -= screen.deltaTime;

    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = heroJumpAnim.getKeyFrame(stateTimer,true);
                break;
            case RUNNING:
                region = heroRunAnim.getKeyFrame(stateTimer,true);
                break;

            case FALLING:
            case STANDING:
            default:
                region = heroIdle;
                break;


        }

        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX() )
        {
            region.flip(true,false);
            if(facingRight) {
                gun.flip(true, false);
                gun.gunPosX -= 3;
                }
            facingRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX() )
        {
            region.flip(true,false);
            if(!facingRight) {


                gun.flip(true, false);
                gun.gunPosX += 3;
            }
            facingRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;

    }

    public State getState(){
        //check the state of the hero
        if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }


    public void jump(){
        if(canjump) {
            b2body.applyLinearImpulse(new Vector2(0, 4), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void dodge()
    {

    }

    public void fire() {
        if(bulletTimer <0 ) {
            screen.heroBullets.add(new herobullet(this,screen, b2body.getPosition().x -(2/RogueGame.PPM), b2body.getPosition().y, facingRight ? true : false));
            bulletTimer = buletTimerTime;
        }


    }

    private void definePhysics()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / RogueGame.PPM, 32 / RogueGame.PPM);
        bdef.position.set(5,15);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9 / RogueGame.PPM);
        fdef.filter.categoryBits = RogueGame.HERO_BIT;
        //fdef.filter.maskBits = RogueGame.GROUND_BIT |
        //        RogueGame.ENEMY_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void onHit(enemybullet bullet)
    {
        if(!isDead) {
            float bulletDamage = bullet.enemy.Damage + (bullet.enemy.DamageRange * damageCalc.nextFloat());
            health -= bulletDamage;
            bullet.setToDestroy();
        }

    }

}
