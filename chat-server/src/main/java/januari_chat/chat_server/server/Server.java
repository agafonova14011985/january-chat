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

// хранит список подключенных клиентов,
// предназначенный для управления соединением с клиентом и рассылкой сообщений.
//Централизованное построение
public class Server {
    public static final String REGEX = "%!%"; //разделитель
    private  final int PORT = 8189;

    //ссылаемся на интерфейс
    private final AuthService authService;
    private final List<ClientHandler> clientHandlers;

    public Server(AuthService authService) {
       // PORT = PropertyReader.getInstance().getPort();
        this.clientHandlers = new ArrayList<>();
        this.authService = authService;
    }

    public void start() {//serverSocket создает socket/занимает порт
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {//слушаем порт
            System.out.println("Server start!");
            while (true) {
                System.out.println("Waiting for connection......");
                var socket = serverSocket.accept();// accept() создает соединение, когда кто то постучался в порт
                System.out.println("Client connected");
                var clientHandler = new ClientHandler(socket, this);//через socket работаем с клиентом
                clientHandler.handle(); //clientHandler - их много внутри них socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            authService.stop();//остановка
            shutdown();
        }
    }

    public void privateMessage(String sender, String recipient, String message, ClientHandler senderHandler) {

        //ищет того кому хотим отправить сообщение
        //ищем его в getHandlerByUser по имени пользователя (recipient);
        var handler = getHandlerByUser(recipient);
        //если  не нашли то отправляем отправителю ошибку в виде сообщения
        if (handler == null){//если нет получателя
            senderHandler.send(String.format("/error" + REGEX + "recipient not found: " + recipient));
            return;
        }
        //если получатель есть то формируем сообщение
        message = String.format("Private" + sender + "->>>" + recipient + ": " + message);
        handler.send(message);//отправка получателю
        senderHandler.send(message);
    }




    private ClientHandler getHandlerByUser(String username) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUserNick().equals(username)) {
                return clientHandler;
            }
        }
        return null;
    }

    public void broadcastMessage(String from, String message) {
       // message = "/broadcast" + REGEX + from + REGEX + message;
        message = String.format("[%s]: %s", from, message);
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

//Для блокировки возможности авторизоваться нескольким клиентам под одной учётной записью используется
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
