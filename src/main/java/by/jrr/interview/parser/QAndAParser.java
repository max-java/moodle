package by.jrr.interview.parser;

import by.jrr.interview.bean.QAndA;
import by.jrr.interview.repository.QAndARepository;
import by.jrr.interview.service.QAndAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class QAndAParser {

    @Autowired
    QAndAService qAndAService = new QAndAService();

    public void run() {
        List<Path> dir = new ArrayList<>();
        try {
            dir = readDir();
        } catch (Exception e) {
            System.out.println("could not read directory");
        }
        for (Path topic : dir) {
            try {
                saveAll(parseFile(topic));
            } catch (Exception ex) {
                System.out.println("could not parse file " + topic.getFileName());
            }
        }
    }

    private List<Path> readDir() throws IOException {
        return Files
                .walk(Paths.get("./interview"))
                .collect(Collectors.toList());
    }

    private List<QAndA> parseFile(Path topic) throws IOException {
        List<String> linesAsCollection = Files.readAllLines(topic);
        List<QAndA> qAndAList = new ArrayList<>();
        QAndA qAndA = new QAndA();
        for (int i = 0; i < linesAsCollection.size(); i++) {
            String line = linesAsCollection.get(i);
            if (line.startsWith("# ")) {
                qAndA.setTheme(line);
                continue;
            }
            if (line.startsWith("##")) {
                qAndA = QAndA.builder().theme(qAndA.getTheme()).answer("").build();
                qAndA.setQuestion(line);
                continue;
            }
            if (line.contains("[к оглавлению]")) {
                qAndAList.add(qAndA);
                qAndA = QAndA.builder().theme(qAndA.getTheme()).answer("").build();
                continue;
            }
            qAndA.setAnswer(qAndA.getAnswer() + "\n<br />" + line);
        }
        return qAndAList;
    }

    private void saveAll(List<QAndA> qAndAList) {
        try{
            qAndAService.saveAll(qAndAList);
        }catch (Exception e) {
            System.out.println("could not save file " + e);
            e.printStackTrace();
        }
    }
}
