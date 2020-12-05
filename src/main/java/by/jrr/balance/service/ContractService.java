package by.jrr.balance.service;

import by.jrr.balance.bean.*;
import by.jrr.balance.constant.OperationRowDirection;
import by.jrr.balance.repository.*;
import by.jrr.profile.bean.Profile;
import by.jrr.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    ContractRepository contractRepository;
    @Autowired
    AcceptanceActRepository acceptanceActRepository;
    @Autowired
    ContractTypeRepository contractTypeRepository;
    @Autowired
    ContractToProfileRepository contractToProfileRepository;
    @Autowired
    ContractToOperationRowRepository contractToOperationRowRepository;
    @Autowired
    OperationToProfileRepository operationToProfileRepository;
    @Autowired
    OperationRowService operationRowService;
    @Autowired
    ProfileService profileService;

    public void save(Contract contract, Long streamId, Long subscriberId, AcceptanceAct acceptanceAct) {
        boolean newContract = contract.getId() == null ? true : false;
        contract = contractRepository.save(contract);
        if (newContract) {
            contractToProfileRepository.save(ContractToProfile.builder()
                    .streamId(streamId)
                    .subscriberId(subscriberId)
                    .contractId(contract.getId())
                    .build());
        } else { // TODO: 12/10/2020 simplify this as with billing form
            Optional<ContractToProfile> contractToProfile = contractToProfileRepository.findByContractId(contract.getId());
            if(contractToProfile.isPresent()) {
                contractToProfileRepository.save(ContractToProfile.builder()
                        .id(contractToProfile.get().getId())
                        .streamId(streamId)
                        .subscriberId(subscriberId)
                        .contractId(contract.getId())
                        .build());
            }

        }
        acceptanceAct.setContractId(contract.getId());
        save(acceptanceAct);
        saveOrUpdateOperationRowForContract(contract);
    }

    private void saveOrUpdateOperationRowForContract(Contract contract) {
        ContractToOperationRow contractToOperationRow = contractToOperationRowRepository.findByContractId(contract.getId())
                .orElseGet(ContractToOperationRow::new);

        OperationRow operationRow = operationRowService.saveRow(
                OperationRow.builder()
                    .id(contractToOperationRow.getOperationRowId())
                    .date(contract.getDate())
                    .sum(contract.getSum())
                    .currency(contract.getCurrency())
                    .operationRowDirection(OperationRowDirection.CONTRACT)
                    .note(String.format("сумма обязательства по договору от %s № %s", contract.getDate(), contract.getNumber()))
                .build());

        contractToOperationRow.setOperationRowId(operationRow.getId());
        contractToOperationRow.setContractId(contract.getId());
        contractToOperationRowRepository.save(contractToOperationRow);

        OperationToProfile operationToProfile = operationToProfileRepository.findByOperationRowId(operationRow.getId())
                .orElseGet(OperationToProfile::new);
        ContractToProfile contractToProfile = contractToProfileRepository.findByContractId(contract.getId())
                .orElseGet(ContractToProfile::new);
        operationToProfile.setContractId(contract.getId());
        operationToProfile.setOperationRowId(operationRow.getId());
        operationToProfile.setStreamId(contractToProfile.getStreamId());
        operationToProfile.setSubscriberId(contractToProfile.getSubscriberId());
        operationToProfileRepository.save(operationToProfile);
    }

    public void save(ContractType contractType) {
        contractTypeRepository.save(contractType);
    }

    public void save(AcceptanceAct acceptanceAct) {
        acceptanceActRepository.save(acceptanceAct);
    }

    public AcceptanceAct getAcceptanceActById(Long id) {
        return acceptanceActRepository.findById(id).orElseGet(AcceptanceAct::new);
    }

    public List<ContractType> getContractTypes() {
        return (List) contractTypeRepository.findAll();
    }

    public List<Contract> findAllContracts() {
        List<ContractToProfile> contractToProfileStream = (List) contractToProfileRepository.findAll();

        List<Long> contractIds = contractToProfileStream.stream()
                .map(c -> c.getContractId())
                .collect(Collectors.toList());
        List<Contract> contractsList = contractRepository.findAllByIdIn(contractIds);
        contractsList.stream()
                .peek(this::setContractTypeToContract)
                .peek(this::setContractTypeToContract)
                .peek(this::setAcceptanceActToContract)
                .peek(this::setIncomesToContract)
                .forEach(this::setUserProfileToContract);
        return contractsList;
    }

    public Contract findContractByIdLazy(Long id) {
        if(id != null) {
            return contractRepository.findById(id).orElseGet(Contract::new);
        }
        return new Contract();
    }

    public List<Contract> findContractsForStream(Long streamId) {
        List<Long> contractIds = contractToProfileRepository.findAllByStreamId(streamId).stream()
                .map(c -> c.getContractId())
                .collect(Collectors.toList());
        List<Contract> contractsList = contractRepository.findAllByIdIn(contractIds);
        contractsList.stream()
                .peek(this::setContractTypeToContract)
                .peek(this::setAcceptanceActToContract)
                .peek(this::setIncomesToContract)
                .forEach(this::setUserProfileToContract);
        return contractsList;
    }

    public List<Contract> findAllContractsForProfileIdLazy(Long subscriberId) {
        List<Long> contractIds = contractToProfileRepository.findAllBySubscriberId(subscriberId).stream()
                .map(c -> c.getContractId())
                .collect(Collectors.toList());
        List<Contract> contractsList = contractRepository.findAllByIdIn(contractIds);
        return contractsList;
    }

    private void setContractTypeToContract(Contract contract) {
        contract.setContractType(contractTypeRepository
                .findById(contract.getContractTypeId())
                .orElseGet(ContractType::new));
    }

    private void setAcceptanceActToContract(Contract contract) {
        contract.setAcceptanceAct(acceptanceActRepository
                .findByContractId(contract.getId())
                .orElseGet(AcceptanceAct::new));
    }

    private void setIncomesToContract(Contract contract) {
        contract.setIncomes(operationRowService.getIncomesForContract(contract.getId()));
    }

    private void setUserProfileToContract(Contract contract) {
        Long userProfileId = contractToProfileRepository
                .findByContractId(contract.getId())
                .orElseGet(ContractToProfile::new)
                .getSubscriberId();

        contract.setUserProfile(
                profileService.findProfileByProfileIdLazy(userProfileId).orElseGet(Profile::new));
    }
}
