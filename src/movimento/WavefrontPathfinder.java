package movimento;
import modelo.Coordenada;
import mapa.GridHospital;

public class WavefrontPathfinder {
 private final GridHospital grid;

 public WavefrontPathfinder(GridHospital grid) {
  if (grid == null) 
   throw new IllegalArgumentException("Grid não pode ser null.");

  this.grid = grid;
 }
 public int[][] calcularWavefront(Coordenada destino) {
  if (destino == null) 
   throw new IllegalArgumentException("Destino não pode ser null.");

  int linhas = grid.getLinhas();
  int colunas = grid.getColunas();
  if (!grid.podeTransitar(destino)) 
   throw new IllegalArgumentException("O destino não é uma posição transitável: " + destino);

  int[][] distancia = new int[linhas][colunas];
  // Inicialmente nenhuma posição foi visitada.
  for (int linha = 0; linha < linhas; linha++) {
   for (int coluna = 0; coluna < colunas; coluna++) {
    distancia[linha][coluna] = -1;
   }
  }

  FilaCoordenadas fila = new FilaCoordenadas();
  // O Wavefront começa no destino.
  distancia[destino.linha()][destino.coluna()] = 0;
  fila.enfileirar(destino);
  // Cima, baixo, esquerda e direita.
  int[] deslocamentoLinha = {-1, 1, 0, 0};
  int[] deslocamentoColuna = {0, 0, -1, 1};
 
  while (!fila.vazia()) {
   Coordenada atual = fila.desenfileirar();
   for (int i = 0; i < 4; i++) {
    int novaLinha = atual.linha() + deslocamentoLinha[i];
    int novaColuna = atual.coluna() + deslocamentoColuna[i];
 
    Coordenada vizinho = new Coordenada(novaLinha, novaColuna);
 
    if (!estaDentroDoMapa(vizinho)) continue;
    // O GridHospital decide se é parede, médico, enfermeira etc.
    if (!grid.podeTransitar(vizinho)) continue;
    // Se já foi visitado, não precisamos adicioná-lo novamente.
    if (distancia[novaLinha][novaColuna] != -1) continue;
 
    distancia[novaLinha][novaColuna] = distancia[atual.linha()][atual.coluna()] + 1;
    fila.enfileirar(vizinho);
   }
  }
 return distancia;
 }

 private boolean estaDentroDoMapa(Coordenada coordenada) {
  return coordenada.linha() >= 0 && coordenada.linha() < grid.getLinhas() && coordenada.coluna() >= 0 && coordenada.coluna() < grid.getColunas();
 }
}
