package by.jrr.balance.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class CurrencyDto {

        public int Cur_ID;
        public Optional<Integer> Cur_ParentID;
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
