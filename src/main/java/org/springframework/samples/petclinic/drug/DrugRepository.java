package org.springframework.samples.petclinic.drug;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DrugRepository extends Repository<Drug, Integer> {

    @Transactional(readOnly = true)
    Drug findById(Integer id);

    @Transactional(readOnly = true)
    List<Drug> findAll();

    void save(Drug drug);
}
