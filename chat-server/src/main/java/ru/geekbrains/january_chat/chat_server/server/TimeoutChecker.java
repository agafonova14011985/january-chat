package ru.geekbrains.january_chat.chat_server.server;

import java.util.HashMap;
import java.util.Map;

public class TimeoutChecker {

    private static final Map<ClientHandler, Long> nonAuthorizedSockets = new HashMap<>();
    private static final long TIME_LIMIT =  1000;

    private TimeoutChecker() {
        new Thread(() -> {
            while (true) {
                nonAuthorizedSockets.forEach(((clientHandler, aLong) -> {
                    if ((System.currentTimeMillis() - aLong) > TIME_LIMIT) {
                        clientHandler.send("Время на авторизацию истекло.\n Соединение разорвано.");

                        clientHandler.closeSocket();
                    }
                }));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void set(ClientHandler clientHandler) {
        nonAuthorizedSockets.put(clientHandler, System.currentTimeMillis());
    }

    public static void unset(ClientHandler clientHandler) {
        nonAuthorizedSockets.remove(clientHandler);
    }
}
