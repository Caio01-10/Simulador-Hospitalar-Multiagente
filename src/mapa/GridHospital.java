package mapa;

import modelo.Coordenada;
import processing.core.PApplet;

public class GridHospital {
    private Bloco[][] grid;
    private int linhas;
    private int colunas;
    
    private Coordenada gerador;
    private Coordenada removedor;
    private Coordenada totem;

    private Coordenada[] enfermeiros;
    private Coordenada[] medicos;
    private Coordenada[] coordenadasAssentos;
    private Assento[] assentos;

    public GridHospital() {
        this.linhas = 0;
        this.colunas = 0;
        this.enfermeiros = new Coordenada[0];
        this.medicos = new Coordenada[0];
        this.coordenadasAssentos = new Coordenada[0];
        this.assentos = new Assento[0];
    }

    /**
     * Carrega o mapa utilizando a função nativa loadStrings do Processing.
     */
    public boolean carregarDeArquivo(PApplet app, String caminhoArquivo) {
        String[] linhasArquivo = app.loadStrings(caminhoArquivo);

        if (linhasArquivo == null || linhasArquivo.length < 2) {
            return false;
        }

        // A primeira linha do arquivo é um cabeçalho com as dimensões do mapa,
        // no formato "numLinhas numColunas" (ex: "24 24"). O grid propriamente
        // dito começa apenas na linha seguinte.
        String[] dimensoes = linhasArquivo[0].trim().split("\\s+");
        if (dimensoes.length != 2) {
            return false;
        }

        int tempLinhas;
        int tempColunas;
        try {
            tempLinhas = Integer.parseInt(dimensoes[0]);
            tempColunas = Integer.parseInt(dimensoes[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (tempLinhas <= 0 || tempColunas <= 0) {
            return false;
        }

        // O restante do arquivo (sem contar o cabeçalho) precisa ter exatamente
        // 'tempLinhas' linhas, senão o arquivo está mal formatado.
        if (linhasArquivo.length - 1 != tempLinhas) {
            return false;
        }

        Bloco[][] tempGrid = new Bloco[tempLinhas][tempColunas];
        
        Coordenada tempGerador = null;
        Coordenada tempRemovedor = null;
        Coordenada tempTotem = null;

        // Contadores para dimensionamento estático de vetores
        int qtdEnfermeiros = 0;
        int qtdMedicos = 0;
        int qtdAssentos = 0;

        // Primeira passagem: validação e contagem
        for (int i = 0; i < tempLinhas; i++) {
            String linha = linhasArquivo[i + 1].trim(); // +1 para pular o cabeçalho de dimensões
            if (linha.length() != tempColunas) {
                return false;
            }

            for (int j = 0; j < tempColunas; j++) {
                char tipo = linha.charAt(j);
                if (!tipoValido(tipo)) return false;

                if (tipo == 'E') qtdEnfermeiros++;
                else if (tipo == 'M') qtdMedicos++;
                else if (tipo == 'A') qtdAssentos++;
            }
        }

        Coordenada[] tempEnfermeiros = new Coordenada[qtdEnfermeiros];
        Coordenada[] tempMedicos = new Coordenada[qtdMedicos];
        Coordenada[] tempCoordenadasAssentos = new Coordenada[qtdAssentos];
        Assento[] tempAssentos = new Assento[qtdAssentos];

        int idxE = 0, idxM = 0, idxA = 0;

        // Segunda passagem: população de estruturas
        for (int i = 0; i < tempLinhas; i++) {
            String linha = linhasArquivo[i + 1].trim(); // +1 para pular o cabeçalho de dimensões
            for (int j = 0; j < tempColunas; j++) {
                char tipo = linha.charAt(j);
                tempGrid[i][j] = new Bloco(i, j, tipo);
                Coordenada coord = new Coordenada(i, j);

                switch (tipo) {
                    case 'G':
                        tempGerador = coord;
                        break;
                    case 'R':
                        tempRemovedor = coord;
                        break;
                    case 'T':
                        tempTotem = coord;
                        break;
                    case 'E':
                        tempEnfermeiros[idxE++] = coord;
                        break;
                    case 'M':
                        tempMedicos[idxM++] = coord;
                        break;
                    case 'A':
                        tempCoordenadasAssentos[idxA] = coord;
                        tempAssentos[idxA] = new Assento();
                        idxA++;
                        break;
                }
            }
        }

        if (tempGerador == null || tempRemovedor == null || tempTotem == null) {
            return false;
        }

        // Atribuição ao estado da instância
        this.grid = tempGrid;
        this.linhas = tempLinhas;
        this.colunas = tempColunas;
        this.gerador = tempGerador;
        this.removedor = tempRemovedor;
        this.totem = tempTotem;
        this.enfermeiros = tempEnfermeiros;
        this.medicos = tempMedicos;
        this.coordenadasAssentos = tempCoordenadasAssentos;
        this.assentos = tempAssentos;

        return true;
    }

    private boolean tipoValido(char tipo) {
        return tipo == '.' || tipo == '#' || tipo == 'G' || tipo == 'R' || 
               tipo == 'T' || tipo == 'A' || tipo == 'E' || tipo == 'M';
    }

    public Bloco getBloco(int linha, int coluna) {
        if (!coordenadaValida(linha, coluna)) return null;
        return grid[linha][coluna];
    }

    public boolean coordenadaValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }
    public Bloco[][] getGrid() { return grid; }
    public Coordenada getGerador() { return gerador; }
    public Coordenada getRemovedor() { return removedor; }
    public Coordenada getTotem() { return totem; }
    public Coordenada[] getEnfermeiros() { return enfermeiros; }
    public Coordenada[] getMedicos() { return medicos; }
    public Coordenada[] getCoordenadasAssentos() { return coordenadasAssentos; }
    public Assento[] getAssentos() { return assentos; }
}