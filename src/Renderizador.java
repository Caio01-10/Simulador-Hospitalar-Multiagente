import processing.core.PApplet;
import processing.core.PImage;

public class Renderizador {
    PImage[] sprites;

    public Renderizador(PApplet app) {
        sprites = new PImage[128];

        sprites['.'] = app.loadImage("sprites/chao.png");
        sprites['#'] = app.loadImage("sprites/parede.png");
        sprites['G'] = app.loadImage("sprites/gerador.png");
        sprites['R'] = app.loadImage("sprites/removedor.png");
        sprites['T'] = app.loadImage("sprites/totem.png");
        sprites['A'] = app.loadImage("sprites/assento.png");
        sprites['E'] = app.loadImage("sprites/nurse.png");
        sprites['M'] = app.loadImage("sprites/doctor.png");
        sprites['P'] = app.loadImage("sprites/pacient.png");
    }

    public PImage getSprite(char tipo) {
        if (tipo < 128 && sprites[tipo] != null) {
            return sprites[tipo];
        }
        return null;
    }

    public void desenharSprite(PApplet app, char tipo, float x, float y, float tamanho) {
        PImage img = getSprite(tipo);
        if (img != null) {
            app.image(img, x, y, tamanho, tamanho);
        }
    }
}
