package estrutura;

import modelo.Paciente;

public class FilaPacientes {
  private NoPaciente inicio;
  private NoPaciente fim;
  private int tamanho;

  public void enfileirar(Paciente paciente) {
    if (paciente == null)
      throw new IllegalArgumentException();
    NoPaciente novo = new NoPaciente(paciente);
    if (fim == null)
      inicio = fim = novo;
    else {
      fim.setProximo(novo);
      fim = novo;
    }
    tamanho++;
  }

  public Paciente desenfileirar() {
    if (inicio == null)
      return null;
    Paciente paciente = inicio.getPaciente();
    inicio = inicio.getProximo();
    tamanho--;
    if (inicio == null)
      fim = null;
    return paciente;
  }

  public Paciente consultarInicio() {
    return inicio == null ? null : inicio.getPaciente();
  }

  public boolean vazia() {
    return inicio == null;
  }

  public int tamanho() {
    return tamanho;
  }

  public void limpar() {
    inicio = fim = null;
    tamanho = 0;
  }
}
