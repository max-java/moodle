package by.jrr.balance.service;

import by.jrr.balance.bean.Contract;
import by.jrr.balance.bean.OperationToProfile;
import by.jrr.balance.repository.OperationToProfileRepository;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperationToProfileService {

    @Autowired
    OperationToProfileRepository operationToProfileRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    ContractService contractService;

    public void save(OperationToProfile operationToProfile) {
        operationToProfileRepository.save(operationToProfile);
    }

    public List<Long> getIdOperationsForStreamById(Long id) {
        return operationToProfileRepository.findAllByStreamId(id).stream()
                .map(OperationToProfile::getOperationRowId)
                .collect(Collectors.toList());

    }

    public List<Long> getIdOperationsForUserByUserProfileId(Long id) {
        return operationToProfileRepository.findAllBySubscriberId(id).stream()
                .map(OperationToProfile::getOperationRowId)
                .collect(Collectors.toList());

    }

    public List<Long> getIdOperationsForContractId(Long id) {
        return operationToProfileRepository.findAllByContractId(id).stream()
                .map(OperationToProfile::getOperationRowId)
                .collect(Collectors.toList());
    }

    public List<Long> getIdOperationsWhereContractIsNull() {
        return operationToProfileRepository.findAllByContractIdIsNull().stream()
                .map(OperationToProfile::getOperationRowId)
                .collect(Collectors.toList());
    }

    public Profile getSubscriberProfileForOperationByOperationId(Long id) {
        try {
            OperationToProfile operationToProfile = operationToProfileRepository.findByOperationRowId(id).orElseGet(OperationToProfile::new);
            Profile profile = profileService.findProfileByProfileIdLazy(operationToProfile.getSubscriberId()).orElseGet(Profile::new);
            return profile;
        } catch (Exception ex) {
            // TODO: 12/10/2020 when contract not set: to reporduce - 1: set contract to operation row, 2 - unset it;
        }
        return new Profile();
    }

    public Contract getContractForOperationByOperationId(Long id) {
        OperationToProfile operationToProfile = operationToProfileRepository.findByOperationRowId(id).orElseGet(OperationToProfile::new); // TODO: 12/10/2020 should not be empty.
        return contractService.findContractByIdLazy(operationToProfile.getContractId()); // TODO: 12/10/2020 move optional to the root service, consider to handle somehow )
    }

    public Long getOperationToProfileIdByOperationRoleId(Long operationRowId) {
        Optional<OperationToProfile> operationToProfile =
                operationToProfileRepository.findByOperationRowId(operationRowId);
        if (operationToProfile.isPresent()) {
            return operationToProfile.get().getId();
        } else {
            return null;
        }
    }
}
