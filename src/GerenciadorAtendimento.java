public class GerenciadorAtendimento {
  FilaPacientes filaTriagemNormal;
  FilaPacientes filaTriagemPrefenrencial;
  FilaPacientes[] filasMedicas;
  int preferenciaisConsecutivos;

  public GerenciadorAtendimento(FilaPacientes filaTriagemNormal, FilaPacientes filaTriagemPreferencial, FilaPacientes[] filasMedicas, int preferenciaisConsecutivos) {
    this.filaTriagemNormal = filaTriagemNormal;
    this.filaTriagemPrefenrencial = filaTriagemPrefenrencial;
    this.filasMedicas = filasMedicas;
    this.preferenciaisConsecutivos = preferenciaisConsecutivos;
  }

  public void enfileirarTriagem(Paciente paciente) {

  }

  public Paciente chamarProximoTriagem() {

  }

  public void classificarPaciente(Paciente paciente) {

  }

  public void enfileirarConsulta(Paciente paciente) {
    
  }

  public Paciente chamarProximoConsulta() {

  }
}`
