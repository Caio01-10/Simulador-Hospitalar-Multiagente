package movimento;
import modelo.Coordenada;
public class WavefrontPathfinder {
 public int[][] calcularWavefront(int destinoLinha, int destinoColuna, char[][] mapa){
  if(mapa == null || mapa.length == 0 || mapa[0].length == 0)throw new IllegalArgumentException("Mapa vazio."); // Montijo trata.
  int linhas = mapa.length;
  int colunas = mapa[0].length;
  int[][] distancia = new int[linhas][colunas];

  for(int i = 0; i < linhas; i++){
   if(mapa[i].length != colunas) throw new IllegalArgumentException("Mapa irregular."); // Montijo trata.
   for(int j = 0; j < colunas; j++) distancia[i][j] = -1;
  }

  // "#" representa a parede.
  if(destinoLinha < 0 || destinoLinha >= linhas || destinoColuna < 0 || destinoColuna >= colunas || mapa[destinoLinha][destinoColuna] == '#') throw new IllegalArgumentException("Destino invalido."); // Tem que tratar.
  
  FilaCoordenadas fila = new FilaCoordenadas();
  // O algoritmo Wavefront começa de trás para frente: do destino para a origem
  distancia[destinoLinha][destinoColuna] = 0;
  fila.enfileirar(new Coordenada(destinoLinha, destinoColuna));
  // Vetores auxiliares para varredura de vizinhos (Cima, Baixo, Esquerda, Direita)
  int[] moveLinha = {-1,1,0,0};
  int[] moveColuna = {0,0,-1,1};

  // Processa os pontos enquanto houver posições alcançáveis na fila
  while(!fila.vazia()){
   Coordenada atual = fila.desenfileirar();

   // Varre as 4 direções adjacentes do ponto atual
   for(int k = 0; k < 4; k++){
    int novaLinha = atual.linha() + moveLinha[k];
    int novaColuna = atual.coluna() + moveColuna[k];
    
    // Valida se o vizinho está nos limites da grade, se não é obstáculo e se ainda não foi visitado
    if(novaLinha >= 0 && novaLinha < linhas && novaColuna >= 0 && novaColuna < colunas && mapa[novaLinha][novaColuna] != '#' && distancia[novaLinha][novaColuna] == -1){
     // Define o custo do vizinho como o custo do nó atual + 1 passo
     distancia[novaLinha][novaColuna] = distancia[atual.linha()][atual.coluna()] + 1;
     // Adiciona o vizinho na fila para expandir a onda a partir dele
     fila.enfileirar(new Coordenada(novaLinha, novaColuna));
    }
   }
  }
  return distancia;
 }
}
