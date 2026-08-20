import processing.core.PApplet;

public class Sketch extends PApplet {
  GerenciadorTelas gerenciadorTelas;
  Renderizador renderizador;

  public static void main(String[] args) {
    PApplet.main("Sketch");
  }

  public static final int LARGURA_JANELA = 800, ALTURA_JANELA = 600;
  public static final int MARGEM = 20, ALTURA_CABECALHO = 78, LARGURA_LEGENDA = 250;

  char[][] mapa;
  int numLinhas;
  int numColunas;

  float tamanhoCelula;
  float origemX;
  float origemY;

  String nomeArquivo = "mapa_hospital_professor.txt", mensagemErro = "";

  int corChao, corParede, corGerador, corRemovedor, corTotem, corAssento, corEnfermeiro, corMedico, corGrade;

  @Override
  public void settings() {
    size(LARGURA_JANELA, ALTURA_JANELA);
  }

  @Override
  public void setup() {
    size(800,650);
    gerenciadorTelas = new GerenciadorTelas();
    //renderizador = new Renderizador();

    surface.setTitle("Visualizador de mapa hospitalar");

    corChao       = color(239, 229, 194); // bege
    corParede     = color(205, 164, 112); // marrom claro
    corGerador    = color(20, 155, 45);   // verde
    corRemovedor  = color(225, 45, 55);   // vermelho
    corTotem      = color(50, 90, 225);   // azul
    corAssento    = color(115, 62, 31);   // marrom escuro
    corEnfermeiro = color(35);            // preto
    corMedico     = color(250, 205, 20);  // amarelo
    corGrade      = color(175, 151, 112);

    //carregarMapa(nomeArquivo);
  }

  @Override
  public void draw() {
background(20);

  if (gerenciadorTelas.getEstadoAtual() == EstadoTela.EM_EXECUCAO) {
    // Chamadas dos métodos da equipe (Ex: grid, pacientes, etc.)
    // renderizador.renderizarGrid(grid);
    // renderizador.renderizarAgentes(pacientes);
    renderizador.renderizarHUD(null);
  }

  /*  Desenha os componentes de telas/menus por cima da simulação
  gerenciadorTelas.desenhar();
  }
  */
  @Override
  public void keyPressed() {
    gerenciadorTelas.tratarTeclado(key);
  }

  @Override
  public void mousePressed() {
    //gerenciadorTelas.tratarCliqueMouse(mouseX, mouseY);
  }
}

