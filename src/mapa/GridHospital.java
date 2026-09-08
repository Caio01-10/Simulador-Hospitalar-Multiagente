package mapa;
import modelo.Coordenada;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GridHospital {
    private Bloco[][] grid;
    private int linhas;
    private int colunas;
    private Coordenada gerador;
    private Coordenada removedor;
    private Coordenada totem;
    private final List<Coordenada> enfermeiros;
    private final List<Coordenada> medicos;
    private final List<Coordenada> coordenadasAssentos;
    private Assento[] assentos;

    public GridHospital(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        grid = new Bloco[linhas][colunas];
        enfermeiros = new ArrayList<>();
        medicos = new ArrayList<>();
        coordenadasAssentos = new ArrayList<>();
        assentos = new Assento[0];
    }

    /**
     * Carrega um mapa de texto.
     * Caracteres esperados:
     * G = gerador
     * R = removedor
     * T = totem
     * A = assento
     * E = enfermeiro
     * M = médico
     * # = parede
     * . = chão
     */
    public boolean carregarDeArquivo(String nomeArquivo) {
        List<String> linhasArquivo = Files.readAllLines(Path.of(nomeArquivo));

        if (linhasArquivo.isEmpty())
            throw new IllegalArgumentException("Arquivo de mapa vazio.");

        int novaQuantidadeLinhas = linhasArquivo.size();
        int novaQuantidadeColunas = linhasArquivo.get(0).length();

        if (novaQuantidadeColunas == 0) 
            throw new IllegalArgumentException("Mapa sem colunas.");

        for (String linha : linhasArquivo) {
            if (linha.length() != novaQuantidadeColunas) 
                throw new IllegalArgumentException("Todas as linhas do mapa devem possuir o mesmo tamanho.");
        }

        Bloco[][] novoGrid = new Bloco[novaQuantidadeLinhas][novaQuantidadeColunas];

        Coordenada novoGerador = null;
        Coordenada novoRemovedor = null;
        Coordenada novoTotem = null;

        List<Coordenada> novosEnfermeiros = new ArrayList<>();
        List<Coordenada> novosMedicos = new ArrayList<>();
        List<Coordenada> novosAssentos = new ArrayList<>();

        for (int i = 0; i < novaQuantidadeLinhas; i++) {
            String linha = linhasArquivo.get(i);
            for (int j = 0; j < novaQuantidadeColunas; j++) {
                char tipo = linha.charAt(j);

                if (!tipoValido(tipo)) 
                    throw new IllegalArgumentException("Caractere inválido no mapa: '" + tipo + "' em (" + i + ", " + j + ")");

                novoGrid[i][j] = new Bloco(i, j, tipo);
                Coordenada coordenada = new Coordenada(i, j);
                switch (tipo) {
                    case 'G':
                        if (novoGerador != null) 
                            throw new IllegalArgumentException("O mapa possui mais de um gerador.");

                        novoGerador = coordenada;
                        break;
                    case 'R':
                        if (novoRemovedor != null) 
                            throw new IllegalArgumentException("O mapa possui mais de um removedor.");

                        novoRemovedor = coordenada;
                        break;
                    case 'T':
                        if (novoTotem != null) 
                            throw new IllegalArgumentException("O mapa possui mais de um totem.");

                        novoTotem = coordenada;
                        break;
                    case 'A':
                        novosAssentos.add(coordenada);
                        break;
                    case 'E':
                        novosEnfermeiros.add(coordenada);
                        break;
                    case 'M':
                        novosMedicos.add(coordenada);
                        break;
                    default:
                        break;
                }
            }
        }
        if (novoGerador == null) 
            throw new IllegalArgumentException("O mapa precisa possuir um gerador.");

        if (novoRemovedor == null) 
            throw new IllegalArgumentException("O mapa precisa possuir um removedor.");
        
        if (novoTotem == null) 
            throw new IllegalArgumentException("O mapa precisa possuir um totem.");
        
        // Só altera o objeto depois que o mapa inteiro foi validado.
        this.grid = novoGrid;
        this.linhas = novaQuantidadeLinhas;
        this.colunas = novaQuantidadeColunas;

        this.gerador = novoGerador;
        this.removedor = novoRemovedor;
        this.totem = novoTotem;

        this.enfermeiros.clear();
        this.enfermeiros.addAll(novosEnfermeiros);

        this.medicos.clear();
        this.medicos.addAll(novosMedicos);

        this.coordenadasAssentos.clear();
        this.coordenadasAssentos.addAll(novosAssentos);

        this.assentos = new Assento[novosAssentos.size()];

        for (int i = 0; i < assentos.length; i++) {
            assentos[i] = new Assento();
        }

        return true;
    }

    private boolean tipoValido(char tipo) {
        return tipo == '.' || tipo == '#' || tipo == 'G' || tipo == 'R' || tipo == 'T' || tipo == 'A' || tipo == 'E' || tipo == 'M';
    }

    public Bloco getBloco(int linha, int coluna) {
        if (!coordenadaValida(linha, coluna)) {
            return null;
        }
        return grid[linha][coluna];
    }

    public Bloco getBloco(Coordenada coordenada) {
        if (coordenada == null) {
            return null;
        }
        return getBloco(coordenada.linha(), coordenada.coluna());
    }

    public boolean coordenadaValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    public boolean podeTransitar(int linha, int coluna) {
        Bloco bloco = getBloco(linha, coluna);
        return bloco != null && bloco.isTransitavel();
    }

    public boolean podeTransitar(Coordenada coordenada) {
        if (coordenada == null) {
            return false;
        }
        return podeTransitar(coordenada.linha(), coordenada.coluna());
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Bloco[][] getGrid() {
        return grid;
    }

    public Coordenada getGerador() {
        return gerador;
    }

    public Coordenada getRemovedor() {
        return removedor;
    }

    public Coordenada getTotem() {
        return totem;
    }

    public List<Coordenada> getEnfermeiros() {
        return List.copyOf(enfermeiros);
    }

    public List<Coordenada> getMedicos() {
        return List.copyOf(medicos);
    }

    public List<Coordenada> getCoordenadasAssentos() {
        return List.copyOf(coordenadasAssentos);
    }

    public Assento[] getAssentos() {
        return assentos.clone();
    }
}