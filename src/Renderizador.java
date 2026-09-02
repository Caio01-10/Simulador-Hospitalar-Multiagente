import processing.core.PApplet;

class Renderizador {
    private Sketch sketch;
    /*private PImage spritesPacientes; 
    private PImage spritesMedicos;
    private PImage spriteEnfermeiras;
    private PImage spriteCenario;
    */
   private final int TAMANHO_CELULA = 25; //pixels de cada bloco do grid

   public Renderizador(Sketch sketch) {
      this.sketch = sketch;
      /*
        this.spritesPacientes = sketch.loadImage("sprites/pacientes.png");
        this.spritesMedicos = sketch.loadImage("sprites/medicos.png");
        this.spriteEnfermeiras = sketch.loadImage("sprites/enfermeiras.png");
        this.spriteCenario = sketch.loadImage("sprites/cenario.png");
       */  
    }


  public void renderizarGrid() {
      sketch.stroke(200);
      sketch.strokeWeight(1);
      
      for (int x = 0; x < sketch.width; x += TAMANHO_CELULA){
        for (int y = 0; y < sketch.height; y += TAMANHO_CELULA){
          sketch.noFill();
          sketch.rect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);
        }
      }
  }

  public void renderizarAgentes(Object listaPacientes) {
    //Implementação após a o armazenamento dos pacientes em uma lista
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
