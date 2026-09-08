package mapa;

public class Bloco {
    private final int linha;
    private final int coluna;
    private final char tipo;  // '.' = chão, '#' = parede, 'G' = gerador, 'R' = removedor, 'T' = totem, 'A' = assento, 'E' = enfermeiro, 'M' = médico
    private boolean ocupado;

    public Bloco(int linha, int coluna, char tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
        this.ocupado = false;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public char getTipo() {
        return tipo;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

   public boolean isTransitavel() {
        if (tipo == '#' || tipo == 'E' || tipo == 'M') {
            return false;
        }
        return !ocupado;
    }

    @Override
    public String toString() {
        return String.valueOf(tipo);
    }
}