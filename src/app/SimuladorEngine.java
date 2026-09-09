package app;

import java.util.Random;

import atendimento.GerenciadorAtendimento;
import enums.EstadoPaciente;
import enums.EstadoTela;
import estrutura.ListaEncadeada;
import mapa.Assento;
import mapa.Bloco;
import mapa.GridHospital;
import mapa.Totem;
import modelo.Coordenada;
import modelo.Paciente;
import movimento.GerenciadorMovimento;
import movimento.GerenciadorTempo;
import movimento.WavefrontPathfinder;
import processing.core.PApplet;

/**
 * Motor da simulação: liga o grid, as filas de atendimento, o pathfinding
 * (Wavefront) e o relógio de tempo, avançando o estado de cada paciente a
 * cada quadro (update). Não desenha nada — isso é responsabilidade do
 * RenderizadorHospital.
 */
public class SimuladorEngine {
    // --- Parâmetros fixos exigidos pela especificação ---
    private static final float MEDIA_SPAWN = 5.0f;

    private static final float MEDIA_TRIAGEM = 6.0f;
    private static final float DESVIO_TRIAGEM = 2.0f;
    private static final float MINIMO_TRIAGEM = 2.0f;

    private static final float MEDIA_CONSULTA = 12.0f;
    private static final float DESVIO_CONSULTA = 4.0f;
    private static final float MINIMO_CONSULTA = 4.0f;

    private static final int[] DESLOC_LINHA = { -1, 1, 0, 0 };
    private static final int[] DESLOC_COLUNA = { 0, 0, -1, 1 };

    private EstadoTela estadoAtual;
    private final ListaEncadeada pacientesAtivos;

    private final GridHospital grid;
    private final GerenciadorMovimento movimento;
    private final WavefrontPathfinder pathfinder;
    private GerenciadorAtendimento atendimento;
    private final GerenciadorTempo relogioSpawn;
    private Totem totem;

    private final Random random;
    private float tempoAtual;
    private int proximoId;
    private int totalAtendidos;

    // Estado de cada enfermeira/médico (arrays paralelos aos vetores do grid).
    private final Coordenada[] adjacentesEnfermeiros;
    private final Paciente[] pacienteEnfermeira;
    private final boolean[] emAtendimentoEnfermeira;
    private final float[] tempoRestanteEnfermeira;

    private final Coordenada[] adjacentesMedicos;
    private final Paciente[] pacienteMedico;
    private final boolean[] emAtendimentoMedico;
    private final float[] tempoRestanteMedico;

    /**
     * @param app             referência ao PApplet (necessário só para carregar o
     *                        arquivo de mapa através de loadStrings).
     * @param nomeArquivoMapa nome do arquivo dentro de data/mapas, ex: "mapa1.txt".
     */
    public SimuladorEngine(PApplet app, String nomeArquivoMapa) {
        if (app == null)
            throw new IllegalArgumentException("PApplet não pode ser null.");

        this.pacientesAtivos = new ListaEncadeada();
        this.random = new Random();
        this.grid = new GridHospital();

        boolean carregado = grid.carregarDeArquivo(app, "mapas/" + nomeArquivoMapa);
        if (!carregado) {
            // Tenta também sem o prefixo, caso o chamador já o tenha incluído.
            carregado = grid.carregarDeArquivo(app, nomeArquivoMapa);
        }
        if (!carregado) {
            throw new IllegalStateException("Não foi possível carregar o mapa: " + nomeArquivoMapa);
        }

        this.movimento = new GerenciadorMovimento(grid);
        this.pathfinder = new WavefrontPathfinder(grid);
        this.atendimento = new GerenciadorAtendimento();
        this.relogioSpawn = new GerenciadorTempo(MEDIA_SPAWN);
        this.totem = new Totem();

        Coordenada[] enfermeiros = grid.getEnfermeiros();
        adjacentesEnfermeiros = new Coordenada[enfermeiros.length];
        pacienteEnfermeira = new Paciente[enfermeiros.length];
        emAtendimentoEnfermeira = new boolean[enfermeiros.length];
        tempoRestanteEnfermeira = new float[enfermeiros.length];
        for (int i = 0; i < enfermeiros.length; i++) {
            adjacentesEnfermeiros[i] = encontrarCelulaAdjacenteLivre(enfermeiros[i]);
        }

        Coordenada[] medicos = grid.getMedicos();
        adjacentesMedicos = new Coordenada[medicos.length];
        pacienteMedico = new Paciente[medicos.length];
        emAtendimentoMedico = new boolean[medicos.length];
        tempoRestanteMedico = new float[medicos.length];
        for (int i = 0; i < medicos.length; i++) {
            adjacentesMedicos[i] = encontrarCelulaAdjacenteLivre(medicos[i]);
        }

        this.tempoAtual = 0;
        this.proximoId = 0;
        this.totalAtendidos = 0;
        this.estadoAtual = EstadoTela.EM_EXECUCAO;
    }

    // =====================================================================
    // CICLO PRINCIPAL
    // =====================================================================

    public void update(float deltaTime) {
        if (estadoAtual != EstadoTela.EM_EXECUCAO)
            return;

        tempoAtual += deltaTime;

        atualizarSpawn();
        atualizarChegadaAoTotem();
        atualizarAssentos();
        atualizarTriagem(deltaTime);
        atualizarConsulta(deltaTime);
        moverPacientes();
        atualizarRemocao();
    }

    // =====================================================================
    // NASCIMENTO DE PACIENTES (Gerador)
    // =====================================================================

    private void atualizarSpawn() {
        if (!relogioSpawn.chegouHora(tempoAtual))
            return;

        Coordenada gerador = grid.getGerador();
        if (grid.podeTransitar(gerador)) {
            criarPaciente(gerador);
            relogioSpawn.resetar(tempoAtual);
        }
        // Se o gerador estiver momentaneamente ocupado, a tentativa é refeita
        // no próximo quadro (o temporizador só é reiniciado após o nascimento
        // efetivo, evitando "perder" chegadas).
    }

    private void criarPaciente(Coordenada gerador) {
        proximoId++;
        boolean preferencial = random.nextDouble() < 0.25; // 25% preferencial, 75% normal
        int[] sinaisVitais = gerarSinaisVitais();

        Paciente paciente = new Paciente(proximoId, preferencial, sinaisVitais, gerador);
        paciente.alterarEstado(EstadoPaciente.INDO_TOTEM);
        paciente.definirDestino(grid.getTotem());

        marcarBlocoOcupado(gerador);
        pacientesAtivos.inserir(paciente);
    }

    private int[] gerarSinaisVitais() {
        int saturacao = 70 + random.nextInt(31); // 70 a 100
        int temperatura = 34 + random.nextInt(9); // 34 a 42
        int nivelDor = random.nextInt(11); // 0 a 10
        int conscienciaAlterada = random.nextInt(20) == 0 ? 1 : 0; // caso raro de emergência
        return new int[] { saturacao, temperatura, nivelDor, conscienciaAlterada };
    }

    // =====================================================================
    // TOTEM (retirada de senha)
    // =====================================================================

    private void atualizarChegadaAoTotem() {
        int n = pacientesAtivos.tamanho();
        Coordenada coordenadaTotem = grid.getTotem();

        for (int i = 0; i < n; i++) {
            Paciente paciente = pacientesAtivos.getPaciente(i);
            if (paciente.getEstadoAgente() != EstadoPaciente.INDO_TOTEM)
                continue;
            if (!paciente.getPosicao().equals(coordenadaTotem))
                continue;

            String senha = totem.gerarProximaSenha(paciente.getPreferencial());
            paciente.definirSenha(senha);

            atendimento.enfileirarTriagem(paciente); // já define ESPERANDO_TRIAGEM
            if (!atribuirAssentoEDestino(paciente)) {
                // Nenhum assento livre no momento: aguarda parado até uma vaga surgir
                // (nova tentativa ocorre em atualizarAssentos()).
                paciente.definirDestino(paciente.getPosicao());
            }
        }
    }

    // =====================================================================
    // ASSENTOS (reserva, ocupação e novas tentativas)
    // =====================================================================

    private void atualizarAssentos() {
        Assento[] assentos = grid.getAssentos();
        Coordenada[] coordenadasAssentos = grid.getCoordenadasAssentos();
        int n = pacientesAtivos.tamanho();

        for (int i = 0; i < n; i++) {
            Paciente paciente = pacientesAtivos.getPaciente(i);
            EstadoPaciente estado = paciente.getEstadoAgente();
            boolean esperandoAssento = estado == EstadoPaciente.ESPERANDO_TRIAGEM
                    || estado == EstadoPaciente.ESPERANDO_CONSULTA;
            if (!esperandoAssento)
                continue;

            boolean possuiAssento = false;
            for (int j = 0; j < assentos.length; j++) {
                if (assentos[j].getOcupante() == paciente) {
                    possuiAssento = true;
                    if (!assentos[j].estaOcupado() && paciente.getPosicao().equals(coordenadasAssentos[j])) {
                        assentos[j].ocupar();
                    }
                    break;
                }
            }

            if (!possuiAssento) {
                atribuirAssentoEDestino(paciente);
            }
        }
    }

    /**
     * Localiza, entre os assentos livres, o mais próximo do paciente (calculando
     * distâncias reais de caminho via Wavefront) e o reserva, definindo-o como
     * novo destino. A escolha do mais próximo usa um algoritmo de ordenação por
     * seleção codificado manualmente, conforme exigido.
     */
    private boolean atribuirAssentoEDestino(Paciente paciente) {
        Assento[] assentos = grid.getAssentos();
        Coordenada[] coordenadasAssentos = grid.getCoordenadasAssentos();
        int n = assentos.length;
        if (n == 0)
            return false;

        int[][] distancias = calcularDistanciasAPartirDe(paciente.getPosicao());
        if (distancias == null)
            return false;

        int[] indices = new int[n];
        int[] distanciasCandidatas = new int[n];
        int qtd = 0;

        for (int i = 0; i < n; i++) {
            if (assentos[i].estaReservado() || assentos[i].estaOcupado())
                continue;
            Coordenada coordenada = coordenadasAssentos[i];
            int distancia = distancias[coordenada.linha()][coordenada.coluna()];
            if (distancia < 0)
                continue;
            indices[qtd] = i;
            distanciasCandidatas[qtd] = distancia;
            qtd++;
        }

        if (qtd == 0)
            return false;

        // Ordenação por seleção (algoritmo clássico implementado manualmente).
        for (int i = 0; i < qtd - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < qtd; j++) {
                if (distanciasCandidatas[j] < distanciasCandidatas[menor])
                    menor = j;
            }
            if (menor != i) {
                int tmpDist = distanciasCandidatas[i];
                distanciasCandidatas[i] = distanciasCandidatas[menor];
                distanciasCandidatas[menor] = tmpDist;

                int tmpIdx = indices[i];
                indices[i] = indices[menor];
                indices[menor] = tmpIdx;
            }
        }

        int escolhido = indices[0];
        if (!assentos[escolhido].tentarReservar(paciente))
            return false;

        paciente.definirDestino(coordenadasAssentos[escolhido]);
        return true;
    }

    private void liberarAssentoDoPaciente(Paciente paciente) {
        Assento[] assentos = grid.getAssentos();
        for (Assento assento : assentos) {
            if (assento.getOcupante() == paciente) {
                assento.liberar();
                break;
            }
        }
    }

    // =====================================================================
    // TRIAGEM (Enfermeiras)
    // =====================================================================

    private void atualizarTriagem(float deltaTime) {
        Coordenada[] enfermeiros = grid.getEnfermeiros();

        for (int i = 0; i < enfermeiros.length; i++) {
            Paciente paciente = pacienteEnfermeira[i];

            if (paciente == null) {
                if (atendimento.temPacientesTriagem()) {
                    Paciente proximo = atendimento.chamarProximoTriagem(); // define INDO_TRIAGEM
                    if (proximo != null) {
                        liberarAssentoDoPaciente(proximo);
                        proximo.definirDestino(adjacentesEnfermeiros[i]);
                        pacienteEnfermeira[i] = proximo;
                        emAtendimentoEnfermeira[i] = false;
                    }
                }
                continue;
            }

            if (!emAtendimentoEnfermeira[i]) {
                if (paciente.getPosicao().equals(adjacentesEnfermeiros[i])) {
                    emAtendimentoEnfermeira[i] = true;
                    tempoRestanteEnfermeira[i] = tempoGaussiano(MEDIA_TRIAGEM, DESVIO_TRIAGEM, MINIMO_TRIAGEM);
                    paciente.alterarEstado(EstadoPaciente.EM_TRIAGEM);
                }
                continue;
            }

            tempoRestanteEnfermeira[i] -= deltaTime;
            if (tempoRestanteEnfermeira[i] <= 0) {
                atendimento.classificarPaciente(paciente);
                atendimento.enfileirarConsulta(paciente); // define INDO_CONSULTA (será ajustado abaixo)
                // O paciente ainda não deve caminhar até o consultório: primeiro
                // aguarda sentado a sua vez, conforme especificação.
                paciente.alterarEstado(EstadoPaciente.ESPERANDO_CONSULTA);
                atribuirAssentoEDestino(paciente);

                pacienteEnfermeira[i] = null;
                emAtendimentoEnfermeira[i] = false;
            }
        }
    }

    // =====================================================================
    // CONSULTAS (Médicos)
    // =====================================================================

    private void atualizarConsulta(float deltaTime) {
        Coordenada[] medicos = grid.getMedicos();

        for (int i = 0; i < medicos.length; i++) {
            Paciente paciente = pacienteMedico[i];

            if (paciente == null) {
                if (atendimento.temPacientesConsulta()) {
                    Paciente proximo = atendimento.chamarProximoConsulta(); // define INDO_CONSULTA
                    if (proximo != null) {
                        liberarAssentoDoPaciente(proximo);
                        proximo.definirDestino(adjacentesMedicos[i]);
                        pacienteMedico[i] = proximo;
                        emAtendimentoMedico[i] = false;
                    }
                }
                continue;
            }

            if (!emAtendimentoMedico[i]) {
                if (paciente.getPosicao().equals(adjacentesMedicos[i])) {
                    emAtendimentoMedico[i] = true;
                    tempoRestanteMedico[i] = tempoGaussiano(MEDIA_CONSULTA, DESVIO_CONSULTA, MINIMO_CONSULTA);
                    paciente.alterarEstado(EstadoPaciente.EM_CONSULTA);
                }
                continue;
            }

            tempoRestanteMedico[i] -= deltaTime;
            if (tempoRestanteMedico[i] <= 0) {
                paciente.alterarEstado(EstadoPaciente.INDO_REMOVEDOR);
                paciente.definirDestino(grid.getRemovedor());

                pacienteMedico[i] = null;
                emAtendimentoMedico[i] = false;
            }
        }
    }

    private float tempoGaussiano(float media, float desvio, float minimo) {
        double valor = media + desvio * random.nextGaussian();
        return (float) Math.max(valor, minimo);
    }

    // =====================================================================
    // MOVIMENTAÇÃO (Wavefront + resolução de conflitos em duas fases)
    // =====================================================================

    private void moverPacientes() {
        int n = pacientesAtivos.tamanho();
        if (n == 0)
            return;

        Paciente[] candidatos = new Paciente[n];
        Coordenada[] alvos = new Coordenada[n];
        int qtd = 0;

        // --- Fase 1: Intenção ---
        for (int i = 0; i < n; i++) {
            Paciente paciente = pacientesAtivos.getPaciente(i);
            Coordenada destino = paciente.getDestino();
            if (destino == null || destino.equals(paciente.getPosicao()))
                continue;

            Coordenada proximoPasso = calcularProximoPasso(paciente, destino);
            if (proximoPasso != null) {
                candidatos[qtd] = paciente;
                alvos[qtd] = proximoPasso;
                qtd++;
            }
        }

        // --- Fase 2: Resolução de conflitos ---
        // Duas entidades não podem se mover para a mesma célula no mesmo quadro;
        // em caso de disputa, o paciente de menor id (criado há mais tempo) tem
        // prioridade e os demais permanecem parados neste quadro.
        for (int i = 0; i < qtd; i++) {
            if (alvos[i] == null)
                continue;
            for (int j = i + 1; j < qtd; j++) {
                if (alvos[j] == null || !alvos[j].equals(alvos[i]))
                    continue;
                if (candidatos[j].getId() < candidatos[i].getId()) {
                    alvos[i] = null;
                    break;
                } else {
                    alvos[j] = null;
                }
            }
        }

        // --- Aplicação dos movimentos aprovados ---
        for (int i = 0; i < qtd; i++) {
            if (alvos[i] == null)
                continue;
            Paciente paciente = candidatos[i];
            Coordenada origem = paciente.getPosicao();
            if (movimento.moverPaciente(paciente, alvos[i])) {
                marcarBlocoLivre(origem);
                marcarBlocoOcupado(alvos[i]);
            }
        }
    }

    /**
     * Calcula, via Wavefront, o melhor próximo passo do paciente em direção ao
     * destino. Se a célula de menor distância estiver ocupada, tenta a segunda
     * menor, e assim sucessivamente (conforme especificado).
     */
    private Coordenada calcularProximoPasso(Paciente paciente, Coordenada destino) {
        Coordenada origem = paciente.getPosicao();

        if (origem == null || destino == null)
            return null;

        if (!destino.equals(origem) && !grid.podeTransitar(destino))
            return null;

        marcarBlocoLivre(origem);

        try {
            int[][] distancias = pathfinder.calcularWavefront(destino);

            int distanciaAtual = distancias[origem.linha()][origem.coluna()];

            if (distanciaAtual <= 0)
                return null;

            int melhorIndice = -1;
            int melhorDistancia = Integer.MAX_VALUE;

            for (int i = 0; i < DESLOC_LINHA.length; i++) {
                int novaLinha = origem.linha() + DESLOC_LINHA[i];
                int novaColuna = origem.coluna() + DESLOC_COLUNA[i];

                if (!grid.coordenadaValida(novaLinha, novaColuna))
                    continue;

                int distanciaVizinho = distancias[novaLinha][novaColuna];

                if (distanciaVizinho < 0)
                    continue;

                boolean ehODestino = novaLinha == destino.linha()
                        && novaColuna == destino.coluna();

                if (!grid.podeTransitar(novaLinha, novaColuna) && !ehODestino)
                    continue;

                if (distanciaVizinho < melhorDistancia) {
                    melhorDistancia = distanciaVizinho;
                    melhorIndice = i;
                }
            }

            if (melhorIndice == -1)
                return null;

            return new Coordenada(
                    origem.linha() + DESLOC_LINHA[melhorIndice],
                    origem.coluna() + DESLOC_COLUNA[melhorIndice]);

        } catch (IllegalArgumentException e) {
            return null;

        } finally {
            // O paciente continua ocupando sua célula original
            // até que o movimento seja efetivamente aplicado.
            marcarBlocoOcupado(origem);
        }
    }

    /**
     * Calcula a matriz de distâncias reais de caminho a partir da posição de um
     * paciente. Libera temporariamente o próprio bloco do paciente (que estaria
     * marcado como ocupado por ele mesmo) para que o Wavefront possa se propagar
     * normalmente a partir dali.
     */
    private int[][] calcularDistanciasAPartirDe(Coordenada origem) {
        marcarBlocoLivre(origem);
        try {
            return pathfinder.calcularWavefront(origem);
        } catch (IllegalArgumentException e) {
            return null;
        } finally {
            marcarBlocoOcupado(origem);
        }
    }

    // =====================================================================
    // REMOÇÃO DE PACIENTES (Removedor)
    // =====================================================================

    private void atualizarRemocao() {
        Coordenada removedor = grid.getRemovedor();
        for (int i = pacientesAtivos.tamanho() - 1; i >= 0; i--) {
            Paciente paciente = pacientesAtivos.getPaciente(i);
            if (paciente.getEstadoAgente() == EstadoPaciente.INDO_REMOVEDOR
                    && paciente.getPosicao().equals(removedor)) {
                marcarBlocoLivre(paciente.getPosicao());
                pacientesAtivos.remover(paciente);
                totalAtendidos++;
            }
        }
    }

    // =====================================================================
    // AUXILIARES DE GRID
    // =====================================================================

    private void marcarBlocoOcupado(Coordenada coordenada) {
        Bloco bloco = grid.getBloco(coordenada);
        if (bloco != null)
            bloco.setOcupado(true);
    }

    private void marcarBlocoLivre(Coordenada coordenada) {
        Bloco bloco = grid.getBloco(coordenada);
        if (bloco != null)
            bloco.setOcupado(false);
    }

    /**
     * Encontra a primeira célula de chão (não-parede) adjacente a uma estação fixa.
     */
    private Coordenada encontrarCelulaAdjacenteLivre(Coordenada estacao) {
        for (int i = 0; i < DESLOC_LINHA.length; i++) {
            int novaLinha = estacao.linha() + DESLOC_LINHA[i];
            int novaColuna = estacao.coluna() + DESLOC_COLUNA[i];
            if (!grid.coordenadaValida(novaLinha, novaColuna))
                continue;
            Bloco bloco = grid.getBloco(novaLinha, novaColuna);
            if (bloco != null && bloco.getTipo() != '#') {
                return new Coordenada(novaLinha, novaColuna);
            }
        }
        return estacao; // fallback defensivo (não deve ocorrer em mapas bem formados)
    }

    // =====================================================================
    // CONTROLE DE TELA (pausa / continuar / resetar)
    // =====================================================================

    public void pausar() {
        estadoAtual = EstadoTela.PAUSADO;
    }

    public void continuar() {
        estadoAtual = EstadoTela.EM_EXECUCAO;
    }

    /**
     * Reinicia a simulação por completo: limpa pacientes, filas, assentos,
     * ocupação física do grid e contadores do totem, sem recarregar o arquivo
     * de mapa (o layout físico permanece o mesmo).
     */
    public void reiniciarTela() {
        pacientesAtivos.limpar();
        tempoAtual = 0;
        proximoId = 0;
        totalAtendidos = 0;
        estadoAtual = EstadoTela.EM_EXECUCAO;
        relogioSpawn.resetar(tempoAtual);

        atendimento = new GerenciadorAtendimento();
        totem = new Totem();

        for (int linha = 0; linha < grid.getLinhas(); linha++) {
            for (int coluna = 0; coluna < grid.getColunas(); coluna++) {
                Bloco bloco = grid.getBloco(linha, coluna);
                if (bloco != null)
                    bloco.setOcupado(false);
            }
        }

        for (Assento assento : grid.getAssentos()) {
            assento.liberar();
        }

        for (int i = 0; i < pacienteEnfermeira.length; i++) {
            pacienteEnfermeira[i] = null;
            emAtendimentoEnfermeira[i] = false;
            tempoRestanteEnfermeira[i] = 0;
        }
        for (int i = 0; i < pacienteMedico.length; i++) {
            pacienteMedico[i] = null;
            emAtendimentoMedico[i] = false;
            tempoRestanteMedico[i] = 0;
        }
    }

    // =====================================================================
    // GETTERS
    // =====================================================================

    public EstadoTela getEstadoAtual() {
        return estadoAtual;
    }

    public ListaEncadeada getPacientesAtivos() {
        return pacientesAtivos;
    }

    public GridHospital getGrid() {
        return grid;
    }

    public GerenciadorMovimento getMovimento() {
        return movimento;
    }

    public GerenciadorAtendimento getAtendimento() {
        return atendimento;
    }

    public GerenciadorTempo getTempo() {
        return relogioSpawn;
    }

    public float getTempoAtual() {
        return tempoAtual;
    }

    public int getTotalAtendidos() {
        return totalAtendidos;
    }
}
