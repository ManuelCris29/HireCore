package org.example.gestor;

import org.example.dominio.Candidato;
import org.example.etapas.Etapa;
import org.example.etapas.FabricaEtapas;
import org.example.etapas.TipoEtapa;
import org.example.comandos.Comando;
import org.example.comandos.ComandoAvanzarEtapa;
import org.example.comandos.HistorialComandos;
import org.example.notificaciones.PublicadorEventos;


import java.time.LocalDateTime;

public class GestorDeCandidato {

    private final FabricaEtapas fabrica;
    private final PublicadorEventos publicador;

    private final HistorialComandos historial;

    public GestorDeCandidato(FabricaEtapas fabrica, PublicadorEventos publicador,  HistorialComandos historial) {
        this.fabrica = fabrica;
        this.publicador = publicador;
        this.historial = historial;
    }

    public void avanzar(Candidato candidato, String usuario) {
        TipoEtapa destino = candidato.getEtapaActual().getSiguientePositiva();
        if (destino == null) {
            throw new TransicionInvalidaException(
                    "No hay siguiente etapa desde " + candidato.getEtapaActual().getTipoEtapa()
            );
        }
        ejecutarTransicion(candidato, destino, usuario, false);
    }

    public void rechazar(Candidato candidato, String usuario, boolean esNotaInterna) {
        ejecutarTransicion(candidato, TipoEtapa.RECHAZADO, usuario, esNotaInterna);
    }

    private void ejecutarTransicion(Candidato candidato, TipoEtapa destino, String usuario, boolean esNotaInterna) {
        Etapa nuevaEtapa = fabrica.obtener(destino);
        Comando comando = new ComandoAvanzarEtapa(candidato, nuevaEtapa, usuario, LocalDateTime.now(), publicador, esNotaInterna);
        comando.ejecutar();
        historial.registrar(comando);
    }


    public void deshacerUltimaTransicion() {
        historial.deshacerUltimo();
    }

}
