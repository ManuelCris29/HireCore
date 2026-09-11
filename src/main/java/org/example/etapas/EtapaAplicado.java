package org.example.etapas;

public class EtapaAplicado implements Etapa {

    private final TipoEtapa siguientePositiva;

    public EtapaAplicado(TipoEtapa siguientePositiva) {
        this.siguientePositiva = siguientePositiva;
    }

    @Override
    public TipoEtapa getTipoEtapa() {
        return TipoEtapa.APLICADO;
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
