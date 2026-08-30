public class ListaEncadeada {
  private NoPaciente cabeca;
  private static int tamanho = 0;

  public void inserir(Paciente paciente) {
    tamanho++;
    NoPaciente novoNo = new NoPaciente(paciente);
    if (cabeca == null)
      cabeca = novoNo;
    else {
      NoPaciente atual = cabeca;
      while (atual.proximo != null)
        atual = atual.proximo;
      atual.proximo = novoNo;
    }
  }

  public void remover(Paciente paciente) {
    tamanho--;
    cabeca = null;
    cabeca = cabeca.proximo;
  }

  public Paciente getPaciente(int indice) {
    NoPaciente atual = cabeca;
    for (int i = 0; i < indice; i++) {
      atual = atual.proximo;
    }
    return atual.paciente;
  }

  public int tamanho() {
    return tamanho;
  }

  public boolean vazia() {
    if (tamanho == 0)
      return true;
    return false;
  }
}
