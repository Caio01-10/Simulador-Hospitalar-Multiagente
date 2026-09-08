package modelo;

public record Coordenada(int linha, int coluna) {
  @Override
  public String toString() {
    return "(" + linha + ", " + coluna + ")";
  }
}
