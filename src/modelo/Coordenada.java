package modelo;

public record Coordenada(int linha, int coluna) {
  @override
  public String toString() {
    return "(" + linha + ", " + ")";
  }
}
