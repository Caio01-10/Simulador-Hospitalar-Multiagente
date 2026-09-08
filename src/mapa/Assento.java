package mapa;
import modelo.Paciente;

public class Assento {
    private boolean reservado;
    private Paciente ocupante;

    public Assento() {
        reservado = false;
        ocupante = null;
    }

    public boolean estaReservado() {
        return reservado;
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public boolean tentaOcupar(Paciente paciente) {
        if (paciente == null || reservado) 
            return false;
        reservado = true;
        ocupante = paciente;
        return true;
    }

    public void liberar() {
        reservado = false;
        ocupante = null;
    }
}