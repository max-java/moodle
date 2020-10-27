package by.jrr.files.controller;

import by.jrr.auth.configuration.annotations.AtLeastFreeStudent;
import by.jrr.auth.service.UserAccessService;
import by.jrr.constant.Endpoint;
import by.jrr.files.service.FileService;
import com.google.common.io.Resources;
import com.google.common.primitives.Longs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;


@Controller
public class FileDownloadingController {

    @Autowired
    FileService fileService;
    @Autowired
    UserAccessService uas;

    @RequestMapping(value = Endpoint.IMAGE + "/{fileName}")
    @ResponseBody
    public byte[] getFileBytesByFilename(@PathVariable String fileName) {
        return fileService.getFileBytes(fileName);
    }

    //    @AtLeastFreeStudent // TODO: 30/06/20 make this restriction by TOA
    @RequestMapping(value = Endpoint.PDF + "/{fileName}", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public byte[] getPdfBytesByFilename(@PathVariable String fileName) {
        return fileService.getFileBytesFromFileStorage(fileName);
    }

    @RequestMapping(value = Endpoint.VIDEO + "/{fileDir}/{fileName}")
    @ResponseBody
    public byte[] getVideoBytesByFilename(@PathVariable String fileDir, @PathVariable String fileName) {
        if (uas.isUserHasAccessToVideoFile(fileDir, fileName)) {
//            return fileService.getFileBytesFromFileStorage(fileDir + "/" + fileName); // TODO: 15/10/2020 this makes OOM. Need urgent fix
            return fileService.getFileBytesFromFileStorage("/common/403_access_denied.mp4");
        }
        return fileService.getFileBytesFromFileStorage("/common/403_access_denied.mp4");
    }

    // TODO: 16/09/20 streaming video
//    @RequestMapping(method = RequestMethod.GET, value = Endpoint.VIDEO + "/{fileDir}/{fileName}")
//    public StreamingResponseBody videoStream(@PathVariable String fileDir, @PathVariable String fileName)
//            throws IOException {
//        final InputStream videoFileStream = fileService.getFileInputStreamFromFileStorage(fileDir + "/" + fileName);
//        return (os) -> {
//            readAndWrite(videoFileStream, os);
//        };
//    }
//
//    private void readAndWrite(final InputStream is, OutputStream os)
//            throws IOException {
//        byte[] data = new byte[2048];
//        int read = 0;
//        while ((read = is.read(data)) >= 0) {
//            os.write(data, 0, read);
//        }
//        os.flush();
//    }


//    // TODO: 16/09/20 streaming video
//    @RequestMapping(value = Endpoint.VIDEO + "/{fileDir}/{fileName}", method = RequestMethod.GET)
//    @ResponseBody
//    public final ResponseEntity<InputStreamResource>
//    retrieveResource(@PathVariable(value = "fileName") final String fileName,
//                     @PathVariable String fileDir,
//                     @RequestHeader(value = "Range", required = false)
//                             String range) {
//        try {
//            byte[] fileBytes = fileService.getFileBytesFromFileStorage(fileDir + "/" + fileName);
//            if (range == null) {
//                range = "Range: bytes=0-1023";
//            }
//
//            long rangeStart = Longs.tryParse(range.replace("Range: bytes=", "").split("-")[0]);//parse range header, which is bytes=0-10000 or something like that
//            long rangeEnd = Longs.tryParse(range.replace("bytes=", "").split("-")[1]);//parse range header, which is bytes=0-10000 or something like that
//            long contentLenght = fileBytes.length;//you must have it somewhere stored or read the full file size
//
//            InputStream inputStream = fileService.getFileInputStreamFromFileStorage(fileDir + "/" + fileName);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType."video/mp4");
//            headers.set("Accept-Ranges", "bytes");
//            headers.set("Expires", "0");
//            headers.set("Cache-Control", "no-cache, no-store");
//            headers.set("Connection", "keep-alive");
//            headers.set("Content-Transfer-Encoding", "binary");
//            headers.set("Content-Length", String.valueOf(rangeEnd - rangeStart));
//
//            //if start range assume that all content
//            if (rangeStart == 0) {
//                return new ResponseEntity<>(new InputStreamResource(inputStream), headers, OK);
//            } else {
//                headers.set("Content-Range", format("bytes %s-%s/%s", rangeStart, rangeEnd, contentLenght));
//                return new ResponseEntity<>(new InputStreamResource(inputStream), headers, PARTIAL_CONTENT);
//            }
//        } catch (IOException ex) {
//            return null; // TODO: 16/09/20 is it stable if file not found?
//        }
//    }
}
