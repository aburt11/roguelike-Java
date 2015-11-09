package com.roguelike.game.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.roguelike.game.RogueGame;
import com.roguelike.game.screens.GameScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by adam on 9/11/15.
 */
public class TileMap {

    //tile map vars
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;



    public TileMap(GameScreen screen, String mapFile) {
        World world = screen.getWorld();

        maploader = new TmxMapLoader();
        map = maploader.load(mapFile);

        renderer = new OrthogonalTiledMapRenderer(map,1/ RogueGame.PPM);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / RogueGame.PPM, (rect.getY() + rect.getHeight() / 2) / RogueGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / RogueGame.PPM, rect.getHeight() / 2 / RogueGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }

    public void render()
    {
        renderer.render();

    }

    public void dipose()
    {
        map.dispose();
    }

}
