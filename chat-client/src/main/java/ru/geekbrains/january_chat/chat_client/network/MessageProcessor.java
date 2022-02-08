package ru.geekbrains.january_chat.chat_client.network;

//обрабатывает сообщение по сети
public interface MessageProcessor {
    void processMessage(String message);
}
