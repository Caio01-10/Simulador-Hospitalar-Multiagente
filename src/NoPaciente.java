public class NoPaciente {
  public Paciente paciente;
  public NoPaciente proximo;

  public NoPaciente (Paciente paciente){
    this.paciente = paciente;
    proximo = null;
  }
}
