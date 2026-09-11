package org.example.notificaciones;

public class ObservadorReclutador implements ObservadorCandidato {

    private final CanalNotificacion canal;

    public ObservadorReclutador(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void notificar(EventoCambioEtapa evento) {
        canal.enviar(
                evento.getCandidato().getReclutadorEmail(),
                evento.getCandidato().getNombre() + " pasó de " + evento.getEtapaAnterior() + " a " + evento.getEtapaNueva()
        );
    }
}