package org.example.notificaciones;

import java.time.format.DateTimeFormatter;

public class ObservadorPortalCandidato implements ObservadorCandidato {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
                            + " (actualizado por " + evento.getUsuario()
                            + " el " + evento.getFecha().format(FORMATO_FECHA) + ")"
            );
        }
    }
}