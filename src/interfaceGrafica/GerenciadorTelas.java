package interfaceGrafica;

import enums.EstadoTela;
import app.Sketch;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class GerenciadorTelas {
    private EstadoTela estadoAtual;
    private String mapaSelecionado;

    // Imagens e Fontes
    private PImage imgFundo;
    private PImage imgFundoPausa;
    private PImage imgThumbMapa1;
    private PImage imgThumbMapa2;
    private PFont fonteMenu;
    private PFont fonteNativa;

    // Controle de Navegação via Teclado (Índice da opção selecionada)
    private int opcaoSelecionada = 0;
    private int totalOpcoes = 3;

    // Sinaliza para quem estiver integrando (Sketch/SimuladorEngine) que a opção
    // "Resetar" foi escolhida no menu de pausa. Necessário porque tanto "Continuar"
    // quanto "Resetar" levam ao mesmo estado EM_EXECUCAO, e sem essa flag não haveria
    // como distinguir as duas ações a partir de fora desta classe.
    private boolean solicitarReset = false;

    public GerenciadorTelas(Sketch sketch) {
        estadoAtual = EstadoTela.MENU_INICIAL;
        mapaSelecionado = "mapa1.txt";

        try {
            imgFundo = sketch.loadImage(sketch.dataPath("sprites/menuInicial.png"));
            imgFundoPausa = sketch.loadImage(sketch.dataPath("sprites/menuPause.png"));
            imgThumbMapa1 = sketch.loadImage(sketch.dataPath("sprites/imgThumbMapa1.png"));
            imgThumbMapa2 = sketch.loadImage(sketch.dataPath("sprites/imgThumbMapa2.png"));
            fonteMenu = sketch.createFont(sketch.dataPath("fontes/fonte.ttf"), 16);
            fonteNativa = sketch.createFont("SansSerif", 16);
        } catch (Exception e) {
            System.out.println("Aviso: Falha ao carregar um ou mais recursos visuais.");
        }
    }

    public void desenhar(Sketch sketch) {
        sketch.cursor(PApplet.ARROW);

        switch (estadoAtual) {
            case MENU_INICIAL:
                desenharMenuInicial(sketch);
                break;
            case SELECAO_MAPA:
                desenharSelecaoMapa(sketch);
                break;
            case EM_EXECUCAO:
                break;
            case PAUSADO:
                desenharMenuPausa(sketch);
                break;
            case CREDITOS:
                desenharCreditos(sketch);
                break;
        }
    }

    private void desenharFundoComOverlay(PImage imagem, Sketch sketch) {
        if (imagem != null) {
            sketch.imageMode(PApplet.CORNER);
            sketch.image(imagem, 0, 0, sketch.width, sketch.height);
        } else {
            sketch.background(30, 40, 50);
        }

        // Overlay preto transparente por cima do fundo
        sketch.noStroke();
        sketch.fill(0, 0, 0, 150);
        sketch.rect(0, 0, sketch.width, sketch.height);
    }

    private void desenharBannerTitulo(String texto, float bannerY, Sketch sketch) {
        float bannerLargura = 620;
        float bannerAltura = 65;
        float bannerX = (sketch.width - bannerLargura) / 2.0f;

        // Banner com o mesmo visual do menu inicial
        sketch.fill(15, 23, 42, 210);
        sketch.stroke(52, 152, 219, 180);
        sketch.strokeWeight(2);
        sketch.rect(bannerX, bannerY, bannerLargura, bannerAltura, 12);
        sketch.noStroke();

        if (fonteMenu != null)
            sketch.textFont(fonteMenu);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);

        // Sombra
        sketch.fill(0, 0, 0, 180);
        sketch.textSize(24);
        sketch.text(texto, (sketch.width / 2.0f) + 2, bannerY + (bannerAltura / 2.0f) + 2);

        // Texto principal
        sketch.fill(255);
        sketch.text(texto, sketch.width / 2.0f, bannerY + (bannerAltura / 2.0f));
    }

    private void desenharMenuInicial(Sketch sketch) {
        desenharFundoComOverlay(imgFundo, sketch);

        float bannerY = sketch.height * 0.12f;
        desenharBannerTitulo("SIMULADOR HOSPITALAR MULTIAGENTE", bannerY, sketch);

        float posX = sketch.width / 2.0f - 100;
        float startY = sketch.height * 0.35f;

        desenharBotaoMenu("Iniciar Simulação", posX, startY, 200, 45, 0, sketch);
        desenharBotaoMenu("Créditos", posX, startY + 60, 200, 45, 1, sketch);
        desenharBotaoMenu("Sair", posX, startY + 120, 200, 45, 2, sketch);

        // Instruções de navegação
        float instrucaoY = sketch.height * 0.88f;

        // 1. Largura exata do fundo da caixa
        float caixaLargura = 520;
        float caixaAltura = 30;
        float caixaX = (sketch.width - caixaLargura) / 2.0f;
        float caixaY = instrucaoY - (caixaAltura / 2.0f);

        // Fundo centralizado
        sketch.fill(15, 23, 42, 180);
        sketch.stroke(52, 152, 219, 100);
        sketch.strokeWeight(1);
        sketch.rect(caixaX, caixaY, caixaLargura, caixaAltura, 8);
        sketch.noStroke();

        // 2. Textos alinhados a partir da borda esquerda da caixa
        float paddingEsquerdo = 18;
        float posXX = caixaX + paddingEsquerdo;

        if (fonteMenu != null)
            sketch.textFont(fonteMenu);
        sketch.textSize(12);
        sketch.fill(220, 230, 242);
        sketch.textAlign(PApplet.LEFT, PApplet.CENTER);

        // Parte 1: Texto inicial
        String txt1 = "Use  [ W / S ]  ou  [ ";
        sketch.text(txt1, posXX, instrucaoY);
        posXX += sketch.textWidth(txt1);

        // Parte 2: Setas (Fonte nativa)
        if (fonteNativa != null)
            sketch.textFont(fonteNativa);
        else
            sketch.textFont(sketch.createFont("SansSerif", 13));

        sketch.text("↑ / ↓", posXX, instrucaoY);
        posXX += sketch.textWidth("↑ / ↓");

        // Parte 3: Texto final
        if (fonteMenu != null)
            sketch.textFont(fonteMenu);
        sketch.textSize(12);

        String txt3 = " ]  para navegar   •   [ ENTER / ESPAÇO ]  para selecionar";
        sketch.text(txt3, posXX, instrucaoY);
    }

    private void desenharMenuPausa(Sketch sketch) {
        // Usa o fundo de pausa ou o fundo padrão se não houver imagem
        PImage fundoAtual = (imgFundoPausa != null) ? imgFundoPausa : imgFundo;
        desenharFundoComOverlay(fundoAtual, sketch);

        float bannerY = sketch.height * 0.12f;
        desenharBannerTitulo("SIMULAÇÃO PAUSADA", bannerY, sketch);

        float posX = sketch.width / 2.0f - 100;
        float startY = sketch.height * 0.35f;

        desenharBotaoMenu("Continuar (ESC)", posX, startY, 200, 45, 0, sketch);
        desenharBotaoMenu("Resetar", posX, startY + 60, 200, 45, 1, sketch);
        desenharBotaoMenu("Voltar ao Menu", posX, startY + 120, 200, 45, 2, sketch);

        // Instruções de navegação

        // Fundo
        float instrucaoY = sketch.height * 0.88f;
        sketch.fill(15, 23, 42, 180);
        sketch.stroke(52, 152, 219, 100);
        sketch.strokeWeight(1);
        sketch.rect(sketch.width / 2.0f - 250, instrucaoY - 14, 500, 28, 8);
        sketch.noStroke();

        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.textSize(12);
        sketch.fill(200);
        sketch.text("Pressione [ ESC ] para Retornar a Simulacao", sketch.width / 2.0f, sketch.height * 0.88f);
    }

    private void desenharSelecaoMapa(Sketch sketch) {
        desenharFundoComOverlay(imgFundo, sketch);

        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.fill(255);
        sketch.textSize(24);
        sketch.text("Selecione o Mapa", sketch.width / 2.0f, 60);

        desenharCardMapa("Mapa 1", "mapa1.txt", imgThumbMapa1, sketch.width / 2.0f - 130, 110, 0, sketch);
        desenharCardMapa("Mapa 2", "mapa2.txt", imgThumbMapa2, sketch.width / 2.0f + 10, 110, 1, sketch);

        desenharBotaoMenu("Começar", sketch.width / 2.0f - 100, 290, 200, 45, 2, sketch);
        desenharBotaoMenu("Voltar", sketch.width / 2.0f - 100, 350, 200, 45, 3, sketch);
    }

    private void desenharCardMapa(String rotulo, String arquivo, PImage thumbnail, float x, float y, int indiceOpcao,
            Sketch sketch) {
        boolean mouseOver = sketch.mouseX >= x && sketch.mouseX <= x + 120 &&
                sketch.mouseY >= y && sketch.mouseY <= y + 140;

        if (mouseOver) {
            sketch.cursor(PApplet.HAND);
            opcaoSelecionada = indiceOpcao;
        }

        boolean tecladoFocado = (opcaoSelecionada == indiceOpcao);

        if (mapaSelecionado.equals(arquivo)) {
            sketch.stroke(241, 196, 15); // Amarelo para o mapa selecionado
            sketch.strokeWeight(3);
        } else if (tecladoFocado || mouseOver) {
            sketch.stroke(255); // Destaque ao passar o mouse ou focar no teclado
            sketch.strokeWeight(2);
        } else {
            sketch.stroke(100);
            sketch.strokeWeight(1);
        }

        sketch.fill(40, 50, 60, 220);
        sketch.rect(x, y, 120, 140, 8);
        sketch.noStroke();

        if (thumbnail != null) {
            sketch.image(thumbnail, x + 10, y + 10, 100, 90);
        } else {
            sketch.fill(80);
            sketch.rect(x + 10, y + 10, 100, 90, 4);
            sketch.fill(180);
            sketch.textSize(10);
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.text("Sem Foto", x + 60, y + 55);
        }

        sketch.fill(255);
        sketch.textSize(13);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text(rotulo, x + 60, y + 118);
    }

    private void desenharCreditos(Sketch sketch) {
        desenharFundoComOverlay(imgFundo, sketch);

        sketch.fill(255);
        sketch.textSize(30);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text("Créditos", sketch.width / 2.0f, 100);

        sketch.textSize(16);
        sketch.text(
                "Desenvolvido por:\n- Bento Martins Tristão\n- Caio Henrique Macedo Silva\n- Davi Campos Montijo\n- Pedro Mendes Santana",
                sketch.width / 2.0f, 220);

        desenharBotaoMenu("Voltar ao Menu", sketch.width / 2.0f - 100, 380, 200, 45, 0, sketch);
    }

    private boolean desenharBotaoMenu(String texto, float x, float y, float larg, float alt, int indiceOpcao,
            Sketch sketch) {
        boolean mouseOver = sketch.mouseX >= x && sketch.mouseX <= x + larg &&
                sketch.mouseY >= y && sketch.mouseY <= y + alt;

        // Se o mouse estiver sobre o botão, sincroniza a seleção via teclado
        if (mouseOver) {
            sketch.cursor(PApplet.HAND);
            opcaoSelecionada = indiceOpcao;
        }

        boolean focado = (opcaoSelecionada == indiceOpcao);

        if (focado) {
            sketch.fill(52, 152, 219); // Azul claro para seleção
            sketch.stroke(255);
            sketch.strokeWeight(2.0f);
        } else {
            sketch.fill(41, 128, 185); // Azul normal
            sketch.noStroke();
        }

        sketch.rect(x, y, larg, alt, 8);

        sketch.fill(255);
        sketch.textSize(16);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text(texto, x + (larg / 2.0f), y + (alt / 2.0f));

        return mouseOver;
    }

    // --- TRATAMENTO DE INTERAÇÃO VIA MOUSE ---
    public void tratarCliqueMouse(int x, int y, Sketch sketch) {
        executarAcaoOpcao(opcaoSelecionada, sketch);
    }

    // --- TRATAMENTO DE INTERAÇÃO VIA TECLADO ---
    public void tratarTeclado(int keyCode, char key, Sketch sketch) {
        // Detecta a tecla ESC (via código ou caractere)
        if (keyCode == PApplet.ESC || keyCode == 27) {
            if (estadoAtual == EstadoTela.EM_EXECUCAO) {
                mudarEstado(EstadoTela.PAUSADO, 3);
            } else if (estadoAtual == EstadoTela.PAUSADO) {
                mudarEstado(EstadoTela.EM_EXECUCAO, 3);
            } else if (estadoAtual == EstadoTela.SELECAO_MAPA || estadoAtual == EstadoTela.CREDITOS) {
                mudarEstado(EstadoTela.MENU_INICIAL, 3);
            }
            return;
        }

        // Navegação com setas/ENTER/W/S durante a pausa ou menus
        if (estadoAtual != EstadoTela.EM_EXECUCAO) {
            if (keyCode == PApplet.UP || key == 'w' || key == 'W') {
                opcaoSelecionada = (opcaoSelecionada - 1 + totalOpcoes) % totalOpcoes;
            } else if (keyCode == PApplet.DOWN || key == 's' || key == 'S') {
                opcaoSelecionada = (opcaoSelecionada + 1) % totalOpcoes;
            } else if (keyCode == PApplet.ENTER || key == ' ') {
                executarAcaoOpcao(opcaoSelecionada, sketch);
            }
        }
    }

    // Executa a lógica da opção ativa (clicada ou confirmada no Enter/Espaço)
    private void executarAcaoOpcao(int opcao, Sketch sketch) {
        switch (estadoAtual) {
            case MENU_INICIAL:
                if (opcao == 0)
                    mudarEstado(EstadoTela.SELECAO_MAPA, 4);
                else if (opcao == 1)
                    mudarEstado(EstadoTela.CREDITOS, 1);
                else if (opcao == 2)
                    sketch.exit();
                break;

            case PAUSADO:
                if (opcao == 0)
                    mudarEstado(EstadoTela.EM_EXECUCAO, 3);
                else if (opcao == 1) {
                    solicitarReset = true;
                    mudarEstado(EstadoTela.EM_EXECUCAO, 3); // Lógica de Reset
                }
                else if (opcao == 2)
                    mudarEstado(EstadoTela.MENU_INICIAL, 3);
                break;

            case EM_EXECUCAO:
                break;

            case SELECAO_MAPA:
                if (opcao == 0)
                    mapaSelecionado = "mapa1.txt";
                else if (opcao == 1)
                    mapaSelecionado = "mapa2.txt";
                else if (opcao == 2)
                    mudarEstado(EstadoTela.EM_EXECUCAO, 3);
                else if (opcao == 3)
                    mudarEstado(EstadoTela.MENU_INICIAL, 3);
                break;

            case CREDITOS:
                if (opcao == 0)
                    mudarEstado(EstadoTela.MENU_INICIAL, 3);
                break;
        }
    }

    private void mudarEstado(EstadoTela novoEstado, int numOpcoesSubsequente) {
        this.estadoAtual = novoEstado;
        this.opcaoSelecionada = 0;
        this.totalOpcoes = numOpcoesSubsequente;
    }

    public EstadoTela getEstadoAtual() {
        return estadoAtual;
    }

    public String getMapaSelecionado() {
        return mapaSelecionado;
    }

    /**
     * Consome (lê e zera) a solicitação de reset feita no menu de pausa.
     * Deve ser chamado por quem controla o motor de simulação assim que
     * detectar a transição PAUSADO -> EM_EXECUCAO.
     */
    public boolean consumirSolicitacaoReset() {
        boolean valor = solicitarReset;
        solicitarReset = false;
        return valor;
    }
}
