package org.example.etapas;

public class EtapaEntrevista implements Etapa {

    private final TipoEtapa siguientePositiva;

    public EtapaEntrevista(TipoEtapa siguientePositiva) {
        this.siguientePositiva = siguientePositiva;
    }

    @Override
    public TipoEtapa getTipoEtapa() {
        return TipoEtapa.ENTREVISTA;
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
