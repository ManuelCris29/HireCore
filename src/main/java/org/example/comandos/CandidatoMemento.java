package org.example.comandos;

import org.example.etapas.Etapa;

public class CandidatoMemento {

    private final Etapa etapa;


    public CandidatoMemento(Etapa etapa) {

        this.etapa = etapa;
    }

    public Etapa getEtapa() {
        return etapa;
    }

}
