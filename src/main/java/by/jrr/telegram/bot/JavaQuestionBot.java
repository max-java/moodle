//package by.jrr.telegram.bot;
//
//import by.jrr.telegram.bot.service.BroadcastService;
//import by.jrr.telegram.bot.service.RequestDispatcher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//@PropertySource("application.properties")
//@Component
//public class JavaQuestionBot extends TelegramLongPollingBot {
//
//    @Autowired
//    RequestDispatcher requestDispatcher;
//    @Autowired
//    BroadcastService broadcastService;
//
//
//    @Value("${bot.username}")
//    private String botUsername;
//    @Value("${bot.token}")
//    private String botToken;
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        requestDispatcher.dispatch(update);
//    }
//
//    @Override
//    public String getBotUsername() {
//        new Thread(() -> broadcastService.startBroadcasting()).start();
//        return botUsername;
//    }
//
//    @Override
//    public String getBotToken() {
//        return botToken;
//    }
//}
