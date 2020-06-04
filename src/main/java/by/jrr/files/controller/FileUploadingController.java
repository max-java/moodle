package by.jrr.files.controller;

import by.jrr.auth.service.UserDataToModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class FileUploadingController {

    @Autowired
    UserDataToModelService userDataToModelService;

    @PostMapping("/files")
    public ModelAndView uploadFile(@RequestParam MultipartFile multipartFile,
                                    @RequestParam Optional<String> save) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        if(save.isPresent()) {
            // TODO: 02/06/20 move to private method

        }

        mov.setViewName("files");
        return mov;
    }
    @GetMapping("/files")
    public ModelAndView viewFiles() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());

        mov.setViewName("files");
        return mov;
    }
    @GetMapping("/file")
    public ModelAndView downloadFile() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());

        mov.setViewName("files");
        return mov;
    }
}
