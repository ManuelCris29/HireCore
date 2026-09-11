package org.example;

import org.example.dominio.Candidato;
import org.example.etapas.Etapa;
import org.example.etapas.FabricaEtapas;
import org.example.etapas.TipoEtapa;
import org.example.notificaciones.*;
import org.example.comandos.HistorialComandos;
import org.example.gestor.GestorDeCandidato;

public class Main {

    public static void main(String[] args) {

        FabricaEtapas fabrica = new FabricaEtapas();
        HistorialComandos historial = new HistorialComandos();
        PublicadorEventos publicador = new PublicadorEventos();

        CanalNotificacion canalEmail = new CanalEmail();
        CanalNotificacion canalPortal = new CanalPortal();

        publicador.suscribir(new ObservadorReclutador(canalEmail));
        publicador.suscribir(new ObservadorGerente(canalEmail));
        publicador.suscribir(new ObservadorNomina(canalEmail));
        publicador.suscribir(new ObservadorPortalCandidato(canalPortal));

        GestorDeCandidato gestor = new GestorDeCandidato(fabrica, publicador, historial);

        Candidato ana = new Candidato("C-001", "Ana Torres", "ana.reclutadora@empresa.com", fabrica.obtener(TipoEtapa.APLICADO));

        System.out.println("== Estado inicial ==");
        System.out.println(ana.getNombre() + " está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Avanzando a ENTREVISTA ==");
        gestor.avanzar(ana, "reclutador.carlos");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();


        System.out.println("== Avanzando a PRUEBA_TECNICA ==");
        gestor.avanzar(ana, "reclutador.carlos");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Avanzando a OFERTA ==");
        gestor.avanzar(ana, "gerente.laura");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Deshaciendo la última transición ==");
        gestor.deshacerUltimaTransicion();
        System.out.println("Después de deshacer, está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Rehaciendo el avance a OFERTA ==");
        gestor.avanzar(ana, "gerente.laura");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Avanzando a VERIFICACION_REFERENCIAS ==");
        gestor.avanzar(ana, "reclutador.carlos");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Avanzando a CONTRATADO ==");
        gestor.avanzar(ana, "gerente.laura");
        System.out.println("Ahora está en " + ana.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Intentando avanzar a un candidato ya CONTRATADO (debería fallar) ==");
        try {
            gestor.avanzar(ana, "reclutador.carlos");
        } catch (Exception e) {
            System.out.println("Rechazado correctamente: " + e.getMessage());
        }
        System.out.println();

        Candidato bruno = new Candidato("C-002", "Bruno Díaz", "ana.reclutadora@empresa.com", fabrica.obtener(TipoEtapa.APLICADO));

        System.out.println("== Rechazando a " + bruno.getNombre() + " como NOTA INTERNA ==");
        System.out.println("(el portal del candidato no debe recibir nada)");
        gestor.rechazar(bruno, "reclutador.carlos", true);
        System.out.println("Ahora está en " + bruno.getEtapaActual().getTipoEtapa());
        System.out.println();

        Candidato carla = new Candidato("C-003", "Carla Ruiz", "carla@empresa.com", fabrica.obtener(TipoEtapa.APLICADO));

        System.out.println("== Avanzando a " + carla.getNombre() + " a ENTREVISTA ==");
        gestor.avanzar(carla, "reclutador.carlos");
        System.out.println(carla.getNombre() + " está en " + carla.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Rechazando a " + carla.getNombre() + " desde ENTREVISTA ==");
        gestor.rechazar(carla, "reclutador.carlos", false);
        System.out.println(carla.getNombre() + " está en " + carla.getEtapaActual().getTipoEtapa());
        System.out.println();

        System.out.println("== Historial de comandos ==");
        historial.getHistorial().forEach(c -> System.out.println(c));
    }
}