package com.roguelike.game.engine.engine3d;

/**
 * Created by adam on 22/03/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class c3DEntity {
    public Model model;
    public ModelInstance instance;
    public String filename;

    public c3DEntity(String filename){
        this.filename = filename;
        this.create();

    }

    public void translate(float x, float y, float z) {
        instance.transform.translate(x,y,z);
    }

    public void rotate(float x, float y, float z, float degrees){
        instance.transform.rotate(x,y,z,degrees);
    }

    public void scale(float x, float y, float z){
        instance.transform.scale(x,y,z);

    }

    public void create(){


        ModelLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal(filename));
        instance = new ModelInstance(model);
        instance = new ModelInstance(model);
    }

    public void dispose () {
        model.dispose();
    }


}
