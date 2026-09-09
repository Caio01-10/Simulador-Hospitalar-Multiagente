package movimento;

import modelo.Coordenada;

public class FilaCoordenadas {
  private NoCoordenada inicio;
  private NoCoordenada fim;

  public void enfileirar(Coordenada coordenada) {
    NoCoordenada no = new NoCoordenada(coordenada);
    if (fim == null)
      inicio = fim = no;
    else {
      fim.setProximo(no);
      fim = no;
    }
  }

  public Coordenada desenfileirar() {
    if (inicio == null)
      return null;
    Coordenada coordenada = inicio.getValor();
    inicio = inicio.getProximo();
    if (inicio == null)
      fim = null;
    return coordenada;
  }

  public boolean vazia() {
    return inicio == null;
  }
}
