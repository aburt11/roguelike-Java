package com.roguelike.game.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roguelike.game.RogueGame;
import com.roguelike.game.entities.enemies.Enemy;
import com.roguelike.game.screens.GameScreen;

/**
 * Created by adam on 19/11/15.
 */
public class HUD implements Disposable {


    //HUD Stage And Viewport
    public Stage stage;
    private Viewport viewport;

    private float heroHealth;
    private float heroExperience;
    private String enemyName;
    private Hero player;

    private static Label enemylabel;
    private Image healthBar;
    private Image expBar;
    private Image emptyExp;
    private Image emptyHealth;
    private Image emptyHealthEnemy;
    private Image healthBarEnemy;
    private Enemy currentEnemy;
    private GameScreen screen;
    private boolean noCurrentTarget;

    private boolean showExpBar = false;

    public HUD(SpriteBatch sb, Hero player, GameScreen screen) {
        this.player = player;
        heroHealth = player.health;
        heroExperience = player.experience;
        this.screen = screen;
        enemyName = "Enemy";

        //Setup The HUD and Viewport
        viewport = new FitViewport(RogueGame.WIDTH, RogueGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels and widgets
        Table table = new Table();
        //Top-Align the table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //add the widgets
        emptyExp = new Image(new Texture("HUD/empty.png"));
        emptyExp.setScale(2, 2);
        emptyExp.setPosition(15, 12);

        emptyHealth = new Image(new Texture("HUD/empty.png"));
        emptyHealth.setPosition(10, 30);
        emptyHealth.setScale(3, 3);
        emptyHealth.setColor(player.GUIRed, player.GUIGreen, player.GUIBlue, player.GUIAlpha);

        emptyHealthEnemy = new Image(new Texture("HUD/empty.png"));
        emptyHealthEnemy.setPosition(RogueGame.WIDTH/2.7f,RogueGame.HEIGHT/1.1f);
        emptyHealthEnemy.setScale(2, 2);
        emptyHealthEnemy.setColor(0.7f,0.0f,0.0f,0.7f);

        healthBarEnemy = new Image(new Texture(Gdx.files.internal("HUD/health.png")));
        healthBarEnemy.setPosition(RogueGame.WIDTH/2.7f +5,RogueGame.HEIGHT/1.1f);
        healthBarEnemy.setScale(1.9f, 1.9f);
        healthBarEnemy.setColor(0.7f,0.0f,0.0f,0.7f);

        expBar = new Image(new Texture(Gdx.files.internal("HUD/experience.png")));
        expBar.setScale(2, 2);
        expBar.setPosition(15,12);


        healthBar = new Image(new Texture(Gdx.files.internal("HUD/health.png")));
        healthBar.setPosition(15,30);
        healthBar.setScale(2.9f, 2.9f);
        healthBar.setColor(player.GUIRed,player.GUIGreen,player.GUIBlue,player.GUIAlpha);

        enemylabel = new Label(enemyName, new Label.LabelStyle(new BitmapFont(), Color.RED));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(enemylabel).right().pad(9);


        //add our table to the stage
        stage.addActor(table);
        stage.addActor(emptyExp);
        stage.addActor(emptyHealth);
        stage.addActor(expBar);
        stage.addActor(healthBar);
        stage.addActor(emptyHealthEnemy);
        stage.addActor(healthBarEnemy);
    }

    public void update(float delta){

        //set targeted enemy
        if(screen.Enemies != null) {
            for (Enemy enemies : screen.Enemies) {
                if (enemies.isTargeted) {
                    this.currentEnemy = enemies;
                    noCurrentTarget = false;
                }
                else
                {
                    noCurrentTarget = true;
                }
            }
        }


        //show or Hide XP Bar
        if(showExpBar)
        {
            expBar.setVisible(true);
            emptyExp.setVisible(true);
        }
        else
        {
            expBar.setVisible(false);
            emptyExp.setVisible(false);
        }

        //update the stats
        heroHealth = player.health;
        heroExperience = player.experience;



        //update the bars
        healthBar.setScale(2.9f * (heroHealth/100),2.9f);
        expBar.setScale(2  * (heroExperience/100),2);

        //update Enemy UI
        if(screen.Enemies != null)
        {
            if(!noCurrentTarget) {
                enemylabel.setText(currentEnemy.EnemyName);

                healthBarEnemy.setScale(1.9f * (currentEnemy.Health / 100), 1.9f);
            }

        }

        //update the GUI on Change
        if(player.GUIChanged) {
            expBar.setColor(player.GUIRed, player.GUIGreen, player.GUIBlue, player.GUIAlpha);
            emptyHealth.setColor(player.GUIRed, player.GUIGreen, player.GUIBlue, player.GUIAlpha);
            player.GUIChanged = false;
        }

    }

    @Override
    public void dispose() {
        stage.dispose();

    }
}
