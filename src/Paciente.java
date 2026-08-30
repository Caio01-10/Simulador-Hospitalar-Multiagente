import enumeracao.*;

public class Paciente {
  private int id;
  private String senha;
  private boolean isPreferencial;
  private int[] sinaisVitais;
  private Cor corManchester;
  private Coordenada posicao;
  private Coordenada destino;
  private int[][] mapaDistanciasAtual;
  private EstadoPaciente estadoAgente;

  public Paciente(int id, boolean isPreferencial) {
    this.id = id;
    this.isPreferencial = isPreferencial;
    // sinaisVitais
    // posicao
    // mapaDistanciasAtual
  }

  public int getId() {return id;}
  public int[] getSinaisVitais() {return sinaisVitais;}
  public boolean isPreferencial() {return isPreferencial;}
  public String getSenha() {return senha;}
  public Cor getCorManchester() {return corManchester;}

  public void definirSenha(String senha) {this.senha = senha;}
  public void definirCorManchester(Cor corManchester) {this.corManchester = corManchester;}
  public void definirDestino(Coordenada destino) {this.destino = destino;}
  public void alterarEstado(String estadoAgente) {this.estadoAgente = estadoAgente;}
}
