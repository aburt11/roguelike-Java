package com.roguelike.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roguelike.game.RogueGame;
import com.roguelike.game.engine.TileMap;

/**
 * Created by adam on 8/11/15.
 */
public class GameScreen implements Screen {

    private SpriteBatch sb;
    private RogueGame game;
    private OrthographicCamera cam;
    private OrthographicCamera hudcam;
    private Viewport view;

    //box2d world variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //tile map deets
    public TileMap gameWorld;

    public GameScreen(RogueGame game){
        this.game = game;
        sb = game.batch;

        //init game cam
        cam = new OrthographicCamera();

        //init viewport
        view = new FitViewport(RogueGame.WIDTH,RogueGame.HEIGHT,cam);
        //set camera to viewport
        cam.position.set(view.getWorldWidth()/2,view.getWorldHeight()/2,0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        gameWorld = new TileMap(this,"text.tmx");

    }

    public void handleInput(float delta){


    }


    public void update(float delta){
    handleInput(delta);

        world.step(1/60f,6,2);

        cam.update();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render
        gameWorld.render();
        b2dr.render(world,cam.combined);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {
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
        gameWorld.dipose();
        world.dispose();
        b2dr.dispose();

    }

    public World getWorld(){
        return world;
    }

}
