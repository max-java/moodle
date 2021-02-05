package by.jrr.crm.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.profile.bean.TimeLine;
import by.jrr.profile.service.TimeLineService;
import by.jrr.registration.service.StudentActionToLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TimeLinePageController {
    @Autowired
    TimeLineService timeLineService;
    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    StudentActionToLogService studentActionToLogService;

    @GetMapping(Endpoint.CRM_TIMELINE+"/{uuid}")
    public ModelAndView saveNewItem(@PathVariable String uuid) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.setViewName(View.CRM_TIMELINE_ITEM_PAGE);

        TimeLine timeLine = timeLineService.findTimeLineByTimeLineUUID(uuid);

        mov.addObject("timeline", timeLine);
        mov.addObject("timelineActions", studentActionToLogService.findActionsByTimelineId(uuid));
        mov.addObject("totalUniqVisitors", studentActionToLogService.findTotalUniqVisitorsForTimelineEvent(uuid));
        mov.addObject("totalUniqVisitorsInTime", studentActionToLogService.findTotalUniqVisitorsForTimelineEventAroundTimestamp(timeLine, 30, 60));

        return mov;
    }

}
