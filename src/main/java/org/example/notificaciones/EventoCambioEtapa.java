package org.example.notificaciones;

import org.example.dominio.Candidato;
import org.example.etapas.TipoEtapa;

public class EventoCambioEtapa {

    private final Candidato candidato;
    private final TipoEtapa etapaAnterior;
    private final TipoEtapa etapaNueva;
    private final boolean esNotaInterna;

    public EventoCambioEtapa(Candidato candidato, TipoEtapa etapaAnterior, TipoEtapa etapaNueva,
                             boolean esNotaInterna) {
        this.candidato = candidato;
        this.etapaAnterior = etapaAnterior;
        this.etapaNueva = etapaNueva;
        this.esNotaInterna = esNotaInterna;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public TipoEtapa getEtapaAnterior() {
        return etapaAnterior;
    }

    public TipoEtapa getEtapaNueva() {
        return etapaNueva;
    }

    public boolean isNotaInterna() {
        return esNotaInterna;
    }
}
