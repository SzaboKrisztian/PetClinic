/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.drug;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.owner.PetType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Simple business object representing a drug.
 *
 * @author Krisztian Szabo
 */
@Entity
@Table(name = "drugs")
public class Drug extends NamedEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "drugs_for_animal", joinColumns = @JoinColumn(name = "drug_id"), inverseJoinColumns = @JoinColumn(name = "animal_type_id"))
    public Set<PetType> petTypes;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "expiry_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    public void setPetTypes(Set<PetType> petTypes) {
        this.petTypes = petTypes;
    }

    public Set<PetType> getPetTypes() {
        return this.petTypes;
    }

    public int getNrOfPetTypes() {
        return this.petTypes.size();
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
