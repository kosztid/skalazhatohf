package com.example.storage;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByStatus(Status status);
    List<Equipment> findByTypeAndStatus(EquipmentType type, Status status);
}
