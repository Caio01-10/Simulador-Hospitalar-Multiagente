import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class Sketch extends PApplet {
    GerenciadorTelas gerenciadorTelas;
    Renderizador renderizador;


    public static void main(String[] args) {
        PApplet.main("Sketch");
    }

    public static final int LARGURA_JANELA = 800, ALTURA_JANELA = 650;
    public static final int MARGEM = 20, ALTURA_CABECALHO = 78, LARGURA_LEGENDA = 250;

    char[][] mapa;
    int numLinhas;
    int numColunas;

    float tamanhoCelula;
    float origemX;
    float origemY;

    String nomeArquivo = "mapa_hospital_professor.txt", mensagemErro = "";

    @Override
    public void settings() {
        // Define a dimensão da janela no local correto
        size(LARGURA_JANELA, ALTURA_JANELA);
    }

    @Override
    public void setup() {
        System.out.println("Caminho atual da pasta data: " + dataPath(""));
        // Inicializa suas classes de controle de tela e renderização
        gerenciadorTelas = new GerenciadorTelas(this);
        renderizador = new Renderizador(this);

        surface.setTitle("Visualizador de Mapa Hospitalar");
        
        // Ative a leitura do mapa quando a função carregarMapa estiver implementada:
        // carregarMapa(nomeArquivo);
    }

    @Override
    public void draw() {
        background(20);

        // Se a simulação estiver ativa, desenha a lógica do jogo/grid
        if (gerenciadorTelas.getEstadoAtual() == EstadoTela.EM_EXECUCAO) {
            renderizador.renderizarGrid(); // Desenha a malha
            renderizador.renderizarHUD(null);  // Desenha a barra inferior
        }

        // Desenha os menus por cima
        gerenciadorTelas.desenhar(this);
    }

    @Override
    public void mousePressed() {
        // Repassa os cliques para o gerenciador de telas
        gerenciadorTelas.tratarCliqueMouse(mouseX, mouseY, this);
    }

    @Override
    public void keyPressed() {
        // Se a tecla pressionada for ESC, impede que o Processing feche a janela
        if (key == ESC) {
            key = 0; 
        }

        // Repassa os eventos do teclado para o gerenciador de telas
        if (gerenciadorTelas != null) {
            gerenciadorTelas.tratarTeclado(keyCode, key, this);
        }
    }
}