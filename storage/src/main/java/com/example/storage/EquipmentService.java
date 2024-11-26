package com.example.storage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    public List<Equipment> getAllEquipment() {
        return repository.findAll();
    }

    public List<Equipment> getAvailableEquipment() {
        return repository.findByStatus(Status.AVAILABLE);
    }

    public List<Equipment> getRentedEquipment() {
        return repository.findByStatus(Status.RENTED);
    }

    public List<Equipment> getDamagedEquipment() {
        return repository.findByStatus(Status.DAMAGED);
    }

    public List<Equipment> getUnderRepairEquipment() {
        return repository.findByStatus(Status.UNDER_REPAIR);
    }

    public List<Equipment> getAvailableEquipmentByType(EquipmentType type) {
        return repository.findByTypeAndStatus(type, Status.AVAILABLE);
    }

    public Equipment saveEquipment(Equipment equipment) {
        return repository.save(equipment);
    }

    public void updateStatus(Long id, Status newStatus) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(newStatus);
        repository.save(equipment);
    }
}

