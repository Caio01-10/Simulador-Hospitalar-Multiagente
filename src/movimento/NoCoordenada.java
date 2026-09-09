package movimento;

import modelo.Coordenada;

public class NoCoordenada {
  private final Coordenada valor;
  private NoCoordenada proximo;

  public NoCoordenada(Coordenada valor) {
    this.valor = valor;
  }

  public Coordenada getValor() {
    return valor;
  }

  public NoCoordenada getProximo() {
    return proximo;
  }

  public void setProximo(NoCoordenada proximo) {
    this.proximo = proximo;
  }
}
