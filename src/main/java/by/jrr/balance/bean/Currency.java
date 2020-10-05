package by.jrr.balance.bean;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum  Currency {

    BYN (933,  "BYN", "Белорусский рубль"),
    USD (840, "USD", "Доллар США");

    public int cur_ID;
    public int cur_ParentID;
    public int code;
    public String abbreviation;
    public String name;
    public String cur_Name_Bel;
    public String cur_Name_Eng;
    public String cur_QuotName;
    public String cur_QuotName_Bel;
    public String cur_QuotName_Eng;
    public String cur_NameMulti;
    public String cur_Name_BelMulti;
    public String cur_Name_EngMulti;
    public int cur_Scale;
    public int cur_Periodicity;
    public LocalDateTime cur_DateStart;
    public LocalDateTime cur_DateEnd;


    Currency(int code, String abbreviation, String name) {

        this.code = code;
        this.abbreviation = abbreviation;
        this.name = name;
    }
}
