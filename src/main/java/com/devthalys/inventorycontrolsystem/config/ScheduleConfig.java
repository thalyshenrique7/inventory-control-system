package com.devthalys.inventorycontrolsystem.config;

import com.devthalys.inventorycontrolsystem.models.BinModel;
import com.devthalys.inventorycontrolsystem.repositories.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class ScheduleConfig {

    @Autowired
    private BinRepository binRepository;
    private final long day = 1000 * 60 * 60 * 24;
    private final long month = day * 30;


    @Scheduled(fixedDelay = month)
    public void deletePermanent(){
        List<BinModel> binReports = binRepository.findByDeleted(true);

        for(BinModel bin : binReports){
            binRepository.delete(bin);
        }
    }
}
