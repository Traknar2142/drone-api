package ru.task.service;

import ru.task.model.Medication;

import java.util.List;

public interface MedicationService {
    Medication saveMedication(Medication medication);
    Medication findByCode(String code);
    List<Medication> findByCodeList(List<String> codes);
    List<Medication> findAll();
    Medication updateMedication(Medication medication);
    void deleteByCode(String code);
}
