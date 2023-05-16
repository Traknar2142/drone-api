package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Medication;
import ru.task.repository.MedicationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    @Transactional
    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    @Transactional(readOnly = true)
    public Medication findByCode(String code) {
        return medicationRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Medication with code " + code + " is not found "));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> findByCodeList(List<String> codes) {
        List<Medication> medications = medicationRepository.findByCodes(codes);
        if (medications.isEmpty()) {
            throw new EntityNotFoundException("Medicine is not found by codes: " + codes);
        } else {
            return medications;
        }
    }

    @Override
    @Transactional
    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    @Override
    @Transactional
    public Medication updateMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    @Transactional
    public void deleteByCode(String code) {
        medicationRepository.deleteByCode(code);
    }
}
