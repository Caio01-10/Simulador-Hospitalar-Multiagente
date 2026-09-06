package movimento;
import java.util.Random;

public class GerenciadorTempo {
    private final float mediaSpawn;
    private float proximaChegada;
    private final Random random;

    public GerenciadorTempo(float mediaSpawn) {
        if (mediaSpawn <= 0) 
            throw new IllegalArgumentException("A média de spawn deve ser maior que zero.");

        this.mediaSpawn = mediaSpawn;
        this.random = new Random();
        calcularProximaChegada();
    }

    /**
     * Gera o próximo intervalo utilizando uma distribuição exponencial em torno da média informada.
     */
    public void calcularProximaChegada() {
        double u = random.nextDouble();
        if (u == 0) 
            u = Double.MIN_VALUE;

        proximaChegada = (float) (-mediaSpawn * Math.log(1.0 - u));
    }

    public boolean chegouHora(float tempoAtual) {
        return tempoAtual >= proximaChegada;
    }

    public float getMediaSpawn() {
        return mediaSpawn;
    }

    public float getProximaChegada() {
        return proximaChegada;
    }

    public void resetar() {
        calcularProximaChegada();
    }
}
