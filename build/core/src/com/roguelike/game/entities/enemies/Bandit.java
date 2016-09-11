package com.roguelike.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.roguelike.game.RogueGame;
import com.roguelike.game.engine.Hero;
import com.roguelike.game.engine.enemybullet;
import com.roguelike.game.engine.herobullet;
import com.roguelike.game.screens.GameScreen;

import javax.xml.soap.Text;

/**
 * Created by adam on 11/12/15.
 */
public class Bandit extends Enemy {

    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;

    private float stateTime;
    private Animation idleAnimation;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean facingRight = false;
    private int viewDistance = 12;

    private float bulletTimer = 0.4f;
    private float bulletTimerTime = 0.4f;


    public Bandit(GameScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_bandit"),0, 0, 32, 32));
        idleAnimation = new Animation(0.4f,frames);
        frames.clear();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_bandit"), i * 32, 0, 32, 32));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 32 / RogueGame.PPM, 32 / RogueGame.PPM);
        setToDestroy = false;
        destroyed = false;
        EnemyName = "Bandit";

    }

    public void update(float dt){
        stateTime += dt;
        if(Health <=0)
            setToDestroy = true;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed) {


            setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2) + 6 / RogueGame.PPM);
            setRegion(getFrame(dt));
            StepAI();

            if(bulletTimer > 0)
            bulletTimer -= dt;

        }
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case RUNNING:
                region = walkAnimation.getKeyFrame(stateTime,true);
                break;

            case FALLING:
            case STANDING:
            default:
                region = idleAnimation.getKeyFrame(stateTime,false);
                break;


        }

        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX() )
        {
            region.flip(true,false);
            facingRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX() )
        {
            region.flip(true,false);
            facingRight = true;
        }

        stateTime = currentState == previousState ? stateTime + delta : 0;
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

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9 / RogueGame.PPM);
        fdef.filter.categoryBits = RogueGame.ENEMY_BIT;
        //fdef.filter.maskBits = RogueGame.ENEMY_BULLET_BIT |
        //        RogueGame.HERO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    public void StepAI()
    {
        boolean enemyInSight = false;

        Hero hero = screen.getHero();
        //go left
        if(hero.b2body.getPosition().x - b2body.getPosition().x < viewDistance){
            if(hero.b2body.getPosition().x - b2body.getPosition().x >(90/RogueGame.PPM)){
                b2body.applyLinearImpulse(new Vector2(0.04f *speed, 0), b2body.getWorldCenter(), true);

            }
            //fire at hero
            fire();


        }

       // go right
        if( b2body.getPosition().x - hero.b2body.getPosition().x < viewDistance){

            if(b2body.getPosition().x - hero.b2body.getPosition().x > (90/RogueGame.PPM)){
                b2body.applyLinearImpulse(new Vector2(-0.04f *speed, 0), b2body.getWorldCenter(), true);

            }

            //fire at hero
            fire();

        }

    }


public void fire()
{
    if(bulletTimer < 0)
    {
        screen.enemybullets.add(new enemybullet(this,screen, b2body.getPosition().x, b2body.getPosition().y, facingRight ? true : false));

        bulletTimer = bulletTimerTime;
    }



}

    //untarget all enemies except this one
    @Override
    public void setTarget() {
        for(Enemy enemies : screen.Enemies){
            enemies.isTargeted = false;
        }

        this.isTargeted = true;

    }

    @Override
    public void onHit(herobullet bullet) {
        setTarget();
        //take damage

        float bulletDamage = bullet.hero.gun.strength + (bullet.hero.gun.range* damageCalc.nextFloat());
        Health -= bullet.hero.gun.strength;
        bullet.setToDestroy();

    }
}
