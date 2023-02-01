package com.marcosviniciusferreira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Jogo extends ApplicationAdapter {

    private SpriteBatch batch;
    private List<Texture> passaros = new ArrayList<>();
    private Texture fundo;
    private float variacao = 0;
    private float gravidade = 0;
    private float posicaoInicialVertical = 0;


    //Atributos de configurações
    private float larguraDispositivo;
    private float alturaDispositivo;

    @Override
    public void create() {

        batch = new SpriteBatch();
        inicializarTexturas();
        inicializarObjetos();

    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

        //Movimentos de bater asas
        if (variacao > 3) variacao = 0;
        batch.draw(passaros.get((int) variacao), 300, posicaoInicialVertical);
        variacao += Gdx.graphics.getDeltaTime() * 10;


        //Aplica evento de clique
        boolean houveToqueTela = Gdx.input.justTouched();
        if (houveToqueTela) {
            gravidade = -20;
        }

        //Aplica gravidade no pássaro
        if (posicaoInicialVertical > 0 || houveToqueTela) {
            posicaoInicialVertical -= gravidade;
        }


        gravidade++;
        batch.end();

    }

    @Override
    public void dispose() {

    }

    private void inicializarTexturas() {

        fundo = new Texture("fundo.png");

        passaros.add(0, new Texture("passaro1.png"));
        passaros.add(1, new Texture("passaro2.png"));
        passaros.add(2, new Texture("passaro3.png"));

    }

    private void inicializarObjetos() {

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        posicaoInicialVertical = alturaDispositivo / 2;

    }

}
