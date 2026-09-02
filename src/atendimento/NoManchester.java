package atendimento;

import enums.*;

public class NoManchester {
  private final int indiceAtributo;
  private final float valorCorte;
  private final String operador;
  private final Cor corResultado;
  private final boolean folha;

  public NoManchester(int indiceAtributo, float valorCorte, String operador, Cor corResultado, boolean folha) {
    this.indiceAtributo = indiceAtributo;
    this.valorCorte = valorCorte;
    this.operador = operador;
    this.corResultado = corResultado;
    this.folha = folha;
  }

  public int getIndiceAtributo() {
    return indiceAtributo;
  }

  public float getValorCorte() {
    return valorCorte;
  }

  public String getOperador() {
    return operador;
  }

  public Cor getCorResultado() {
    return corResultado;
  }

  public boolean getFolha() {
    return folha;
  }
}
