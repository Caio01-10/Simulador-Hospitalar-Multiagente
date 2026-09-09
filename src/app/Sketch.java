package app;

import interfaceGrafica.GerenciadorTelas;
import renderizadores.RenderizadorHospital;
import enums.EstadoTela;
import processing.core.PApplet;

/**
 * Ponto de entrada da aplicação Processing. Coordena três responsabilidades:
 * - GerenciadorTelas: menus (inicial, seleção de mapa, pausa, créditos);
 * - SimuladorEngine: toda a lógica/estado da simulação em si;
 * - RenderizadorHospital: desenho do grid, pacientes e HUD.
 *
 * O Sketch não conhece regras de negócio do hospital; ele apenas observa as
 * transições de estado do GerenciadorTelas e reage criando, pausando,
 * retomando ou reiniciando o SimuladorEngine conforme necessário.
 */
public class Sketch extends PApplet {

    public static void main(String[] args) {
        PApplet.main("app.Sketch");
    }

    public static final int LARGURA_JANELA = 800;
    public static final int ALTURA_JANELA = 650;

    private GerenciadorTelas gerenciadorTelas;
    private RenderizadorHospital renderizador;
    private SimuladorEngine engine;

    private EstadoTela estadoAnterior;
    private int ultimoMillis;

    @Override
    public void settings() {
        size(LARGURA_JANELA, ALTURA_JANELA);
    }

    @Override
    public void setup() {
        surface.setTitle("Simulador Hospitalar Multiagente");

        gerenciadorTelas = new GerenciadorTelas(this);
        renderizador = new RenderizadorHospital(this);

        estadoAnterior = gerenciadorTelas.getEstadoAtual();
        ultimoMillis = millis();
    }

    @Override
    public void draw() {
        background(20);

        int agora = millis();
        float deltaTime = (agora - ultimoMillis) / 1000.0f;
        ultimoMillis = agora;

        sincronizarEngineComTelas();

        EstadoTela estadoAtual = gerenciadorTelas.getEstadoAtual();

        if (estadoAtual == EstadoTela.EM_EXECUCAO && engine != null) {
            engine.update(deltaTime);
            renderizador.renderizar(this, engine);
        }

        // Os menus (inicial, seleção de mapa, pausa, créditos) são sempre
        // desenhados por cima; durante EM_EXECUCAO, desenhar() não desenha
        // nada (ver GerenciadorTelas.desenhar).
        gerenciadorTelas.desenhar(this);
    }

    /**
     * Observa as transições de tela desde o quadro anterior e aciona as
     * operações correspondentes no SimuladorEngine. O GerenciadorTelas não
     * conhece o SimuladorEngine (separação de responsabilidades), então essa
     * ponte precisa ser feita aqui.
     */
    private void sincronizarEngineComTelas() {
        EstadoTela estadoAtual = gerenciadorTelas.getEstadoAtual();

        if (estadoAnterior == EstadoTela.SELECAO_MAPA && estadoAtual == EstadoTela.EM_EXECUCAO) {
            iniciarNovaSimulacao(gerenciadorTelas.getMapaSelecionado());
        }

        if (estadoAnterior == EstadoTela.PAUSADO && estadoAtual == EstadoTela.EM_EXECUCAO && engine != null) {
            if (gerenciadorTelas.consumirSolicitacaoReset()) {
                engine.reiniciarTela();
            } else {
                engine.continuar();
            }
        }

        if (estadoAnterior == EstadoTela.EM_EXECUCAO && estadoAtual == EstadoTela.PAUSADO && engine != null) {
            engine.pausar();
        }

        if (estadoAtual == EstadoTela.MENU_INICIAL) {
            // Sair para o menu inicial descarta a simulação em andamento,
            // permitindo escolher um novo mapa do zero.
            engine = null;
        }

        estadoAnterior = estadoAtual;
    }

    private void iniciarNovaSimulacao(String nomeArquivoMapa) {
        try {
            engine = new SimuladorEngine(this, nomeArquivoMapa);
        } catch (IllegalStateException e) {
            System.err.println("Falha ao iniciar a simulação: " + e.getMessage());
            engine = null;
        }
    }

    @Override
    public void mousePressed() {
        gerenciadorTelas.tratarCliqueMouse(mouseX, mouseY, this);
    }

    @Override
    public void keyPressed() {
        // Impede que o Processing feche a janela ao pressionar ESC; o próprio
        // GerenciadorTelas usa essa tecla para alternar pausa/menus.
        if (key == ESC) {
            key = 0;
        }

        if (gerenciadorTelas != null) {
            gerenciadorTelas.tratarTeclado(keyCode, key, this);
        }
    }
}
