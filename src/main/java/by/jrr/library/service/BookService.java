package by.jrr.library.service;

import by.jrr.auth.service.UserService;
import by.jrr.feedback.bean.EntityType;
import by.jrr.library.bean.Book;
import by.jrr.library.repository.BookRepository;
import by.jrr.profile.service.ProfilePossessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final Supplier<Integer> DEFAULT_PAGE_NUMBER = () -> 1;
    private final Supplier<Integer> DEFAULT_ELEMENTS_PER_PAGE = () -> 15;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProfilePossessesService pss;


    public Page<Book> findAll(String page, String items) {
        Page<Book> books;
        try {
            books = bookRepository.findAll(PageRequest.of(Integer.valueOf(page), Integer.valueOf(items)));
        } catch (Exception ex) {
            books = bookRepository.findAll(PageRequest.of(Integer.valueOf(0), Integer.valueOf(10)));
        }
        return books;
    }

    public Book create(Book book) {
        book = bookRepository.save(book);
        pss.savePossessForCurrentUser(book.getId(), EntityType.BOOK);
        return book;
    }
    public Book update(Book book) {
        book = bookRepository.save(book);
        return book;
    }
    public void delete() {
    }
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }


    public Page<Book> findAllPageable(Optional<Integer> userFriendlyNumberOfPage,
                                      Optional<Integer> numberOfElementsPerPage,
                                      Optional<String> searchTerm) {
        // pages are begins from 0, but userFriendly is to begin from 1
        int page = userFriendlyNumberOfPage.orElseGet(DEFAULT_PAGE_NUMBER) - 1;
        int elem = numberOfElementsPerPage.orElseGet(DEFAULT_ELEMENTS_PER_PAGE);

        if(searchTerm.isPresent()) {
            return searchByAllFieldsPageable(searchTerm.get(), page, elem);
        } else {
            Page<Book> page1 = bookRepository.findAll(PageRequest.of(page, elem));
            return page1;
        }
    }

    private Page<Book> searchByAllFieldsPageable(String searchTerm, int page, int elem) {
        List<Book> bookList = serarchBookByAllFields(searchTerm);
        if(bookList.size() != 0) {
            // TODO: 26/05/20 this pagination should be moved in a static method
            Pageable pageable = PageRequest.of(page, elem);
            int pageOffset = (int) pageable.getOffset(); // TODO: 26/05/20 dangerous cast!
            int toIndex = (pageOffset + elem) > bookList.size() ? bookList.size() : pageOffset + elem;
            Page<Book> bookPageImpl  = new PageImpl<>(bookList.subList(pageOffset, toIndex), pageable, bookList.size());
            return bookPageImpl;
        } else {
            return Page.empty();
        }
    }

    private List<Book> serarchBookByAllFields(String searchTerm) {
        return bookRepository.findByNameContaining(searchTerm)
                .and(bookRepository.findByAuthorContaining(searchTerm)
                        .and(bookRepository.findByEditionContaining(searchTerm)
                        .and(bookRepository.findByImgContaining(searchTerm)
                        .and(bookRepository.findByIsbnContaining(searchTerm)
                        .and(bookRepository.findByPublishedContaining(searchTerm)
                        .and(bookRepository.findByPublisherContaining(searchTerm)))))))
                .stream()
                .distinct()// TODO: 15/06/20 не дистинктит. переписать equals & hashcode?
                .collect(Collectors.toList());
    }
}
