package by.jrr.balance.currency.mapper;

import by.jrr.balance.currency.bean.BynUsdRate;
import by.jrr.balance.currency.dto.RateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateMappingStrategy.class)
public interface RateMapper {

    RateMapper OF = Mappers.getMapper(RateMapper.class);

    @Mapping(source = "Date", target = "date")
    @Mapping(source = "Cur_Abbreviation", target = "currAbbr")
    @Mapping(source = "Cur_OfficialRate", target = "sum")
    BynUsdRate getBynUsdFromRateDto(RateDto rateDto);
}
