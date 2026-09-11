package org.example.notificaciones;

public class ObservadorPortalCandidato implements ObservadorCandidato {

    private final CanalNotificacion canal;

    public ObservadorPortalCandidato(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void notificar(EventoCambioEtapa evento) {
        if (!evento.isNotaInterna()) {
            canal.enviar(
                    evento.getCandidato().getId(),
                    "Tu proceso avanzó a " + evento.getEtapaNueva()
            );
        }
    }
}