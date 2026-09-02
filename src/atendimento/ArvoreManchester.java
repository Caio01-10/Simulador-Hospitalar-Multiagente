package atendimento;

import enums.*;

public class ArvoreManchester {
  private final NoManchester[] vetorArvore;

  public ArvoreManchester() {
    vetorArvore = new NoManchester[31];
    construirArvoreEstatica();
  }

  private Cor classificarPaciente(int[] sinaisVitais) {
    if (sinaisVitais == null || sinaisVitais.length != 4)
      throw new IllegalArgumentException("");
    int indice = 0;
    while (true) {
      NoManchester no = vetorArvore[indice];
      if (no == null)
        throw new IllegalArgumentException("");
      if (no.getFolha())
        return no.getCorResultado();
      indice = avaliarNo(no, sinaisVitais) ? 2 * indice + 1 : 2 * indice + 2;
    }
  }

  private void construirArvoreEstatica() {
    vetorArvore[0] = new NoManchester(3, 1, "==", null, false);
    vetorArvore[1] = new NoManchester(-1, 0, "", Cor.VERMELHA, true);
    vetorArvore[2] = new NoManchester(0, 92, "<", null, false);
    vetorArvore[5] = new NoManchester(-1, 0, "", Cor.LARANJA, true);
    vetorArvore[6] = new NoManchester(2, 8, ">=", null, false);
    vetorArvore[13] = new NoManchester(-1, 0, "", Cor.AMARELA, true);
    vetorArvore[14] = new NoManchester(1, 38, ">=", null, false);
    vetorArvore[29] = new NoManchester(-1, 0, "", Cor.VERDE, true);
    vetorArvore[30] = new NoManchester(-1, 0, "", Cor.AZUL, true);
  }

  private boolean avaliarNo(NoManchester no, int[] sinaisVitais) {
    float valor = sinaisVitais[no.getIndiceAtributo()];
    return switch (no.getOperador()) {
      case "==" -> valor == no.getValorCorte();
      case "<" -> valor < no.getValorCorte();
      case ">=" -> valor >= no.getValorCorte();
      default -> throw new IllegalArgumentException("");
    };
  }

  public NoManchester[] getVetorArvore() {
    return vetorArvore;
  }
}
