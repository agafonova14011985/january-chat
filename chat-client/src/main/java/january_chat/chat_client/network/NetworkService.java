package january_chat.chat_client.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//сервер отвечающий за работу сети
public class NetworkService {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private MessageProcessor messageProcessor;

    public NetworkService(MessageProcessor messageProcessor)  {
        //создаем
        this.messageProcessor = messageProcessor;
    }

    public void connect() throws IOException {
        this.socket = new Socket(HOST, PORT);
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        readMessages();
    }

    //читаем сообщение в отдельном потоке
    public void readMessages() {
        var thread = new Thread(() -> {
            try {
                //когда было полученно сообщение мы его отдаем messageProcessor
                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                    var message = in.readUTF();
                    messageProcessor.processMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);//наш поток - демон
        thread.start();
    }

    //отправка сообщений
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    //метод закрытие
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
