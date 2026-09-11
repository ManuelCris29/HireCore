package org.example.dominio;

import org.example.etapas.Etapa;
import org.example.comandos.CandidatoMemento;

public class Candidato {

    private final String id;
    private final String nombre;
    private final String reclutadorEmail;
    private Etapa etapaActual;

    public Candidato(String id, String nombre, String reclutadorEmail, Etapa etapaInicial) {
        this.id = id;
        this.nombre = nombre;
        this.reclutadorEmail = reclutadorEmail;
        this.etapaActual = etapaInicial;
    }

    public String getReclutadorEmail() {
        return reclutadorEmail;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public Etapa getEtapaActual() {
        return etapaActual;
    }

    public void setEtapaActual(Etapa etapaActual) {
        this.etapaActual = etapaActual;
    }

    public CandidatoMemento guardarEstado() {
        return new CandidatoMemento(etapaActual);
    }

    public void restaurarEstado(CandidatoMemento memento) {
        this.etapaActual = memento.getEtapa();
    }

}
