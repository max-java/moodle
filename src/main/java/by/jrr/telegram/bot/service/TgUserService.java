//package by.jrr.telegram.bot.service;
//
//import by.jrr.telegram.bot.repository.TgUserRepository;
//import by.jrr.telegram.model.TgUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class TgUserService {
//
//    @Autowired
//    TgUserRepository tgUserRepository;
//
//    public TgUser getTgUserBByProfileId(Long id) {
//        Optional<TgUser> tgUserOptional = tgUserRepository.findByProfileId(id);
//        if(tgUserOptional.isPresent()) {
//            return tgUserOptional.get();
//        } else {
//            return new TgUser();
//        }
//    }
//    public List<TgUser> findAll() {
//        return (List) tgUserRepository.findAll();
//    }
//}
