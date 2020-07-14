package by.jrr.interview.service;

import by.jrr.interview.bean.QAndA;
import by.jrr.interview.repository.QAndARepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class QAndAService {

    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 30;

    @Autowired
    QAndARepository qAndARepository;
    @Autowired
    ProfilePossessesService pss;

    public Map<String, Long> totalQuestions() {
        List<QAndA> qAndAs = (List) qAndARepository.findAll();
        Map<String, Long> result = qAndAs.stream()
                .collect(Collectors.groupingBy(QAndA::getTheme, Collectors.counting())); // TODO: 31/05/20 see QAndA.class comment: throw NPE if theme is null
        return result;
    }

    public QAndA create(QAndA qAndA) {
        qAndA = qAndARepository.save(qAndA);
        return qAndA;
    }
    public QAndA update(QAndA qAndA) {
        qAndA = qAndARepository.save(qAndA);
        return qAndA;
    }
    public void delete() {
    }
    public Optional<QAndA> findById(Long id) {
        return qAndARepository.findById(id);
    }
    public void saveAll(List<QAndA> topicList) {
        qAndARepository.saveAll(topicList);
    }

//    =========================

    public Page<QAndA> findAllPageable(Optional<Integer> userFriendlyNumberOfPage,
                                       Optional<Integer> numberOfElementsPerPage,
                                       Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);

        if(searchTerm.isPresent()) {
            return searchByAllFieldsPageable(searchTerm.get(), page, elem);
        } else {
            return qAndARepository.findAll(PageRequest.of(page, elem));
        }

    }

    private Page<QAndA> searchByAllFieldsPageable(String seatchTerm, int page, int elem) {
        List<QAndA> qAndAList = serarchQAndAByAllFields(seatchTerm);
        if(qAndAList.size() != 0) {
            // TODO: 26/05/20 this pagination should be moved in a static method
            Pageable pageable = PageRequest.of(page, elem);
            int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
            int toIndex = (pageOffset + elem) > qAndAList.size() ? qAndAList.size() : pageOffset + elem;
            Page<QAndA> qAndAPageImpl  = new PageImpl<>(qAndAList.subList(pageOffset, toIndex), pageable, qAndAList.size());
            return qAndAPageImpl;
        } else {
            return Page.empty();
        }
    }

    private List<QAndA> serarchQAndAByAllFields(String searchTerm) {
        return qAndARepository.findByThemeContaining(searchTerm)
                .and(qAndARepository.findByQuestionContaining(searchTerm)
                        .and(qAndARepository.findByAnswerContaining(searchTerm)
                        )
                )
                .stream()
                .distinct()// TODO: 26/05/20 не дистинктит. переписать equals & hashcode?
                .collect(Collectors.toList());
    } // M.Shelkovich: see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details

    public Optional<QAndA> getRandomQuestion() {
        Long total = qAndARepository.count();
        int idx = (int)(Math.random() * total);
        Page<QAndA> questionPage = qAndARepository.findAll(PageRequest.of(idx, 1));
        if (questionPage.hasContent()) {
            return Optional.of(questionPage.getContent().get(0));
        }
        return Optional.empty();
    }
}
