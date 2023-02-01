package com.marcosviniciusferreira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Jogo extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture passaro;
    //private Texture[] passaros = {};
    private List<Texture> passaros = new ArrayList<>();
    private float variacao = 0;

    private Texture fundo;

    //Atributos de configurações
    private float larguraDispositivo;
    private float alturaDispositivo;

    @Override
    public void create() {

        batch = new SpriteBatch();
        passaro = new Texture("passaro1.png");
        fundo = new Texture("fundo.png");

//        passaros = new Texture[3];
//        passaros[0] = new Texture("passaro1.png");
//        passaros[1] = new Texture("passaro2.png");
//        passaros[2] = new Texture("passaro3.png");

        passaros.add(0, new Texture("passaro1.png"));
        passaros.add(1, new Texture("passaro2.png"));
        passaros.add(2, new Texture("passaro3.png"));

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();

    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

        if (variacao > 3) variacao = 0;
        batch.draw(passaros.get((int) variacao), 300, alturaDispositivo / 2);

        variacao += 0.1;
        batch.end();

    }

    @Override
    public void dispose() {

    }
}
