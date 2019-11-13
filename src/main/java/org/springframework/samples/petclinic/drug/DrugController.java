package org.springframework.samples.petclinic.drug;

import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class DrugController {

  private final String ADD_OR_UPDATE_DRUG_VIEW = "drugs/addOrUpdateDrug";

  private final DrugRepository drugRepo;
  private final PetRepository pets;

  public DrugController(DrugRepository drugs, PetRepository pets) {
    this.drugRepo = drugs;
    this.pets = pets;
  }

  @InitBinder("drug")
  public void initDrugBinder(WebDataBinder dataBinder) {
    dataBinder.setValidator(new DrugValidator());
  }

  @ModelAttribute("allPetTypes")
  public Set<PetType> populatePetTypes() {
    return new TreeSet<>(this.pets.findPetTypes());
  }

  @GetMapping("/drugs")
  public String seeDrugsList(Map<String, Object> model) {
    model.put("drugs", drugRepo.findAll());
    return "drugs/drugsList";
  }

  @GetMapping("/drugs/new")
  public String initAddDrugForm(Map<String, Object> model) {
    model.put("drug", new Drug());
    return ADD_OR_UPDATE_DRUG_VIEW;
  }

  @PostMapping("/drugs/new")
  public String processAddDrugForm(@Valid Drug drug, BindingResult result, Map<String, Object> model) {
    if (result.hasErrors()) {
      model.put("drug", drug);
      return ADD_OR_UPDATE_DRUG_VIEW;
    } else {
      this.drugRepo.save(drug);
      return "redirect:/drugs";
    }
  }

  @GetMapping("/drugs/edit/{drug_Id}")
  public String initUpdateDrugForm(@PathVariable("drug_Id") int drug_Id, Map<String, Object> model) {
    Drug drug = this.drugRepo.findById(drug_Id);
    model.put("drug", drug);
    return ADD_OR_UPDATE_DRUG_VIEW;
  }

  @PostMapping("/drugs/edit/{drug_Id}")
  public String processUpdateDrugForm(@Valid Drug drug, BindingResult result, ModelMap model) {
    if (result.hasErrors()) {
      model.put("drug", drug);
      return ADD_OR_UPDATE_DRUG_VIEW;
    } else {
      this.drugRepo.save(drug);
      return "redirect:/drugs";
    }
  }

  @GetMapping("/drugs/delete/{drug_Id}")
  public String deleteDrug(@PathVariable("drug_Id") int drugId) {
    this.drugRepo.deleteDrugById(drugId);
    return "redirect:/drugs";
  }
}
