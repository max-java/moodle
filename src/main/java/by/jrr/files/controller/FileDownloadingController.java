package by.jrr.files.controller;

import by.jrr.constant.Endpoint;
import by.jrr.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileDownloadingController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = Endpoint.IMAGE+"/{fileName}") // TODO: 01/06/20 add to constants and add link to files
    @ResponseBody
    public byte[] getFileBytesByFilename(@PathVariable String fileName)  {
        return fileService.getFileBytes(fileName);
    }

}
