package modelo;

import enums.Cor;
import enums.EstadoPaciente;

public class Paciente {
  private final int id;
  private String senha;
  private final boolean preferencial;
  private final int[] sinaisVitais;
  private Cor corManchester;
  private Coordenada posicao;
  private Coordenada destino;
  private EstadoPaciente estadoAgente;

  public Paciente(int id, boolean preferencial, int[] sinaisVitais, Coordenada posicao) {
    this.id = id;
    this.preferencial = preferencial;
    this.sinaisVitais = sinaisVitais;
    this.posicao = posicao;
    this.estadoAgente = EstadoPaciente.NASCENDO;
  }

  public Paciente(int id, boolean preferencial, int[] sinaisVitais) {
    this(id, preferencial, sinaisVitais, new Coordenada(0, 0));
  }

  public int getId() {
    return id;
  }

  public String getSenha() {
    return senha;
  }

  public int[] getSinaisVitais() {
    return sinaisVitais;
  }

  public Cor getCorManchester() {
    return corManchester;
  }

  public Coordenada getPosicao() {
    return posicao;
  }

  public Coordenada getDestino() {
    return destino;
  }

  public EstadoPaciente getEstadoAgente() {
    return estadoAgente;
  }

  public void definirSenha(String senha) {
    this.senha = senha;
  }

  public void definirCorManchester(Cor corManchester) {
    this.corManchester = corManchester;
  }

  public void definirDestino(Coordenada destino) {
    this.destino = destino;
  }

  public void alterarEstado(EstadoPaciente estadoAgente) {
    this.estadoAgente = estadoAgente;
  }

  public void moverPara(Coordenada posicao) {
    this.posicao = posicao;
  }

  public boolean isPreferencial() {
    return preferencial;
  }
}
