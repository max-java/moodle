package by.jrr.files.controller;

import by.jrr.auth.service.UserAccessService;
import by.jrr.constant.Endpoint;
import by.jrr.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class FileDownloadingController {

    @Autowired
    FileService fileService;
    @Autowired
    UserAccessService uas;

    @RequestMapping(value = Endpoint.IMAGE + "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getFileBytesByFilename(@PathVariable String fileName) {
        return fileService.getFileBytes(fileName);
    }

    //    @AtLeastFreeStudent // TODO: 30/06/20 make this restriction by TOA
    @RequestMapping(value = Endpoint.PDF + "/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] getPdfBytesByFilename(@PathVariable String fileName) {
        return fileService.getFileBytesFromS3(fileName);
    }
}
