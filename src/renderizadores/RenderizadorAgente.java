package renderizadores;

import modelo.Coordenada;
import modelo.Paciente;
import enums.Cor;
import estrutura.NoPaciente;
import estrutura.ListaEncadeada;
import processing.core.PApplet;
import processing.core.PImage;

public class RenderizadorAgente {
    private final PApplet sketch;
    private PImage spritePaciente;
    private PImage spritePacientePreferencial;
    private PImage spriteMedico;
    private PImage spriteEnfermeira;

    private static final int TAMANHO_CELULA = 25;

    public RenderizadorAgente(PApplet sketch) {
        if (sketch == null)
            throw new IllegalArgumentException("PApplet não pode ser null.");

        this.sketch = sketch;
        carregarSprites();
    }

    private void carregarSprites() {
        spritePaciente = sketch.loadImage("sprites/paciente.png");
        spritePacientePreferencial = sketch.loadImage("sprites/pacientePreferencial.png");
        spriteMedico = sketch.loadImage("sprites/medico.png");
        spriteEnfermeira = sketch.loadImage("sprites/enfermeira.png");
    }

    /**
     * Desenha todos os pacientes existentes.
     */
    public void renderizarPacientes(ListaEncadeada pacientes) {
        if (pacientes == null || pacientes.vazia())
            return;

        NoPaciente atual = pacientes.getCabeca();
        while (atual != null) {
            Paciente paciente = atual.getPaciente();
            if (paciente != null)
                renderizarPaciente(paciente);

            atual = atual.getProximo();
        }
    }

    /**
     * Desenha um paciente individualmente.
     */
    public void renderizarPaciente(Paciente paciente) {
        if (paciente == null)
            return;

        Coordenada posicao = paciente.getPosicao();
        if (posicao == null)
            return;

        float x = posicao.coluna() * TAMANHO_CELULA;
        float y = posicao.linha() * TAMANHO_CELULA;

        // Brilho da cor Manchester
        desenharBrilhoManchester(paciente, x, y);

        // Escolhe o sprite
        PImage sprite;

        if (paciente.getPreferencial())
            sprite = spritePacientePreferencial;
        else
            sprite = spritePaciente;

        if (sprite != null)
            sketch.image(sprite, x, y, TAMANHO_CELULA, TAMANHO_CELULA);
        else
            desenharPacienteSemSprite(paciente, x, y);

        renderizarInformacoes(paciente, x, y);
    }

    /**
     * Caso o sprite não seja encontrado,
     * desenha um marcador simples.
     */
    private void desenharPacienteSemSprite(Paciente paciente, float x, float y) {
        sketch.pushStyle();
        sketch.fill(100);
        sketch.ellipse(x + TAMANHO_CELULA / 2.0f, y + TAMANHO_CELULA / 2.0f, TAMANHO_CELULA * 0.7f,
                TAMANHO_CELULA * 0.7f);
        sketch.popStyle();
    }

    /**
     * Mostra informações básicas do paciente.
     */

    private void desenharBrilhoManchester(Paciente paciente, float x, float y) {
        if (paciente.getCorManchester() == null)
            return;

        int cor = obterCorManchester(paciente.getCorManchester());

        sketch.pushStyle();

        // Cor do brilho
        sketch.fill(cor, 120);

        // Sem contorno
        sketch.noStroke();

        // Desenha atrás do paciente
        sketch.ellipse(x + TAMANHO_CELULA / 2.0f, y + TAMANHO_CELULA / 2.0f, TAMANHO_CELULA * 1.4f,
                TAMANHO_CELULA * 1.4f);

        sketch.popStyle();
    }

    private int obterCorManchester(Cor cor) {
        return switch (cor) {
            case VERMELHA -> sketch.color(255, 0, 0);
            case LARANJA -> sketch.color(255, 165, 0);
            case AMARELA -> sketch.color(255, 255, 0);
            case VERDE -> sketch.color(0, 200, 0);
            case AZUL -> sketch.color(0, 100, 255);
        };
    }

    private void renderizarInformacoes(Paciente paciente, float x, float y) {
        sketch.pushStyle();
        sketch.textSize(9);
        sketch.fill(0);
        if (paciente.getSenha() != null)
            sketch.text(paciente.getSenha(), x + 2, y + 10);

        sketch.popStyle();
    }

    public PImage getSpritePaciente() {
        return spritePaciente;
    }

    public PImage getSpriteMedico() {
        return spriteMedico;
    }

    public PImage getSpriteEnfermeira() {
        return spriteEnfermeira;
    }
}
