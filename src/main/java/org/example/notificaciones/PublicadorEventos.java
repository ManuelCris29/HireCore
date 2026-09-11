package org.example.notificaciones;

import java.util.ArrayList;
import java.util.List;

public class PublicadorEventos {

    private final List<ObservadorCandidato> observadores = new ArrayList<>();

    public void suscribir(ObservadorCandidato observador) {
        observadores.add(observador);
    }

    public void publicar(EventoCambioEtapa evento) {
        for (ObservadorCandidato observador : observadores) {
            observador.notificar(evento);
        }
    }
}
