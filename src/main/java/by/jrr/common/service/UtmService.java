package by.jrr.common.service;

import by.jrr.common.bean.UrchinTracking;
import by.jrr.common.repository.UrchinTrackingRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UtmService {

    @Autowired
    UrchinTrackingRepository urchinTrackingRepository;

    public void setTrack(Map<String,String> allParams) {
        if (allParams != null && allParams.size() >0 ) {
            urchinTrackingRepository.save(UrchinTracking.builder().timestamp(LocalDateTime.now()).allParams(allParams).build());
        }
    }


}
