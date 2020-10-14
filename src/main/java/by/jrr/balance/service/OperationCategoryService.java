package by.jrr.balance.service;

import by.jrr.balance.bean.OperationCategory;
import by.jrr.balance.repository.OperationCategoryRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class OperationCategoryService {

    @Autowired
    OperationCategoryRepository operationCategoryRepository;

    public void save(OperationCategory operationCategory) {
        operationCategoryRepository.save(operationCategory);
    }

    public List<OperationCategory> getAllOperationCategories() {
        return (List) operationCategoryRepository.findAll();
    }
}
