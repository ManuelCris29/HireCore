package org.example.notificaciones;

public class CanalPortal implements CanalNotificacion {

    @Override
    public void enviar(String destino, String mensaje) {
        System.out.println("[PORTAL para " + destino + "] " + mensaje);
    }
}
