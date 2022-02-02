package januari_chat.chat_server.server;

import januari_chat.chat_server.error.WrongCredentialsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Thread handlerThread;
    private Server server;
    private String user;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Handler created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //обработка/когда подключился клиент мы его должны авторизовать
    public void handle() {
        handlerThread = new Thread(() -> {
            authorize();
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                try {
                    var message = in.readUTF();
                    handleMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();
    }

    //получаем все сообщения и обрабатываем
    private void handleMessage(String message) {
        var splitMessage = message.split(Server.REGEX);
        switch (splitMessage[0]) {
            case "/broadcast":
                server.broadcastMessage(user, splitMessage[1]);
                break;
        }
    }

    //обработка/когда подключился клиент мы его должны авторизовать
    private void authorize() {
        System.out.println("Authorizing");
        while (true) {
            try {
                var message = in.readUTF();
                if (message.startsWith("/auth")) {
                    var parsedAuthMessage = message.split(Server.REGEX);
                    var response = "";
                    String nickname = null;
                    try {
                        nickname = server.getAuthService().authorizeUserByLoginAndPassword(parsedAuthMessage[1], parsedAuthMessage[2]);
                    } catch (WrongCredentialsException e) {
                        response = "/error" + Server.REGEX + e.getMessage();
                        System.out.println("Wrong credentials, nick / не верные данные " + parsedAuthMessage[1]);
                    }

                    //отправка ошибки если ник занят
                    if (server.isNickBusy(nickname)) {
                        response = "/error" + Server.REGEX + "this client already connected";
                        System.out.println("Nick busy " + nickname);
                    }
                    if (!response.equals("")) {
                        send(response);
                    } else {
                        //если все хорошо то
                        this.user = nickname;
                        server.addAuthorizedClientToList(this);
                        send("/auth_ok" + Server.REGEX + nickname);
                        break;//прерывание авторизации
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread getHandlerThread() {
        return handlerThread;
    }

    public String getUserNick() {
        return this.user;
    }
}

/*import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private static int clientCounter = 0;//нумерация клиентов для их идентификации/ счетчик статика, новый клиент с новым номером
    private int clientNumber;//по номеру
    private Socket socket;//поле мы получаем из сервера, который создает объект Handler на каждого подключившегося клиента
    private DataOutputStream out;//потоки ввода вывода
    private DataInputStream in;
    private Thread handlerThread;
    private Server server;//ссылка на сервер

    //когда сервер создает обработчика
    public ClientHandler(Socket socket, Server server) { //ссылка на сервер, для отправки сообщений
        try {
            this.server = server;//передается ссылка на самого себя/что бы были сообщения
            this.socket = socket;//передается серверам
            this.in = new DataInputStream(socket.getInputStream());//из socket - достаем потоки вв и выв
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Handler created");
            this.clientNumber = ++clientCounter;//обновляем номер клиента
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод обработки сообщений
    public void handle() {
        handlerThread = new Thread(() -> { //запуск в отдельном потоке
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                //слушает входящее сообщение от своего клиента
                try {
                    String message = in.readUTF();
                    message = "client #" + this.clientNumber + ": " + message;//форматирование сообщения
                    server.broadcast(message);// у сервера вызываем метод для отправки сообщения всем
                    System.out.printf("Client #%d: %s\n", this.clientNumber, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();//запуск потока
    }

    //метод, который сервер у каждого Handler- вызывает у обработчика
    public void send(String msg) {
        try {
            out.writeUTF(msg);//запись сообщения и отправка его в сеть
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Thread getHandlerThread() {
        return handlerThread;
    }
}*/