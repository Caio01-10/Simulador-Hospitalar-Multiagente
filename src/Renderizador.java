import processing.core.PApplet;
import processing.core.PImage;

public class Renderizador { // Removido o 'extends Sketch'
    private Sketch sketch;
    private PImage spritesPacientes; 
    private PImage spritesMedicos;
    private PImage spriteEnfermeiras;
    private PImage spriteCenario;

    private final int TAMANHO_CELULA = 25; // Pixels de cada bloco do grid

    public Renderizador(Sketch sketch) {
        this.sketch = sketch;
        
        try {
            // Usa sketch.dataPath para garantir o caminho correto na pasta data/
            this.spritesPacientes  = sketch.loadImage(sketch.dataPath("sprites/paciente.png"));
            this.spritesMedicos    = sketch.loadImage(sketch.dataPath("sprites/medico.png"));
            this.spriteEnfermeiras = sketch.loadImage(sketch.dataPath("sprites/enfermeira.png"));
            // this.spriteCenario  = sketch.loadImage(sketch.dataPath("sprites/cenario.png")); 
        } catch (Exception e) {
            System.out.println("Aviso [Renderizador]: Sprites não encontrados na pasta 'data'.");
        }
    }

    public void renderizarGrid() {
        sketch.stroke(200);
        sketch.strokeWeight(1);
        
        for (int x = 0; x < sketch.width; x += TAMANHO_CELULA) {
            for (int y = 0; y < sketch.height; y += TAMANHO_CELULA) {
                sketch.noFill();
                sketch.rect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
            }
        }
    }

    public void renderizarAgentes(Object listaPacientes) {
        // Implementação após o armazenamento dos pacientes em uma lista
    }

    public void renderizarHUD(Object gerenciadorTempo) {
        sketch.fill(40, 40, 40, 200);
        sketch.rect(0, sketch.height - 50, sketch.width, 50);

        sketch.fill(255);
        sketch.textSize(14);
        sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
        sketch.text("Status: Simulação Ativa", 20, sketch.height - 25);

        sketch.textAlign(PApplet.RIGHT, PApplet.CENTER);
        sketch.text("Pressione ESC para Pausar", sketch.width - 20, sketch.height - 25);
    }
}