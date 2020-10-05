package by.jrr.balance.service;

import by.jrr.balance.bean.OperationToProfile;
import by.jrr.balance.beanrepository.OperationToProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationToProfileService {

    @Autowired
    OperationToProfileRepository operationToProfileRepository;

    public void save(OperationToProfile operationToProfile) {
        operationToProfileRepository.save(operationToProfile);
    }

    public List<Long> getIdOperationsForStreamById(Long id) {
        List<Long> result =  operationToProfileRepository.findAllByStreamId(id).stream()
                .map(o -> o.getOperationRowId())
                .collect(Collectors.toList());
        return result;
    }
}
