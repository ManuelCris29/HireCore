package org.example.etapas;

public class EtapaPruebaTecnica implements Etapa {

    private final TipoEtapa siguientePositiva;

    public EtapaPruebaTecnica(TipoEtapa siguientePositiva) {
        this.siguientePositiva = siguientePositiva;
    }

    @Override
    public TipoEtapa getTipoEtapa() {
        return TipoEtapa.PRUEBA_TECNICA;
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
