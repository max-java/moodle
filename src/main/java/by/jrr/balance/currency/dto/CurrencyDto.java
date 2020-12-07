package by.jrr.balance.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    public int Cur_ID;
//    public Nullable<int> Cur_ParentID;
    public String Cur_Code;
    public String Cur_Abbreviation;
    public String Cur_Name;
    public String Cur_Name_Bel;
    public String Cur_Name_Eng;
    public String Cur_QuotName;
    public String Cur_QuotName_Bel;
    public String Cur_QuotName_Eng;
    public String Cur_NameMulti;
    public String Cur_Name_BelMulti;
    public String Cur_Name_EngMulti;
    public int Cur_Scale;
    public int Cur_Periodicity;
    public LocalDateTime Cur_DateStart;
    public LocalDateTime Cur_DateEnd;
}
