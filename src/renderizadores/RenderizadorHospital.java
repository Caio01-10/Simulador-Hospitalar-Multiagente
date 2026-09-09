package renderizadores;

import app.SimuladorEngine;
import processing.core.PApplet;

public class RenderizadorHospital {
    private final RenderizadorMapa renderizadorMapa;
    private final RenderizadorAgente renderizadorAgente;

    public RenderizadorHospital(PApplet app) {
        if (app == null)
            throw new IllegalArgumentException("PApplet não pode ser null.");

        renderizadorMapa = new RenderizadorMapa(app);

        renderizadorAgente = new RenderizadorAgente(app);
    }

    /**
     * Desenha o hospital inteiro.
     *
     * Ordem:
     * 1. mapa
     * 2. pacientes
     * 3. HUD
     */
    public void renderizar(PApplet app, SimuladorEngine engine) {
        if (engine == null)
            return;

        app.pushStyle();
        app.background(20);

        // 1. MAPA
        if (engine.getGrid() != null)
            renderizadorMapa.renderizar(engine.getGrid());

        // 2. PACIENTES
        if (engine.getPacientesAtivos() != null)
            renderizadorAgente.renderizarPacientes(engine.getPacientesAtivos());

        // 3. HUD
        desenharHUD(app, engine);
        app.popStyle();
    }

    private void desenharHUD(PApplet app, SimuladorEngine engine) {
        app.pushStyle();

        app.fill(0, 0, 0, 180);
        app.rect(0, app.height - 55, app.width, 55);

        app.fill(255);
        app.textSize(14);
        app.textAlign(PApplet.LEFT, PApplet.CENTER);

        int aguardandoTriagem = 0;
        if (engine.getAtendimento() != null) {
            aguardandoTriagem = engine.getAtendimento().getFilaTriagemNormal().tamanho()
                    + engine.getAtendimento().getFilaTriagemPreferencial().tamanho();
        }

        app.text("Ativos: " + engine.getPacientesAtivos().tamanho(), 150, app.height - 35);
        app.text("Aguard. Triagem: " + aguardandoTriagem, 260, app.height - 35);
        app.text("Atendidos: " + engine.getTotalAtendidos(), 430, app.height - 35);
        app.text(String.format("Tempo: %.1f s", engine.getTempoAtual()), 560, app.height - 35);

        app.popStyle();
    }

    public RenderizadorMapa getRenderizadorMapa() {
        return renderizadorMapa;
    }

    public RenderizadorAgente getRenderizadorAgente() {
        return renderizadorAgente;
    }
}
