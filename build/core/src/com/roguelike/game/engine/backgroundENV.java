package com.roguelike.game.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by adam on 6/01/16.
 */
public class backgroundENV {

    private String filename;
    private OrthographicCamera cam;
    private Texture tex;
    private SpriteBatch sb;

    public backgroundENV(String filename, OrthographicCamera cam, SpriteBatch sb)
    {
        this.filename = filename;
        this.cam = cam;
        this.sb = sb;

        tex = new Texture(filename);

    }

    public void render()
    {
        sb.draw(tex,(cam.position.x+tex.getWidth())/2,(cam.position.y+tex.getHeight())/2);

    }
}
