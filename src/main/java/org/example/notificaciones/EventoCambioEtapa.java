package org.example.notificaciones;

import org.example.dominio.Candidato;
import org.example.etapas.TipoEtapa;

import java.time.LocalDateTime;

public class EventoCambioEtapa {

    private final Candidato candidato;
    private final TipoEtapa etapaAnterior;
    private final TipoEtapa etapaNueva;
    private final String usuario;
    private final LocalDateTime fecha;
    private final boolean esNotaInterna;

    public EventoCambioEtapa(Candidato candidato, TipoEtapa etapaAnterior, TipoEtapa etapaNueva,
                             String usuario, LocalDateTime fecha, boolean esNotaInterna) {
        this.candidato = candidato;
        this.etapaAnterior = etapaAnterior;
        this.etapaNueva = etapaNueva;
        this.usuario = usuario;
        this.fecha = fecha;
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

    public String getUsuario() {
        return usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public boolean isNotaInterna() {
        return esNotaInterna;
    }
}
