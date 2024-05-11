package com.github.alex4790354.repository;


import com.github.alex4790354.general.dto.CurrencyDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface FeederCbrRepository {

    @Select("""
                SELECT cur.id, cur.name_rus, cur.name_eng, cur.nominal, cur.parent_code, cur.frequency, rate.first_crncy char_code
                    FROM feed.currency cur
                    INNER JOIN feed.currency_rate rate on cur.id = rate.id;
            """)
    List<CurrencyDto> getAllCurrencies();

}
