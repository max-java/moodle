//package by.jrr.telegram.bot.processor.term;
//
//import by.jrr.telegram.bot.bean.NerdTermLibrary;
//import by.jrr.telegram.bot.processor.BotCommand;
//import by.jrr.telegram.bot.processor.Processor;
//import by.jrr.telegram.bot.service.MessageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Service
//public class NerdTermProcessor implements Processor {
//
//    @Autowired
//    NerdTermService nerdTermService;
//    @Autowired
//    MessageService messageService;
//
//    @Override
//    public String run() {
//        return "Magic is happening";
//    }
//
//    public void process(Update update) {
//        Message msq = update.getMessage();
//        System.out.println("msq.getChatId() = " + msq.getChatId());
//        String response = "я ничего не понял";
//        if (msq.hasText()) {
//            final String text = msq.getText();
//            final String term = text.substring(text.indexOf(BotCommand.NERD_TERM.getCommand()) + BotCommand.NERD_TERM.getCommand().length()+1).trim();
//            List<NerdTermLibrary> nerdTermLibraryList = nerdTermService.getByTermIfPresent(term);
//            if (nerdTermLibraryList.size() == 1) {
//                NerdTermLibrary nerdTerm = nerdTermLibraryList.get(0);
//                response = nerdTerm.getTerm() + "\n" + nerdTerm.getDefinition();
//            } else if(nerdTermLibraryList.size() > 1) {
//                int index = ThreadLocalRandom.current().nextInt(0, nerdTermLibraryList.size());
//                NerdTermLibrary nerdTerm = nerdTermLibraryList.get(index);
//                response = nerdTerm.getTerm() + "\n" + nerdTerm.getDefinition();
//            } else {
//                NerdTermLibrary nerdTerm = nerdTermService.saveToLearn(term);
//                response = nerdTerm.getTerm() + "\n" + nerdTerm.getDefinition();
//            }
//        }
//        messageService.sendMessage(msq, response);
//    }
//}
