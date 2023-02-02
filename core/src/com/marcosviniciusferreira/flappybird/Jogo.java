package com.marcosviniciusferreira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class Jogo extends ApplicationAdapter {

    //Texturas
    private SpriteBatch batch;
    private Texture[] passaros;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
    private Texture gameOver;

    //Formas para colisão
    private Circle circuloPassaro;
    private Rectangle retanguloCanoCima;
    private Rectangle retanguloCanoBaixo;
    private ShapeRenderer shapeRenderer;

    //Atributos de configurações
    private float larguraDispositivo;
    private float alturaDispositivo;
    private float variacao = 0;
    private float gravidade = 0;
    private float posicaoInicialVerticalPassaro = 0;
    private float posicaoCanoHorizontal;
    private float posicaoCanoVertical;
    private float espacoEntreCanos;
    private Random random;
    private boolean passouCano;
    private int estadoJogo = 0;

    //Exibição de textos
    BitmapFont textoPontuacao;
    BitmapFont textoColisao;
    BitmapFont textoReiniciar;
    BitmapFont textoMelhorPontuacao;
    private int pontos = 0;
    private int pontuacaoMaxima = 0;

    //Configuracao dos sons
    Sound somVoando;
    Sound somColisao;
    Sound somPontuacao;

    //Objeto salvar pontuacao
    Preferences preferences;

    //Objetos para câmera
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 720;
    private final float VIRTUAL_HEIGHT = 1280;


    @Override
    public void create() {

        inicializarTexturas();
        inicializaObjetos();

    }

    @Override
    public void render() {

        //Limpar frames anteriores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        verificarEstadoJogo();
        validarPontos();
        desenharTexturas();
        detectarColisoes();
    }


    @Override
    public void dispose() {
    }

    private void detectarColisoes() {

        circuloPassaro.set(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        retanguloCanoCima.set(posicaoCanoHorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());

        retanguloCanoBaixo.set(posicaoCanoHorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());

        if (Intersector.overlaps(circuloPassaro, retanguloCanoCima) || Intersector.overlaps(circuloPassaro, retanguloCanoBaixo)) {

            if (estadoJogo == 1) {
                estadoJogo = 2;
                somColisao.play();

            }

        }


        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Forma pássaro
        shapeRenderer.circle(50 + passaros[0].getWidth() / 2,
                posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
        shapeRenderer.setColor(Color.RED);

        //Forma cano (topo)
        shapeRenderer.rect(posicaoCanoHorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
                canoTopo.getWidth(), canoTopo.getHeight());
        //Forma cano (base)
        shapeRenderer.rect(posicaoCanoHorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
                canoBaixo.getWidth(), canoBaixo.getHeight());

        shapeRenderer.end();*/

    }

    private void validarPontos() {
        //Passou da posição do pássaro
        if (posicaoCanoHorizontal < 50 - passaros[0].getWidth()) {
            if (!passouCano) {
                pontos++;
                passouCano = true;
                somPontuacao.play();

            }
        }

        variacao += Gdx.graphics.getDeltaTime() * 10;
        //Verifica variação para bater asas do pássaro
        if (variacao > 3)
            variacao = 0;

    }

    private void verificarEstadoJogo() {

        boolean toqueTela = Gdx.input.justTouched();

        if (estadoJogo == 0) {

            //Aplica evento de toque na tela
            if (toqueTela) {
                gravidade = -15;
                estadoJogo = 1;
                somVoando.play();
            }

        } else if (estadoJogo == 1) {

            if (toqueTela) {
                gravidade = -15;
                somVoando.play();

            }

            //Movimentar cano
            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;
            if (posicaoCanoHorizontal < -canoTopo.getWidth()) {
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVertical = new Random().nextInt(800) - new Random().nextInt(600);
                passouCano = false;
            }

            //Aplica gravidade
            if (posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


            gravidade++;

        } else {

            if (posicaoInicialVerticalPassaro > 0 || toqueTela)
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
            gravidade++;


            if (pontos > pontuacaoMaxima) {
                pontuacaoMaxima = pontos;
                preferences.putInteger("pontuacaoMaxima", pontuacaoMaxima);
            }

            if (toqueTela) {
                estadoJogo = 0;
                pontos = 0;
                gravidade = 0;

                posicaoInicialVerticalPassaro = alturaDispositivo / 2;
                posicaoCanoHorizontal = larguraDispositivo;
            }


        }


    }

    private void desenharTexturas() {

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);

        batch.draw(canoBaixo, posicaoCanoHorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
        batch.draw(canoTopo, posicaoCanoHorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

        textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

        if (estadoJogo == 0) {
            textoColisao.draw(batch, "Toque na tela para iniciar o jogo", larguraDispositivo / 2 - 180, alturaDispositivo / 2 + 80);

        }

        if (estadoJogo == 2) {
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
            textoReiniciar.draw(batch, "Toque para reiniciar", larguraDispositivo / 2 - 150, alturaDispositivo / 2 + 180);
            textoMelhorPontuacao.draw(batch, "Seu record : " + pontuacaoMaxima + " pontos", larguraDispositivo / 2 - 150, alturaDispositivo / 2 - gameOver.getHeight() + 20);

        }
        //textoColisao.draw(batch, "Game Over!", larguraDispositivo / 2, alturaDispositivo / 2);


        batch.end();

    }

    private void inicializarTexturas() {
        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo_maior.png");
        canoTopo = new Texture("cano_topo_maior.png");
        gameOver = new Texture("game_over.png");


    }

    private void inicializaObjetos() {

        batch = new SpriteBatch();

        larguraDispositivo = VIRTUAL_WIDTH;
        alturaDispositivo = VIRTUAL_HEIGHT;
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;
        posicaoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 400;

        //Configuracoes dos textos
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor(Color.WHITE);
        textoPontuacao.getData().setScale(8);

        textoColisao = new BitmapFont();
        textoColisao.setColor(Color.WHITE);
        textoColisao.getData().setScale(2);

        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.WHITE);
        textoReiniciar.getData().setScale(2);

        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.ORANGE);
        textoMelhorPontuacao.getData().setScale(2);

        //Configurar formas para colisões
        shapeRenderer = new ShapeRenderer();
        circuloPassaro = new Circle();
        retanguloCanoCima = new Rectangle();
        retanguloCanoBaixo = new Rectangle();

        //Inicializar sons
        somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
        somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
        somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

        //Configurar preferencias dos objetos
        preferences = Gdx.app.getPreferences("flappyBird");
        pontuacaoMaxima = preferences.getInteger("pontuacaoMaxima", 0);

        //Configuracao da câmera
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }
}
