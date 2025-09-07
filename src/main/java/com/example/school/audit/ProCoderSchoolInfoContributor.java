package com.example.school.audit;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProCoderSchoolInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder){
        Map<String, String> proMap = new HashMap<>();
        proMap.put("application name","Pro-Coder School");
        proMap.put("App Description", "Pro-Coder School Web Application for Students and Admin");
        proMap.put("App Version", "1.0.0");
        proMap.put("Contact Email", "info@proschool.com");
        proMap.put("Contact Mobile", "+1(21) 000 4158");
        builder.withDetail("proCoderSchoolInfo", proMap);
    }
}
