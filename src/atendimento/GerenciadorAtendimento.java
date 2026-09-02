package atendimento;

import estrutura.FilaPacientes;
import enums.Cor;
import enums.EstadoPaciente;
import modelo.Paciente;

public class GerenciadorAtendimento {
  private final FilaPacientes filaTriagemNormal = new FilaPacientes();
  private final FilaPacientes filaTriagemPrefenrencial = new FilaPacientes();
  private final FilaPacientes[] filasMedicas = new FilaPacientes[5];
  private final ArvoreManchester arvoreManchester = new ArvoreManchester();
  private int preferenciaisConsecutivos;

  public GerenciadorAtendimento(FilaPacientes filaTriagemNormal, FilaPacientes filaTriagemPreferencial,
      FilaPacientes[] filasMedicas, int preferenciaisConsecutivos) {
    for (int i = 0; i < filasMedicas.length(); i++)
      filasMedicas[i] = new FilaPacientes();
  }

  public void enfileirarTriagem(Paciente paciente) {
    if (paciente == null)
      throw new IllegalArgumentException("");
    if (paciente.getPreferencial())
      filaTriagemPrefenrencial.enfileirar(paciente);
    else
      filaTriagemNormal.enfileirar(paciente);
    paciente.alterarEstado(EstadoPaciente.ESPERANDO_TRIAGEM);
  }

  public Paciente chamarProximoTriagem() {
    Paciente paciente;
    if (!filaTriagemNormal.vazia() && preferenciaisConsecutivos >= 2) {
      paciente = filaTriagemNormal.desenfileirar();
      preferenciaisConsecutivos = 0;
    } else if (!filaTriagemPrefenrencial.vazia()) {
      paciente = filaTriagemPrefenrencial.desenfileirar();
      preferenciaisConsecutivos++;
    } else if (!filaTriagemNormal.vazia()) {
      paciente = filaTriagemNormal.desenfileirar();
      preferenciaisConsecutivos = 0;
    } else
      return null;
    paciente.alterarEstado(EstadoPaciente.INDO_TRIAGEM);
    return paciente;
  }

  public void classificarPaciente(Paciente paciente) {
    if(paciente == null) throw new IllegalArgumentException("");
    paciente.definirCorManchester(arvoreManchester.classificarPaciente(paciente.getSinaisVitais();
  }

  public void enfileirarConsulta(Paciente paciente) {
    if (paciente == null || paciente.getCorManchester() == null)
      throw new IllegalArgumentException();
    filasMedicas[indiceDaCor(paciente.getCorManchester())].enfileirar(paciente);
    paciente.alterarEstado(EstadoPaciente.INDO_CONSULTA);
  }

  public Paciente chamarProximoConsulta() {
    for (int i = 0; i < filasMedicas.length; i++) {
      if (!filasMedicas[i].vazia()) {
        Paciente paciente = filasMedicas[i].desenfileirar();
        paciente.alterarEstado(EstadoPaciente.INDO_CONSULTA);
        return paciente;
      }
    }
  }

  private int indiceDaCor(Cor cor) {
    return switch (cor) {
      case VERMELHA -> 0;
      case LARANJA -> 1;
      case AMARELA -> 2;
      case VERDE -> 3;
      case AZUL -> 4;
    };
  }

  public boolean temPacientesTriagem() {
    return (!filaTriagemNormal.vazia() || !filaTriagemPrefenrencial.vazia());
  }

  public boolean temPacientesConsulta() {
    for (FilaPacientes fila : filasMedicas) {
      if (!fila.vazia())
        return true;
      return false;
    }
  }

  public ArvoreManchester getArvoreManchester() {
    return arvoreManchester;
  }

  public FilaPacientes getFilaTriagemNormal() {
    return filaTriagemNormal;
  }

  public FilaPacientes getFilaTriagemPreferencial() {
    return filaTriagemPrefenrencial;
  }
}
