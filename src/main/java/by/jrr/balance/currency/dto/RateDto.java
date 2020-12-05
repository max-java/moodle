package by.jrr.balance.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDto {
    public int Cur_ID;
    public LocalDateTime Date;
    public String Cur_Abbreviation;
    public int Cur_Scale;
    public String Cur_Name;
    public double Cur_OfficialRate;
}
