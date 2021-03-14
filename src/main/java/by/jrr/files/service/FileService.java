package by.jrr.files.service;

import by.jrr.files.bean.FileBytes;
import by.jrr.files.bean.FileMeta;
import by.jrr.files.constant.FileType;
import by.jrr.files.repository.FileBytesRepository;
import by.jrr.files.repository.FileMetaRepository;
import by.jrr.profile.service.ProfilePossessesService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
    @Autowired
    AmazonS3 amazonS3;


    public static final String FILE_STORAGE = "/home/max/web/files/";
    public static final String AWS_BUCKET_NAME = "tutrit-public-files";


    public String saveUploaded(MultipartFile file, Optional<String> description) throws IOException {
        FileMeta fileMeta = new FileMeta(file);
        if (description.isPresent()) {
            fileMeta.setDescription(description.get());
        }
        fileMeta = fileMetaRepository.save(fileMeta);

        if(fileMeta.getExtension().equals("pdf")) {
//            uploadToFileSystem(fileMeta, file)
            uploadToS3(fileMeta, file);
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

    public byte[] getFileBytesFromFileStorage(String name) {
        Path path = Paths.get(FILE_STORAGE + name);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            return "".getBytes();
        }
    }

    public byte[] getFileBytesFromS3(String name) {
        try {
            return amazonS3
                    .getObject(new GetObjectRequest(AWS_BUCKET_NAME, name))
                    .getObjectContent()
                    .readAllBytes();
        } catch (Exception ex) {
            return "".getBytes();
        }
    }

    private void uploadToS3(FileMeta fileMeta, MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            amazonS3.putObject(AWS_BUCKET_NAME, fileMeta.getNameWithExtension(), file.getInputStream(), new ObjectMetadata());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    private void uploadToFileSystem(FileMeta fileMeta, MultipartFile file) {
        try {
            Path path = Paths.get(FILE_STORAGE + fileMeta.getNameWithExtension());
            Files.copy(file.getInputStream(), path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
