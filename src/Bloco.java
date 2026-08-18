public class Bloco {
    int linha, coluna;
    char tipo; // 'C' = chão, 'P' = parede, 'G' = gerador, 'R' = removedor, 'T' = totem, 'A' = assento, 'E' = enfermeiro, 'M' = médico
    boolean ocupado;

    public Bloco(int linha, int coluna, char tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
        this.ocupado = false;
    }

    boolean isTransitavel() {
        if (tipo == 'P') return false;
        return ocupado;
    }
}