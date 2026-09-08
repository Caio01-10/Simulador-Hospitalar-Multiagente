package gerenciador;

import processing.core.PApplet;
import mapa.GridHospital;
import renderizador.RenderizadorMapa;

public class GerenciadorMapa extends PApplet {

    private GridHospital hospital = new GridHospital();
    private RenderizadorMapa renderizador;
    private String mapaAtual = "mapas/mapa1.txt";

    public static void main(String[] args) {
        PApplet.main("gerenciador.GerenciadorMapa");
    }

    @Override
    public void settings() {
        carregarMapa(mapaAtual);
        int largura = hospital.getColunas() * RenderizadorMapa.TAMANHO_CELULA;
        int altura = hospital.getLinhas() * RenderizadorMapa.TAMANHO_CELULA;
        size(largura, altura);
    }

    @Override
    public void setup() {
        surface.setTitle("Simulador Hospitalar Multiagente");
        renderizador = new RenderizadorMapa(this);
    }

    @Override
    public void draw() {
        background(30);
        
        if (hospital != null && renderizador != null) {
            renderizador.renderizar(hospital);
        }
    }

    @Override
    public void keyPressed() {
        if (key == '1') {
            trocarMapa("mapas/mapa1.txt");
        } else if (key == '2') {
            trocarMapa("mapas/mapa2.txt");
        } else if (key == '3') {
            trocarMapa("mapas/mapa3.txt");
        }
    }

    private void trocarMapa(String caminhoRelativo) {
        carregarMapa(caminhoRelativo);
        int largura = hospital.getColunas() * RenderizadorMapa.TAMANHO_CELULA;
        int altura = hospital.getLinhas() * RenderizadorMapa.TAMANHO_CELULA;
        surface.setSize(largura, altura);
    }

   private void carregarMapa(String caminhoRelativo) {
    this.mapaAtual = caminhoRelativo;
    boolean sucesso = hospital.carregarDeArquivo(this, mapaAtual);

    if (!sucesso) {
        sucesso = hospital.carregarDeArquivo(this, "data/" + mapaAtual);
    }
    if (sucesso) {
        System.out.println("Mapa '" + mapaAtual + "' carregado com sucesso!");
    } else {
        System.err.println("Erro ao carregar o arquivo: " + mapaAtual);
    }
}
}