public class GridHospital {
    Bloco[][] grid;
    int linhas, colunas;
    Coordenada gerador, removedor, totem;
    Assento[] assentos;

    public GridHospital(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.grid = new Bloco[linhas][colunas];
        this.assentos = new Assento[linhas * colunas];
    }

    boolean carregarDeArquivo(String nomeArquivo) {
        // Implementar a lógica para carregar o grid a partir de um arquivo
        return true;
    }

    Bloco getBloco(int linha, int coluna) {
        if (linha < 0 || linha >= linhas || coluna < 0 || coluna >= colunas) {
            return null;
        }
        return grid[linha][coluna];
    }

    // Fazer tratamento de exceções para mais de um gerador, removedor. Isso deve existir no código do professor.
    // Tem que ver se a célula não é ocupada, isso é importante para o algoritimo WaveFront.
}
