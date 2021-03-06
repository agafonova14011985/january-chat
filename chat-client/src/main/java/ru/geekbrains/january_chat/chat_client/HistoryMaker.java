package ru.geekbrains.january_chat.chat_client;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//работа с историей сообщений
public class HistoryMaker {
    private  String login;
    private File history;

    public HistoryMaker(String login) {
        this.login = login;
        //история записывается в файл по имени пользователя - login
        this.history = new File("history/" + "history_" + login + ".txt");
        //если пользователь еще без истории, нет католога, то мы его создаем
        if (!history.exists()) {//проверка
            //создание файла
            File path = new File("history/");
            path.mkdirs();//mkdirs
        }
    }

    public List<String> readHistory() {
        //проверяем если истории нет, то выводим сообщение : нет истории сообщений
        if (!history.exists())
            return Collections.singletonList("У вас еще нет истории сообщений");
        List<String> result = null;
        //если история имеется то читаем наш файл методом BufferedReader/экономит ресурсы
        if (history.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(history))) {
                String historyString;
                //чтение файла происходит построчно,
                List<String> historyStrings = new ArrayList<>();
                //имеется в виду, что сообщение занимает не больше 1 строки
                while ((historyString = reader.readLine()) != null) {
                    historyStrings.add(historyString); //add - добавляем результат
                }//если в сообщении меньше 100 строк то все их и выводим
                if (historyStrings.size() <= 100) {
                    result = historyStrings;
                }//если в сообщении больше 100 строк, то обрезаем
                if (historyStrings.size() > 100) {
                    int firstIndex = historyStrings.size() - 100;
                    result = new ArrayList<>(100);

                    for (int counter = firstIndex - 1; counter < historyStrings.size(); counter++) {
                        result.add(historyStrings.get(counter));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("История сообщений для: " +login +" - "+ result.size()+ " строк" );
        return result;
    }
//метод для записи истории
    public void writeHistory(String message) {
        //контроллер передает сообщение, и записываем в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(history, true))) {//добавляем новые записи в конец файла при помощи флага true
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();//ловим
        }
    }
}
