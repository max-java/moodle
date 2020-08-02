package by.jrr.crm.controller;

import by.jrr.constant.Endpoint;
import by.jrr.crm.bean.NoteItem;
import by.jrr.crm.bean.Task;
import by.jrr.crm.common.CrmCommand;
import by.jrr.crm.service.HistoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class HistoryController {
    @Autowired
    HistoryItemService historyItemService;

    @PostMapping(Endpoint.CRM_NEW_HISTORY_ITEM)
    public ModelAndView saveNewItem(@RequestParam Long profileId,
                                    @RequestParam CrmCommand command,
                                    @RequestParam Optional<String> task,
                                    @RequestParam Optional<String> taskDL,
                                    @RequestParam Optional<Boolean> isFinished,
                                    @RequestParam Optional<Long> taskId,
                                    @RequestParam Optional<String> note
    ) {
        switch (command) {
            case SAVE_NOTE:
                saveNote(profileId, note);
                break;
            case SAVE_TASK:
                saveTask(profileId, task, taskDL);
                break;
            case FINISH_TASK:
                finishTask(taskId, isFinished);

        }

        return new ModelAndView("redirect:" + Endpoint.PROFILE_CARD + "/" + profileId);
    }
    private void saveNote(Long profileId, Optional<String> note) {
        historyItemService.saveNoteForProfile(NoteItem.builder()
                .profileId(profileId)
                .text(note.get())
                .timestamp(LocalDateTime.now())
                .build());
    }
    private void saveTask(Long profileId, Optional<String> task, Optional<String> taskDL) {
        LocalDateTime deadline = LocalDateTime.parse(taskDL.get()+":00");
        historyItemService.saveTaskForProfile(Task.builder()
                .profileId(profileId)
                .text(task.get())
                .deadLine(deadline)
                .isFinished(false)
                .timestamp(LocalDateTime.now())
                .build());
    }
    private void finishTask(Optional<Long> taskId, Optional<Boolean> isFinished) {
        if(isFinished.isPresent()) {
            Task savedTask = historyItemService.findTaskById(taskId.get());
            savedTask.setTimestamp(LocalDateTime.now());
            savedTask.setIsFinished(true);
            historyItemService.saveTaskForProfile(savedTask);
        } else {
            Task savedTask = historyItemService.findTaskById(taskId.get());
            savedTask.setTimestamp(LocalDateTime.now());
            savedTask.setIsFinished(false);
            historyItemService.saveTaskForProfile(savedTask);
        }

    }
}
