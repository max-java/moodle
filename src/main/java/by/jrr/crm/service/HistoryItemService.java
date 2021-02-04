package by.jrr.crm.service;

import by.jrr.auth.service.UserAccessService;
import by.jrr.crm.bean.History;
import by.jrr.crm.bean.NoteItem;
import by.jrr.crm.bean.Task;
import by.jrr.crm.repository.NoteItemRepository;
import by.jrr.crm.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HistoryItemService {

    @Autowired
    UserAccessService uas;
    @Autowired
    NoteItemRepository noteItemRepository;
    @Autowired
    TaskRepository taskRepository;

    public String example(Long profileId) {
        if (uas.isCurrentUserIsAdmin()) {
            return "action";
        }
        return new String();
    }


    public List<History> getHistoryForProfile(Long profileId) {
        if (uas.isCurrentUserIsAdmin()) {
            List<History> historyList = new ArrayList<>();
            historyList.addAll(noteItemRepository.findByProfileId(profileId));
            historyList.addAll(taskRepository.findByProfileId(profileId));
            historyList.sort(Comparator.comparing(History::getDate).reversed());
            return historyList;
        }
        return new ArrayList<>();
    }

    public List<NoteItem> getNoteForProfile(Long profileId) {
        if (uas.isCurrentUserIsAdmin()) {
            return noteItemRepository.findByProfileId(profileId);
        }
        return new ArrayList<>();
    }

    public void saveNoteForProfile(NoteItem noteItem) {
        noteItemRepository.save(noteItem); //uas.isCurrentUserIsAdmin() to save user actions
    }

    public void deleteNoteForProfileByEntity(NoteItem noteItem) {
        if (uas.isCurrentUserIsAdmin()) {
            noteItemRepository.delete(noteItem);
        }
    }


    public List<Task> getTaskForProfile(Long profileId) {
        if (uas.isCurrentUserIsAdmin()) {
            return taskRepository.findByProfileId(profileId);
        }
        return new ArrayList<>();
    }

    public Task findTaskById(Long taskId) {
        if (uas.isCurrentUserIsAdmin()) {
            return taskRepository.findById(taskId).orElseGet(Task::new);
        }
        return null; // TODO: 01/08/20 throw Access denied exception everywhere, catch in controller and redirect to 403
    }

    public void saveTaskForProfile(Task task) {
        if (uas.isCurrentUserIsAdmin()) {
            taskRepository.save(task);
        }
    }

    public void deleteTaskForProfileByEntity(Task task) {
        if (uas.isCurrentUserIsAdmin()) {
            taskRepository.delete(task);
        }
    }

    public List<Task> findAllNotFinishedTasks() {
        if (uas.isCurrentUserIsAdmin()) {
            return taskRepository.findByIsFinishedFalse();
        }
        return null; // TODO: 08/10/2020 make NPE safety
    }

    public List<Task> findActiveTasksForProfile(Long id) {
        if (uas.isCurrentUserIsAdmin()) {
            return taskRepository.findByProfileIdAndIsFinishedFalse(id);
        }
        return new ArrayList<>();
    }


}
