package org.example.notificaciones;

import org.example.etapas.TipoEtapa;

public class ObservadorNomina implements ObservadorCandidato {

    private final CanalNotificacion canal;

    public ObservadorNomina(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void notificar(EventoCambioEtapa evento) {
        if (evento.getEtapaNueva() == TipoEtapa.CONTRATADO) {
            canal.enviar(
                    "nomina@empresa.com",
                    evento.getCandidato().getNombre() + " fue contratado"
            );
        }
    }
}