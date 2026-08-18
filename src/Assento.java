public class Assento {
    boolean reservado;
    Paciente ocupante;

    public Assento() {
        this.reservado = false;
        this.ocupante = null;
    }

    // Essa função deve ser ativada a distância não apenas quando o paciente estiver no assento.
    boolean tentaOcupar(Paciente paciente) {
        if (!reservado) {
            reservado = true;
            ocupante = paciente;
            return true;
        }
        return false;
    }

    void liberar() {
        reservado = false;
        ocupante = null;
    }
}
