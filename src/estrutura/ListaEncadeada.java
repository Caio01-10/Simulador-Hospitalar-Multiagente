package estrutura;

import modelo.Paciente;

public class ListaEncadeada {
  private NoPaciente cabeca;
  private int tamanho;

  public void inserir(Paciente paciente) {
    if (paciente == null)
      throw new IllegalArgumentException();
    tamanho++;
    NoPaciente novoNo = new NoPaciente(paciente);
    if (cabeca == null)
      cabeca = novoNo;
    else {
      NoPaciente atual = cabeca;
      while (atual.getProximo() != null)
        atual = atual.getProximo();
      atual.setProximo(novoNo);
    }
  }

  public boolean remover(Paciente paciente) {
    if (cabeca == null || paciente == null)
      return false;
    if (cabeca.getPaciente() == paciente) {
      cabeca = cabeca.getProximo();
      tamanho--;
      return true;
    }
    NoPaciente anterior = cabeca;
    NoPaciente atual = cabeca.getProximo();
    while (atual != null) {
      if (atual.getPaciente() == paciente) {
        anterior.setProximo(atual.getProximo());
        tamanho--;
        return true;
      }
      anterior = atual;
      atual = atual.getProximo();
    }
    return false;
  }

  public Paciente getPaciente(int indice) {
    if (indice < 0 || indice >= tamanho)
      throw new IllegalArgumentException();
    NoPaciente atual = cabeca;
    for (int i = 0; i < indice; i++)
      atual = atual.getProximo();
    return atual.getPaciente();
  }

  public int tamanho() {
    return tamanho;
  }

  public boolean vazia() {
    return tamanho == 0;
  }

  public NoPaciente getCabeca() {
    return cabeca;
  }

  public void limpar() {
    cabeca = null;
    tamanho = 0;
  }
}
