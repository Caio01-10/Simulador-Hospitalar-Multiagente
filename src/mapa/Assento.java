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

    public boolean tentarReservar(Paciente paciente) {
        if (paciente == null || reservado || ocupado) {
            return false;
        }
        this.reservado = true;
        this.ocupante = paciente;
        return true;
    }

    public void ocupar() {
        this.ocupado = true;
    }

    public void liberar() {
        this.reservado = false;
        this.ocupado = false;
        this.ocupante = null;
    }
}