package mapa;

public class Totem {
    private int contadorNormal;
    private int contadorPreferencial;

    public Totem() {
        contadorNormal = 0;
        contadorPreferencial = 0;
    }

    public String gerarProximaSenha(boolean preferencial) {
        if (preferencial) {
            contadorPreferencial++;
            return String.format("P%03d", contadorPreferencial);
        }
        contadorNormal++;
        return String.format("N%03d", contadorNormal);
    }

    public int getContadorNormal() {
        return contadorNormal;
    }

    public int getContadorPreferencial() {
        return contadorPreferencial;
    }
}
