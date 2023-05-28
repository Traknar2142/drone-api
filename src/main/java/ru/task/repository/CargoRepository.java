package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.task.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
