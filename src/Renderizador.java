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
        /*
        this.spritesPacientes = sketch.loadImage("sprites/pacientes.png");
        this.spritesMedicos = sketch.loadImage("sprites/medicos.png");
        this.spriteEnfermeiras = sketch.loadImage("sprites/enfermeiras.png");
        this.spriteCenario = sketch.loadImage("sprites/cenario.png");
       */
    }


  public void renderizarGrid(Object gridObj) {
    // 
  }

  public void renderizarAgentes(Object listaPacientes) {
    //
  }

  public void renderizarHUD(Object gerenciadorTempo) {
    sketch.fill(40, 40, 40, 200);
    sketch.rect(0, sketch.height - 50, sketch.width, 50);

    sketch.fill(255);
    sketch.textSize(14);
    sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
    sketch.text("Status: Simulação Ativa", 20, sketch.height - 25);
    sketch.text("Pressione ESC para Pausar", sketch.width - 200, sketch.height - 25);
  }
}
