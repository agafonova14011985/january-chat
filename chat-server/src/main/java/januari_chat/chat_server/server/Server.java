package januari_chat.chat_server.server;

import januari_chat.chat_server.auth.AuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final String REGEX = "%!%"; //разделитель
    private static final int PORT = 8189;

    //ссылаемся на интерфейс
    private AuthService authService;
    private List<ClientHandler> clientHandlers;

    public Server(AuthService authService) {
        this.clientHandlers = new ArrayList<>();
        this.authService = authService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start!");
            while (true) {
                System.out.println("Waiting for connection......");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandler.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            authService.stop();//остановка
            shutdown();
        }
    }

    public void privateMessage(String from, String message) {

    }

    public void broadcastMessage(String from, String message) {
        message = "/broadcast" + REGEX + from + REGEX + message;
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(message);
        }
    }

    //добавление клиентов
    public synchronized void addAuthorizedClientToList(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
        sendOnlineClients();
    }

    //удаление клиентов
    public synchronized void removeAuthorizedClientToList(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        sendOnlineClients();
    }

    //отправка списка клиентов онлайн
    public void sendOnlineClients() {
        var sb = new StringBuilder("/list");
        sb.append(REGEX);//добавляем разделитель
        //пройдемся по всем
        for (ClientHandler clientHandler : clientHandlers) {
            sb.append(clientHandler.getUserNick());
            sb.append(REGEX);
        }
        var message = sb.toString();
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(message);
        }
    }

    //проверка была ли авторизация уже
    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUserNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    private void shutdown() {

    }

    public AuthService getAuthService() {
        return authService;
    }
}
/*public class Server {
    private static final int PORT = 8189;
    private List<Handler> handlers;

    public Server() {
        this.handlers = new ArrayList<>();
    }

    //стартует
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start!");
            while (true) {
                System.out.println("Waiting for connection......");
                Socket socket = serverSocket.accept();//сервер образует socket
                System.out.println("Client connected");
                Handler handler = new Handler(socket, this);//при появлении socket, создается handler
                handlers.add(handler);//добавляет в список, для дальнейшей отправки общего сообщения всем
                handler.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (Handler handler : handlers) {
            handler.send(message);
        }
    }
}*/
