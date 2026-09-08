package mapa;
import modelo.Paciente;

public class Assento {
    private boolean reservado;
    private boolean ocupado;
    private Paciente ocupante;

    public Assento() {
        this.reservado = false;
        this.ocupado = false;
        this.ocupante = null;
    }

    public boolean estaReservado() {
        return reservado;
    }

    public boolean estaOcupado() {
        return ocupado;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    /**
     * Tenta reservar o assento à distância enquanto o paciente caminha até ele.
     */
    public boolean tentarReservar(Paciente paciente) {
        if (paciente == null || reservado || ocupado) {
            return false;
        }
        this.reservado = true;
        this.ocupante = paciente;
        return true;
    }

    /**
     * Confirma a ocupação física quando o paciente senta na cadeira.
     */
    public void ocupar() {
        this.ocupado = true;
    }

    /**
     * Libera o assento quando o paciente é chamado.
     */
    public void liberar() {
        this.reservado = false;
        this.ocupado = false;
        this.ocupante = null;
    }
}