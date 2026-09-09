package movimento;

import mapa.Bloco;
import mapa.GridHospital;
import modelo.Coordenada;
import modelo.Paciente;

public class GerenciadorMovimento {
    private final GridHospital grid;

    public GerenciadorMovimento(GridHospital grid) {
        if (grid == null)
            throw new IllegalArgumentException("Grid não pode ser null.");
        this.grid = grid;
    }

    public GridHospital getGrid() {
        return grid;
    }

    public boolean podeMoverPara(Coordenada destino) {
        return grid.podeTransitar(destino);
    }

    public boolean podeMoverPara(int linha, int coluna) {
        return grid.podeTransitar(linha, coluna);
    }

    public boolean saoVizinhas(Coordenada a, Coordenada b) {
        if (a == null || b == null)
            return false;
        int distanciaLinha = Math.abs(a.linha() - b.linha());
        int distanciaColuna = Math.abs(a.coluna() - b.coluna());
        return distanciaLinha + distanciaColuna == 1;
    }

    public boolean movimentoValido(Coordenada origem, Coordenada destino) {
        if (!saoVizinhas(origem, destino))
            return false;
        return podeMoverPara(destino);
    }

    public boolean moverPaciente(Paciente paciente, Coordenada novaPosicao) {
        if (paciente == null || novaPosicao == null)
            return false;

        Coordenada posicaoAtual = paciente.getPosicao();

        if (!movimentoValido(posicaoAtual, novaPosicao))
            return false;

        paciente.moverPara(novaPosicao);

        return true;
    }

    public Bloco getBloco(Coordenada coordenada) {
        return grid.getBloco(coordenada);
    }
}
