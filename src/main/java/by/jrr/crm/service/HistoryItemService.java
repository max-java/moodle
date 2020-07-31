package by.jrr.crm.service;

import by.jrr.auth.service.UserAccessService;
import by.jrr.crm.bean.HistoryItem;
import by.jrr.crm.repository.HistoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryItemService {

    @Autowired
    UserAccessService uas;
    @Autowired
    HistoryItemRepository historyItemRepository;
    public String example(Long profileId) {
        if(uas.isCurrentUserIsAdmin()) {
            return "action";
        }
        return new String();
    }

    public List<HistoryItem> getHistoryForProfile(Long profileId) {
        if(uas.isCurrentUserIsAdmin()) {
            return historyItemRepository.findByProfileId(profileId);
        }
        return new ArrayList<>();
    }
    public void saveHistoryForProfile(HistoryItem historyItem) {
        if(uas.isCurrentUserIsAdmin()) {
            historyItemRepository.save(historyItem);
        }
    }
    public void deleteHistoryForProfileByEntity(HistoryItem historyItem) {
        if(uas.isCurrentUserIsAdmin()) {
            historyItemRepository.delete(historyItem);
        }
    }
}
