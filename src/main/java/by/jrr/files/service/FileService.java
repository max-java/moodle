package by.jrr.files.service;

import by.jrr.files.bean.FileBytes;
import by.jrr.files.bean.FileMeta;
import by.jrr.files.repository.FileBytesRepository;
import by.jrr.files.repository.FileMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileMetaRepository fileMetaRepository;
    @Autowired
    FileBytesRepository fileBytesRepository;


    public String saveUploaded(MultipartFile file) throws IOException {
        FileMeta fileMeta = new FileMeta(file);
        fileMeta = fileMetaRepository.save(fileMeta);
        FileBytes fileBytes = new FileBytes(file, fileMeta);
        fileBytesRepository.save(fileBytes);
        return fileMeta.getNameWithExtension();
    }

    public byte[] getFileBytes(String name)  {
        Optional<FileBytes> fileBytes = fileBytesRepository.findByFileName(name);
        if (fileBytes.isPresent()) {
            return fileBytes.get().getBytes();
        } else {
            return "".getBytes();
        }
    }

    public List<FileMeta> getAllFileMetas() {
        Iterable<FileMeta> result = fileMetaRepository.findAll();
        return (List<FileMeta>) result;
    }
}
