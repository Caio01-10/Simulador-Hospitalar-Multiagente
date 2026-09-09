package estrutura;

import modelo.Paciente;

public class NoPaciente {
  private final Paciente paciente;
  private NoPaciente proximo;

  public NoPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public NoPaciente getProximo() {
    return proximo;
  }

  public void setProximo(NoPaciente proximo) {
    this.proximo = proximo;
  }
}
