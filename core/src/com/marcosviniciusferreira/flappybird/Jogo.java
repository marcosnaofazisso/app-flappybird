package com.marcosviniciusferreira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture passaro;

    @Override
    public void create() {

        batch = new SpriteBatch();
        passaro = new Texture("passaro1.png");

    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(passaro, 300, 800);
        batch.end();

    }

    @Override
    public void dispose() {

    }
}
