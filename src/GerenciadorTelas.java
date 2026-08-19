import processing.core.PApplet;

public class GerenciadorTelas {
    private EstadoTela estadoAtual;
    private String mapaSelecionado;

    public GerenciadorTelas() {
        this.estadoAtual = EstadoTela.MENU_INICIAL;
        this.mapaSelecionado = "mapa1.txt";
    }

    public void desenhar(Sketch sketch) {
        switch (estadoAtual) {
            case MENU_INICIAL:
                desenharMenuInicial(sketch);
                break;
            case EM_EXECUCAO:
                //
                break;
            case PAUSADO:
                desenharMenuPausa(sketch);
                break;
        }
    }

    private void desenharMenuInicial(Sketch sketch) {
        sketch.background(30,40,50);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.fill(255);
        sketch.textSize(32);
        sketch.text("SIMULADOR HOSPITALAR MULTIAGENTE", sketch.width / 2, 100);

        //Botões para selecionar mapa
        desenharBotaoMapa("Mapa 1", "mapa1.txt", sketch.width / 2 - 120, 220, sketch);
        desenharBotaoMapa("Mapa 2", "mapa2.txt", sketch.width / 2 + 20, 220, sketch);

        //Botão para iniciar 
        sketch.fill(46, 204, 113);
        sketch.rect(sketch.width / 2 - 100, 340, 200, 50, 10);
        sketch.fill(255);
        sketch.textSize(20);
        sketch.text("INICIAR", sketch.width / 2, 365);
    }

    private void desenharBotaoMapa(String rotulo, String arquivoMapa, float x, float y, Sketch sketch) {
        if(mapaSelecionado.equals(arquivoMapa)) {
           sketch.stroke(241, 196, 15);
           sketch.strokeWeight(3);
        } 
        else {
            sketch.noStroke();
        }
        sketch.fill(52, 73, 94);
        sketch.rect(x, y, 100, 40, 5);
        sketch.fill(255);
        sketch.textSize(14);
        sketch.text(rotulo, x + 50, y + 20);
        sketch.noStroke();
    }

    private void desenharMenuPausa(Sketch sketch) {
        //Overlay de fundo escuro
        sketch.fill(0, 0, 0, 180);
        sketch.rect(0, 0, sketch.width, sketch.height);

        sketch.fill(255);
        sketch.textSize(36);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text("SIMULAÇÃO PAUSADA", sketch.width / 2, 150);

        // Opções de Pausa
        desenharBotaoMenu("Continuar (ESC)", sketch.width / 2 - 100, 250, sketch);
        desenharBotaoMenu("Resetar", sketch.width / 2 - 100, 320, sketch);
        desenharBotaoMenu("Voltar ao Menu", sketch.width / 2 - 100, 390, sketch);
    }

    private void desenharBotaoMenu(String texto, float x, float y, Sketch sketch) {
        sketch.fill(41, 128, 185);
        sketch.rect(x, y, 200, 45, 8);
        sketch.fill(255);
        sketch.textSize(16);
        sketch.text(texto, x + 100, y + 22);
    }

    public void tratarCliqueMouse(int x, int y, Sketch sketch) {
    if (estadoAtual == EstadoTela.MENU_INICIAL) {
        if (x >= sketch.width / 2 - 120 && x <= sketch.width / 2 - 20 && y >= 220 && y <= 260) {
            mapaSelecionado = "mapa1.txt";
        } else if (x >= sketch.width / 2 + 20 && x <= sketch.width / 2 + 120 && y >= 220 && y <= 260) {
            mapaSelecionado = "mapa2.txt";
        } else if (x >= sketch.width / 2 - 100 && x <= sketch.width / 2 + 100 && y >= 340 && y <= 390) {
            // Ação: Carregar mapa e iniciar simulação
            estadoAtual = EstadoTela.EM_EXECUCAO;
        }
        } else if (estadoAtual == EstadoTela.PAUSADO) {
        if (x >= sketch.width / 2 - 100 && x <= sketch.width / 2 + 100) {
            if (y >= 250 && y <= 295) estadoAtual = EstadoTela.EM_EXECUCAO; // Continuar
            else if (y >= 320 && y <= 365) { /* Lógica de Resetar */ }
            else if (y >= 390 && y <= 435) estadoAtual = EstadoTela.MENU_INICIAL; // Voltar
        }
        }
    }

    public void tratarTeclado(char tecla){
        if (tecla == PApplet.ESC){
            tecla = 0;
            if (estadoAtual == EstadoTela.EM_EXECUCAO) {
                estadoAtual = EstadoTela.PAUSADO;
            } else if (estadoAtual == EstadoTela.PAUSADO) {
                estadoAtual = EstadoTela.EM_EXECUCAO;
            }
        }
    }

    public EstadoTela getEstadoAtual() { return estadoAtual; }
    public String getMapaSelecionado() { return mapaSelecionado; }
}