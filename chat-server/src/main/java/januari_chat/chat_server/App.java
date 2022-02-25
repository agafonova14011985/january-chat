package januari_chat.chat_server;

import januari_chat.chat_server.auth.InMemoryAuthService;
import januari_chat.chat_server.server.Server;

public class App {
    //создает объект и запускает сервер
    public static void main(String[] args) {
        new Server(new InMemoryAuthService()).start();
    }
}
//https://github.com/agafonova14011985/january-chat/pull/3