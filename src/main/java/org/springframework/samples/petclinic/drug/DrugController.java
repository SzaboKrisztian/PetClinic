package org.springframework.samples.petclinic.drug;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DrugController {

    private final DrugRepository drugRepo;

    public DrugController(DrugRepository drugs) {
        this.drugRepo = drugs;
    }

    @GetMapping("/drugs")
    public String seeDrugsList(Map<String, Object> model) {
        model.put("drugs", drugRepo.findAll());
        return "drugs/drugs";
    }

}
