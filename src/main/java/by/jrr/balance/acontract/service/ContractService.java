package by.jrr.balance.acontract.service;

import by.jrr.balance.acontract.bean.Contract;
import by.jrr.balance.acontract.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    @Autowired
    ContractRepository contractRepository;

    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }
}
