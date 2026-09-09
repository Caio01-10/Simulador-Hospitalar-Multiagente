package criacaoMapa;

import processing.core.PApplet;
import mapa.GridHospital;
import renderizador.RenderizadorMapa;

public class Main extends PApplet {

    private GridHospital hospital = new GridHospital();
    private RenderizadorMapa renderizador;
    private String mapaAtual = "mapas/mapa1.txt";

    public static void main(String[] args) {
        Main sketch = new Main();
        sketch.setMapaAtual(1);
        PApplet.runSketch(new String[]{"criacaoMapa.Main"}, sketch);
    }

    public void setMapaAtual(int opcao) {
        switch (opcao) {
            case 1:
                this.mapaAtual = "mapas/mapa1.txt";
                break;
            case 2:
                this.mapaAtual = "mapas/mapa2.txt";
                break;
            default:
                System.err.println("Opção de mapa inválida: " + opcao);
        }
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