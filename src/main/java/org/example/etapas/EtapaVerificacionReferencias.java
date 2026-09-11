package org.example.etapas;

public class EtapaVerificacionReferencias implements Etapa {

    private final TipoEtapa siguientePositiva;

    public EtapaVerificacionReferencias(TipoEtapa siguientePositiva) {
        this.siguientePositiva = siguientePositiva;
    }

    @Override
    public TipoEtapa getTipoEtapa() {
        return TipoEtapa.VERIFICACION_REFERENCIAS;
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
