package by.jrr.balance.service;

import by.jrr.balance.bean.ContractToProfile;
import by.jrr.balance.repository.ContractToProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractToProfileService {

    @Autowired
    ContractToProfileRepository contractToProfileRepository;

    public void save(ContractToProfile contractToProfile) {
        contractToProfileRepository.save(contractToProfile);
    }

    public List<Long> getIdContractsForStreamById(Long id) {
        List<Long> result =  contractToProfileRepository.findAllByStreamId(id).stream()
                .map(o -> o.getContractId())
                .collect(Collectors.toList());
        return result;
    }
}

