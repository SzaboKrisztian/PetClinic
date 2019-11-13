package org.springframework.samples.petclinic.drug;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DrugController.class)
public class DrugControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DrugRepository drugRepo;

  @MockBean
  private PetRepository petRepo;

  @BeforeEach
  void setup() {
    PetType dog = new PetType();
    dog.setId(1);
    dog.setName("dog");
    PetType cat = new PetType();
    cat.setId(2);
    cat.setName("cat");
    PetType rat = new PetType();
    rat.setId(3);
    rat.setName("rat");
    given(this.petRepo.findPetTypes()).willReturn(Lists.newArrayList(dog, cat, rat));

    Drug someDrug = new Drug();
    someDrug.setId(1);
    someDrug.setName("Some Drug");
    someDrug.setPetTypes(Sets.newLinkedHashSet(dog, cat));
    someDrug.setBatchNumber("SD1233B01");
    someDrug.setExpiryDate(LocalDate.now().plusYears(2));
    given(this.drugRepo.findAll()).willReturn(Lists.newArrayList(someDrug));
    given(this.drugRepo.findById(1)).willReturn(someDrug);
  }

  @Test
  void testDrugList() throws Exception {
    mockMvc.perform(get("/drugs"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("allPetTypes"))
        .andExpect(model().attributeExists("drugs"))
        .andExpect(view().name("drugs/drugsList"));
  }

  @Test
  void testInitCreationForm() throws Exception {
    mockMvc.perform(get("/drugs/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("allPetTypes"))
        .andExpect(model().attributeExists("drug"))
        .andExpect(view().name("drugs/addOrUpdateDrug"));
  }

  @Test
  void testProcessCreationFormSuccess() throws Exception {
    mockMvc.perform(post("/drugs/new")
        .param("name", "New Drug")
        .param("batchNumber", "SBN4431L01")
        .param("expiryDate", "2025-12-12")
        //.param("_petTypes", "1") No clue how to test the multiple select box
        //.param("petTypes", "dog")
        //.param("petTypes", "cat")
        .param("id", "")
    )
        .andExpect(status().is3xxRedirection());
  }

  @Test
  void testProcessCreationFormHasErrors() throws Exception {
    mockMvc.perform(post("/drugs/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("drugs/addOrUpdateDrug"))
        .andExpect(model().attributeHasErrors("drug"))
        .andExpect(model().attributeHasFieldErrors("drug", "name", "batchNumber", "expiryDate"));
  }

  @Test
  void testInitUpdateForm() throws Exception {
    mockMvc.perform(get("/drugs/edit/{drug_id}", 1))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("allPetTypes"))
        .andExpect(model().attributeExists("drug"))
        .andExpect(model().attribute("drug", drugRepo.findById(1)))
        .andExpect(view().name("drugs/addOrUpdateDrug"));
  }

  @Test
  void testProcessUpdateFormSuccess() throws Exception {
    mockMvc.perform(post("/drugs/edit/{drug_Id}", 1)
        .param("name", "Changed name")
        .param("batchNumber", "Makes no sense")
        .param("expiryDate", "2099-12-12")
        .param("id", "1")
    )
        .andExpect(status().is3xxRedirection());
  }

  @Test
  void testProcessUpdateFormHasErrors() throws Exception {
    mockMvc.perform(post("/drugs/edit/{drug_Id}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("drugs/addOrUpdateDrug"))
        .andExpect(model().attributeHasErrors("drug"))
        .andExpect(model().attributeHasFieldErrors("drug", "name", "batchNumber", "expiryDate"));
  }

  @Test
  void testDeleteDrugEntry() throws Exception {
    mockMvc.perform(get("/drugs/delete/{drug_Id}", 1))
        .andExpect(status().is3xxRedirection());
  }
}
