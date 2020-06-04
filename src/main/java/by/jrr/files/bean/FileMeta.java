package by.jrr.files.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMeta {

    @javax.persistence.Id
    @GeneratedValue
    private Long Id;

    @Column(unique = true)
    private String name;
    private String nameWithExtension;
    private String description;
    private String extension;
    private String contentType;
    private Long size;


    public FileMeta(MultipartFile file) {
        this.contentType = file.getContentType();
        this.extension = FilenameUtils.getExtension(file.getOriginalFilename());
        this.name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + UUID.randomUUID();
        this.size = file.getSize();
        this.nameWithExtension = name + "." + extension;
    }
}
