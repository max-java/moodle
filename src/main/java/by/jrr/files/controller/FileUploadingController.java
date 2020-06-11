package by.jrr.files.controller;

import by.jrr.auth.service.UserDataToModelService;
import by.jrr.constant.Endpoint;
import by.jrr.constant.View;
import by.jrr.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@Controller
public class FileUploadingController {
    // TODO: 11/06/20 make it like only admins could upload huge files

    @Autowired
    UserDataToModelService userDataToModelService;
    @Autowired
    FileService fileService;


    @PostMapping(Endpoint.FILES)
    public ModelAndView uploadFile(@RequestParam MultipartFile multipartFile,
                                    @RequestParam Optional<String> description,
                                    @RequestParam Optional<String> save) {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        if(save.isPresent()) {
            try {
                fileService.saveUploaded(multipartFile, description);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mov.addObject("files", fileService.getAllFileMetas());
        mov.setViewName(View.FILE);
        return mov;
    }
    @GetMapping(Endpoint.FILES)
    public ModelAndView viewFiles() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());
        mov.addObject("files", fileService.getAllFileMetas());
        mov.setViewName(View.FILE);
        return mov;
    }
    @GetMapping("/file")
    public ModelAndView downloadFile() {
        ModelAndView mov = userDataToModelService.setData(new ModelAndView());

        mov.setViewName(View.FILE);
        return mov;
    }
}
