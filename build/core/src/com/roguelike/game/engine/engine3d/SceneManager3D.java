package com.roguelike.game.engine.engine3d;

/**
 * Created by adam on 22/03/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;


public class SceneManager3D {

    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Array<c3DEntity> models;
    public Environment environment;
    public CameraInputController camController;

    public void create(){
        models = new Array<c3DEntity>();

        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        //lighting
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

    }

    public void update(){

        camController.update();
    }

    public void add3DEntitiy(c3DEntity entity){
        models.add(entity);


    }

    public void render () {

        if(models != null) {
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(cam);
            for(c3DEntity entity: models) {


                modelBatch.render(entity.instance,environment); //render the instances

            }
            modelBatch.end();
        }
    }

    public void dispose () {
        modelBatch.dispose();
    }

}
