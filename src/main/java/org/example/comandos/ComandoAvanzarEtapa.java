package org.example.comandos;

import org.example.dominio.Candidato;
import org.example.etapas.Etapa;
import org.example.gestor.TransicionInvalidaException;
import org.example.notificaciones.EventoCambioEtapa;
import org.example.notificaciones.PublicadorEventos;

import java.time.LocalDateTime;

public class ComandoAvanzarEtapa implements Comando {

    private final Candidato candidato;
    private final Etapa nuevaEtapa;
    private final String usuario;
    private final LocalDateTime fecha;
    private final PublicadorEventos publicador;
    private final boolean esNotaInterna;
    private CandidatoMemento memento;

    public ComandoAvanzarEtapa(Candidato candidato, Etapa nuevaEtapa, String usuario,
                               LocalDateTime fecha, PublicadorEventos publicador,
                               boolean esNotaInterna) {
        this.candidato = candidato;
        this.nuevaEtapa = nuevaEtapa;
        this.usuario = usuario;
        this.fecha = fecha;
        this.publicador = publicador;
        this.esNotaInterna = esNotaInterna;
    }

    @Override
    public void ejecutar() {
        if (!candidato.getEtapaActual().puedePasarA(nuevaEtapa.getTipoEtapa())) {
            throw new TransicionInvalidaException(
                    "Transición inválida: " + candidato.getEtapaActual().getTipoEtapa() + " -> " + nuevaEtapa.getTipoEtapa()
            );
        }
        memento = candidato.guardarEstado();
        var etapaAnterior = candidato.getEtapaActual().getTipoEtapa();
        candidato.setEtapaActual(nuevaEtapa);
        publicador.publicar(new EventoCambioEtapa(candidato, etapaAnterior, nuevaEtapa.getTipoEtapa(), esNotaInterna));
    }

    @Override
    public void deshacer() {

        var etapaAnterior = candidato.getEtapaActual().getTipoEtapa();
        candidato.restaurarEstado(memento);

        var nuevaEtapa = candidato.getEtapaActual().getTipoEtapa();
        publicador.publicar(new EventoCambioEtapa(candidato, etapaAnterior, nuevaEtapa, esNotaInterna));
    }

    @Override
    public String auditoria() {
        return fecha + " | " + usuario + " | " + candidato.getId()
                + " -> " + nuevaEtapa.getTipoEtapa()
                + (esNotaInterna ? " | NOTA INTERNA" : "");
    }
}
