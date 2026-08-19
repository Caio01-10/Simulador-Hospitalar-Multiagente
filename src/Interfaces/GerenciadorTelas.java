package Interfaces;
import processing.core.PApplet;

public class GerenciadorTelas {
    private EstadoTela estadoAtual;
    private String mapaSelecionado;

    public GerenciadorTelas() {
        this.estadoAtual = EstadoTela.MENU_INICIAL;
        this.mapaSelecionado = "mapa1.txt";
    }

    public void desenhar(){
        switch (estadoAtual) {
            case MENU_INICIAL:
                desenharMenuInicial();
                break;
            case EM_EXECUCAO:
                //
                break;
            case PAUSADO:
                desenharMenuPausa();
                break;
        }
    }

    private void desenharMenuInicial() {
        background(30,40,50);
        textAlign(CENTER, CENTER);
        fill(255);
        textSize(32);
        text("SIMULADOR HOSPITALAR MULTIAGENTE", width / 2, 100);

        //Botões para selecionar mapa
        desenharBotaoMapa("Mapa 1", "mapa1.txt", width / 2 - 120, 220);
        desenharBotaoMapa("Mapa 2", "mapa2.txt", width / 2 + 20, 220);

        //Botão para iniciar 
        fill(46, 204, 113);
        rect(width / 2 - 100, 340, 200, 50, 10);
        fill(255);
        textSize(20);
        text("INICIAR", width / 2, 365);
    }

    private void desenharBotaoMapa(String rotulo, String arquivoMapa, float x, float y) {
        if(mapaSelecionado.equals(arquivoMapa)) {
           stroke(241, 196, 15);
           strokeWeight(3);
        } 
        else {
            noStroke();
        }
        fill(52, 73, 94);
        rect(x, y, 100, 40, 5);
        fill(255);
        textSize(14);
        text(rotulo, x + 50, y + 20);
        noStroke();
    }

    private void desenharMenuPausa() {
        //Overlay de fundo escuro
        fill(0, 0, 0, 180);
        rect(0, 0, width, height);

        fill(255);
        textSize(36);
        textAlign(CENTER, CENTER);
        text("SIMULAÇÃO PAUSADA", width / 2, 150);

        // Opções de Pausa
        desenharBotaoMenu("Continuar (ESC)", width / 2 - 100, 250);
        desenharBotaoMenu("Resetar", width / 2 - 100, 320);
        desenharBotaoMenu("Voltar ao Menu", width / 2 - 100, 390);
    }

    private void desenharBotaoMenu(String texto, float x, float y) {
        fill(41, 128, 185);
        rect(x, y, 200, 45, 8);
        fill(255);
        textSize(16);
        text(texto, x + 100, y + 22);
    }
}
