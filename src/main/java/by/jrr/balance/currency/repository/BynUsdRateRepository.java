package by.jrr.balance.currency.repository;

import by.jrr.balance.currency.bean.BynUsdRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BynUsdRateRepository extends CrudRepository<BynUsdRate, Long> {
    Optional<BynUsdRate> findByDate(LocalDate date);
}
