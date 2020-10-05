package by.jrr.balance.acontract.repository;

import by.jrr.balance.acontract.bean.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
}
