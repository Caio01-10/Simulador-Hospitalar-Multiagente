package renderizador;

import mapa.GridHospital;
import mapa.Bloco;
import processing.core.PApplet;
import processing.core.PImage;

public class RenderizadorMapa {
    private final PApplet app;
    private final PImage[] sprites;
    public static final int TAMANHO_CELULA = 25;

    public RenderizadorMapa(PApplet app) {
        if (app == null) 
            throw new IllegalArgumentException("PApplet não pode ser null.");

        this.app = app;
        sprites = new PImage[128];
        carregarSprites();
    }

    private void carregarSprites() {
        sprites['.'] = app.loadImage("sprites/chao.png");
        sprites['#'] = app.loadImage("sprites/parede.png");
        sprites['G'] = app.loadImage("sprites/gerador.png");
        sprites['R'] = app.loadImage("sprites/removedor.png");
        sprites['T'] = app.loadImage("sprites/totem.png");
        sprites['A'] = app.loadImage("sprites/assento.png");
        sprites['E'] = app.loadImage("sprites/enfermeira.png");
        sprites['M'] = app.loadImage("sprites/doutor.png");
    }

    public void renderizar(GridHospital hospital) {
        if (hospital == null) 
            return;

        for (int linha = 0; linha < hospital.getLinhas(); linha++) {
            for (int coluna = 0; coluna < hospital.getColunas(); coluna++) {
                Bloco bloco = hospital.getBloco(linha, coluna);
                if (bloco != null) {
                    renderizarBloco(bloco);
                }
            }
        }
    }

    public void renderizarBloco(Bloco bloco) {
        if (bloco == null) 
            return;

        char tipo = bloco.getTipo();
        float x = bloco.getColuna() * TAMANHO_CELULA;
        float y = bloco.getLinha() * TAMANHO_CELULA;
        
        desenharSprite(tipo, x, y, TAMANHO_CELULA);
    }

    private void desenharSprite(char tipo, float x, float y, float tamanho) {
        // Uso direto da sua função getSprite()
        PImage sprite = getSprite(tipo);
        
        if (sprite != null) {
            app.image(sprite, x, y, tamanho, tamanho);
        } else {
            // Desenho de fallback (retângulo cinza) caso a imagem do sprite falhe ao carregar
            app.fill(100);
            app.rect(x, y, tamanho, tamanho);
        }
    }

    public PImage getSprite(char tipo) {
        if (tipo >= 0 && tipo < sprites.length) {
            return sprites[tipo];
        }
        return null;
    }
}