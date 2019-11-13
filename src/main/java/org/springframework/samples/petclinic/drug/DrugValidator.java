package org.springframework.samples.petclinic.drug;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DrugValidator implements Validator {

  private final String REQ = "required";

  @Override
  public boolean supports(Class<?> aClass) {
    return Drug.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    Drug drug = (Drug) o;

    if (!StringUtils.hasLength(drug.getName())) {
      errors.rejectValue("name", REQ, REQ);
    }

    if (!StringUtils.hasLength(drug.getBatchNumber())) {
      errors.rejectValue("batchNumber", REQ, REQ);
    }

    if (drug.getExpiryDate() == null) {
      errors.rejectValue("expiryDate", REQ, REQ);
    }
  }
}
