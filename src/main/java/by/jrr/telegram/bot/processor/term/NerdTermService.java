package by.jrr.telegram.bot.processor.term;

import by.jrr.telegram.bot.bean.NerdTermLibrary;
import by.jrr.telegram.bot.bean.NerdTermToLearn;
import by.jrr.telegram.bot.repository.NerdTermLibraryRepository;
import by.jrr.telegram.bot.repository.NerdTermToLearnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class NerdTermService {

    @Autowired
    private NerdTermLibraryRepository nerdTermLibraryRepository;
    @Autowired
    private NerdTermToLearnRepository nerdTermToLearnRepository;

    public List<NerdTermLibrary> getByTermIfPresent(String term) {
        return nerdTermLibraryRepository.findByTermIgnoreCase(term);
    }
    public boolean deleteByTermAndDefinition(String term, String definition) {
        Optional<NerdTermLibrary> nerdTermLibrary = nerdTermLibraryRepository.findByTermAndDefinition(term, definition);
        if (nerdTermLibrary.isPresent()) {
            nerdTermLibraryRepository.deleteById(nerdTermLibrary.get().getId());
            return true;
        }
            return false;
    }

    public NerdTermLibrary saveToLearn (String term) {
        NerdTermToLearn nerdTermToLearn = new NerdTermToLearn();
        nerdTermToLearn.setTerm(term);
        nerdTermToLearnRepository.save(nerdTermToLearn);
        NerdTermLibrary nerdTermLibrary = new NerdTermLibrary();
        nerdTermLibrary.setTerm(term);
        nerdTermLibrary.setDefinition(randomText());
        return nerdTermLibrary;
    }
    public void saveLearned(NerdTermLibrary nerdTermLibrary) {
        nerdTermLibraryRepository.save(nerdTermLibrary);
    }

    private String randomText() {
        List<String> msg = Arrays.asList(
                "Я спрошу у папы",
                "Никогда не слышал",
                "Я загуглю",
                "Отпишусь",
                "Папа спит, просил не беспокоить",
                "Это написано кровью",
                "А ты знаешь, что такое GoF?",
                "А ты знаешь, что такое S.O.L.I.D?",
                "А ты знаешь, что такое KISS?",
                "А ты знаешь, что такое YAGNI?",
                "А ты знаешь, что такое Бритва Оккама?",
                "Поспи лучше",
                "Спроси Макса, он умный!",
                "Не стесняйся спрашивать на занятии! Сэкономишь кучу времени!",
                "Ты не знаешь этого? а чего молчишь???",
                "Я думал это понятно. Но для дебя поищу.",
                "Это хорошее начало разговора...",
                "А ты знаешь, что такое MVC?",
                "А что говорят коллеги?",
                "А ты гуглил?",
                "Ментор знает. Я спрошу",
                "У меня есть подписка на википедию - думаю там найдется",
                "Когда-то я тоже задавался таким вопросом",
                "Вроде просто... В чем подвох?",
                "Вот ты умница - такие слова знаешь!",
                "Эти англицизмы - просто напасть какая-то!",
                "Хорошо, я посмотрю. Ты пока домашку делай",
                "Запишись на курсы - там все узнаешь",
                "Я еще маленький и многих слов не знаю...",
                "Дам знать как узнаю",
                "Спишемся, на связи",
                "Думаю, это будет не сложно",
                "Завтра узнаю",
                "До конца недели отпишусь",
                "Пингани меня через день",
                "У Антона спрашивал?",
                "Кто-то из наших уже спрашивал ...",
                "Я часто такое слышу",
                "Я умный, но даже я с таким не сталкивался",
                "Вот это ты задвинул",
                "Где-то я такое слышал...",
                "ok, отпишусь");
        int index = ThreadLocalRandom.current().nextInt(0, msg.size() - 1);
        return msg.get(index);
    }


}
