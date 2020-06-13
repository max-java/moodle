package by.jrr.files.service;

import by.jrr.files.bean.FileBytes;
import by.jrr.files.bean.FileMeta;
import by.jrr.files.constant.FileType;
import by.jrr.files.repository.FileBytesRepository;
import by.jrr.files.repository.FileMetaRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileMetaRepository fileMetaRepository;
    @Autowired
    FileBytesRepository fileBytesRepository;
    @Autowired
    ProfilePossessesService pss;

    public static final String FILE_STORAGE = "/home/max/web/files/";


    public String saveUploaded(MultipartFile file, Optional<String> description) throws IOException {
        FileMeta fileMeta = new FileMeta(file);
        if (description.isPresent()) {
            fileMeta.setDescription(description.get());
        }
        fileMeta = fileMetaRepository.save(fileMeta);

        if(fileMeta.getExtension().equals("pdf")) {
            Path path = Paths.get(FILE_STORAGE + fileMeta.getNameWithExtension());
            Files.copy(file.getInputStream(), path);
        } else {
            FileBytes fileBytes = new FileBytes(file, fileMeta);
            fileBytesRepository.save(fileBytes);
        }

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

    public byte[] getPdfFromFileStorage(String name) {
        Path path = Paths.get(FILE_STORAGE + name);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            return "".getBytes();
        }
    }
}
