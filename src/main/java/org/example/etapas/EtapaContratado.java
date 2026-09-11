package org.example.etapas;

public class EtapaContratado implements Etapa {

    private final TipoEtapa siguientePositiva;

    public EtapaContratado(TipoEtapa siguientePositiva) {
        this.siguientePositiva = siguientePositiva;
    }

    @Override
    public TipoEtapa getTipoEtapa() {
        return TipoEtapa.CONTRATADO;
    }

    @Override
    public TipoEtapa getSiguientePositiva() {
        return siguientePositiva;
    }

    @Override
    public boolean puedePasarA(TipoEtapa destino) {
        return destino.equals(siguientePositiva)
                || (destino == TipoEtapa.RECHAZADO && siguientePositiva != null);
    }
}
