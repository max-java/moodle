package by.jrr.files.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileBytes {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;
    private Long fileMetaId;
    @Lob
    private byte[] bytes;
    @Column(unique = true)
    private String fileName; // to select bytes only directly by filename to avoid Brute-force

    public FileBytes(MultipartFile uploadedFile, FileMeta file) throws IOException {
        this.fileMetaId = file.getId();
        this.bytes = uploadedFile.getBytes();
        this.fileName = file.getNameWithExtension();
    }




}
