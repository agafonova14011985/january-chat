package ru.geekbrains.january_chat.chat_server.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//используем библиотеку апачи             <groupId>org.apache.logging.log4j</groupId>

//шесть уровней логирования//уровни сообщений самое важное сообщение это Fatal
// Trace << Debug << Info << Warn << Error << Fatal

public class Logging {

    //создаем объект логера, получаем его из фабрики логменеджер/пользуемся методом getLogger
   //логеры обычно делают для класса
    private static final Logger log = LogManager.getLogger(Logging.class);//берем логер для данного класса

   public static void main(String[] args) {



        //различные методы//логируют переданную информацию на разных уровнях

        log.info("Info log");//
        log.trace("Trace log");//записывают все все мелочи,
        log.debug("Debug log");//инфа для дебага
        log.info("Info log");//не критичная информация
        log.warn("Warn log");
        log.error("Error log");//логируем ошибки
        log.fatal("Fatal log");


   }
}