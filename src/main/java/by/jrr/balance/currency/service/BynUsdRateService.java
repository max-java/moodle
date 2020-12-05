package by.jrr.balance.currency.service;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.dto.RateDto;
import by.jrr.balance.currency.mapper.RateMapper;
import by.jrr.balance.currency.repository.BynUsdRateRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
public class BynUsdRateService {

    public final BynUsdRateRepository bynUsdRateRepository;

    public BynUsdRateService(BynUsdRateRepository bynUsdRateRepository) {
        this.bynUsdRateRepository = bynUsdRateRepository;
    }

    public BynUsdRate saveRate(RateDto rateDto) {
        return bynUsdRateRepository.save(RateMapper.OF.getBynUsdFromRateDto(rateDto));
    }
    public BynUsdRate getRateOnDate(LocalDate localDate) throws EntityNotFoundException {
        return bynUsdRateRepository.findByDate(localDate).orElseGet(BynUsdRate::new); //todo consider handle with exception
    }
}
