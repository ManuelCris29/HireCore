package org.example.notificaciones;

public class CanalEmail implements CanalNotificacion {

    @Override
    public void enviar(String destino, String mensaje) {
        System.out.println("[EMAIL a " + destino + "] " + mensaje);
    }

}
