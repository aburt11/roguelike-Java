package com.roguelike.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roguelike.game.RogueGame;
import com.roguelike.game.engine.HUD;
import com.roguelike.game.engine.Hero;
import com.roguelike.game.engine.WorldContactListener;
import com.roguelike.game.engine.enemybullet;
import com.roguelike.game.engine.engine3d.SceneManager3D;
import com.roguelike.game.engine.engine3d.c3DEntity;
import com.roguelike.game.engine.herobullet;
import com.roguelike.game.entities.enemies.Bandit;
import com.roguelike.game.entities.enemies.Enemy;

/**
 * Created by adam on 8/11/15.
 */
public class GameScreen implements Screen {

    boolean is3DScene = false;

    public SpriteBatch sb;
    private RogueGame game;
    public OrthographicCamera cam;
    public OrthographicCamera hudcam;
    public Viewport view;

    //box2d world variables
    public World world;
    public Box2DDebugRenderer b2dr;

    //texture atlas
    private TextureAtlas atlas;

    //hero vars
    private Hero player;


    public float deltaTime = 0;

    //hud var
    private HUD hud;

    //tile map deets
    private TmxMapLoader maploader;
    private TiledMap map;
    private String mapFile = "test.tmx";
    private OrthogonalTiledMapRenderer renderer;


    //game based variables
    private float jumpTimer = 1f;
    private float dodgeTimer = 1f;


    //environment Background
   // private EnviromentBG background;


    //arrays
    public Array<herobullet> heroBullets;
    public Array<enemybullet> enemybullets;
    public Array<Enemy> Enemies;


    public GameScreen(RogueGame game) {


        if(!is3DScene) {
            atlas = new TextureAtlas("sprites/sprites.atlas");

            //init arrays
            heroBullets = new Array<herobullet>();
            Enemies = new Array<Enemy>();
            enemybullets = new Array<enemybullet>();

        }
        else{


        }


        this.game = game;


        if(!is3DScene) {
        sb = game.batch;


            //init game cam
            cam = new OrthographicCamera(RogueGame.WIDTH, RogueGame.HEIGHT);

            //init viewport
            view = new FitViewport(RogueGame.WIDTH / RogueGame.PPM, RogueGame.HEIGHT / RogueGame.PPM, cam);


            //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
            world = new World(new Vector2(0, -10), true);
            //allows for debug lines of our box2d world.
            b2dr = new Box2DDebugRenderer();

            maploader = new TmxMapLoader();
            map = maploader.load(mapFile);


            renderer = new OrthogonalTiledMapRenderer(map, 1 / RogueGame.PPM);

            //set camera to viewport
            cam.position.set(view.getWorldWidth() / 2, view.getWorldHeight() / 2, 0);

            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fdef = new FixtureDef();
            Body body;

            //create ground bodies/fixtures
            for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / RogueGame.PPM, (rect.getY() + rect.getHeight() / 2) / RogueGame.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / RogueGame.PPM, rect.getHeight() / 2 / RogueGame.PPM);
                fdef.shape = shape;


                body.createFixture(fdef);

            }

            player = new Hero(this);
            // bg = new EnviromentBG(this);

            //test enemy
            Enemies.add(new Bandit(this, player.b2body.getPosition().x - 5 / RogueGame.PPM, player.b2body.getPosition().y));

        //initialize HUD
        hud = new HUD(game.batch,player,this);


            world.setContactListener(new WorldContactListener());
        }


    }

    public void handleInput(float delta){

        if(!is3DScene) {

            if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
                player.fire();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                if (dodgeTimer < 0) {
                    player.dodge();
                    dodgeTimer = 1;
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);


            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (jumpTimer < 0) {
                    player.jump();

                    jumpTimer = 1f;
                }
            }


            // if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            //    player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            //  if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            //     player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

        }
    }

    public void handleTimers(float delta)
    {
        if(!is3DScene) {
            if (jumpTimer > 0)
                jumpTimer -= delta;

            if (dodgeTimer > 0)
                dodgeTimer -= delta;
        }


    }

    public void update(float delta){
        deltaTime = delta;
        handleTimers(delta);

    handleInput(delta);

        if(!is3DScene) {
            player.update(delta);

            //update arrays
            if (heroBullets != null) {
                for (herobullet bullet : heroBullets) {
                    bullet.update(delta);

                    if (bullet.isDestroyed()) {
                        heroBullets.removeValue(bullet, true);
                    }

                }

            }

            //update enemy bullets
            if (enemybullets != null) {
                for (enemybullet ebullet : enemybullets) {
                    ebullet.update(delta);

                    if (ebullet.isDestroyed()) {
                        enemybullets.removeValue(ebullet, true);
                    }

                }

            }

            if (Enemies != null) {
                for (Enemy enemy : Enemies) {
                    enemy.update(delta);
                }

            }

            //sync camera to player
            cam.position.x = player.b2body.getPosition().x;
            cam.position.y = player.b2body.getPosition().y;

            //synch BG
            // background.update(cam);

            world.step(1 / 60f, 6, 2);
        }



        if(!is3DScene) {
            //hud update
            hud.update(delta);
            //update our gamecam with correct coordinates after changes
            cam.update();
            //tell our renderer to draw only what our camera can see in our game world.
            renderer.setView(cam);

        }
        else{
        }



    }

    public TextureAtlas getAtlas(){ return atlas; }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        if(!is3DScene) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            //render
            renderer.render();


            b2dr.render(world, cam.combined);

            game.batch.setProjectionMatrix(cam.combined);
            game.batch.begin();
            // background.draw(game.batch);
            player.draw(game.batch);
            player.gun.draw(game.batch);

            //render arrays
            if (heroBullets != null) {
                for (herobullet bullet : heroBullets) {
                    bullet.draw(game.batch);

                }

            }

            if (enemybullets != null) {
                for (enemybullet ebullet : enemybullets) {
                    ebullet.draw(game.batch);

                }

            }

            if (Enemies != null) {
                for (Enemy enemy : Enemies) {
                    enemy.draw(game.batch);
                }

            }

            game.batch.end();

            //Set our batch to now draw what the Hud camera sees.
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
        }
        else{


        }




    }

    @Override
    public void resize(int width, int height) {
       if(!is3DScene)
        view.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if(!is3DScene) {
            map.dispose();
            renderer.dispose();
            world.dispose();
            b2dr.dispose();
            hud.dispose();
        }
        else{
            hud.dispose();
        }

    }

    public World getWorld(){
        return world;
    }

    public Hero getHero(){ return player;}

}
