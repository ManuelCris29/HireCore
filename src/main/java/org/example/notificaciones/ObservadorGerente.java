package org.example.notificaciones;

import org.example.etapas.TipoEtapa;

public class ObservadorGerente implements ObservadorCandidato {

    private final CanalNotificacion canal;

    public ObservadorGerente(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void notificar(EventoCambioEtapa evento) {
        if (evento.getEtapaNueva() == TipoEtapa.OFERTA || evento.getEtapaNueva() == TipoEtapa.CONTRATADO) {
            canal.enviar(
                    "gerente@empresa.com",
                    evento.getCandidato().getNombre() + " llegó a " + evento.getEtapaNueva()
            );
        }
    }
}