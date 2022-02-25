package ru.geekbrains.january_chat.chat_server.error;

public class ChangeNickExeption extends RuntimeException {

    public ChangeNickExeption(String message){
        super(message);
    }
}
