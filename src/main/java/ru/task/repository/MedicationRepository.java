package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.task.model.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByCode(String code);

    @Query("SELECT m FROM Medication m WHERE m.code IN :codes")
    List<Medication> findByCodes(@Param("codes") List<String> codes);

    void deleteByCode(String code);
}
