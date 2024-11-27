package com.example.storage;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Retry(name = "retry")
    public List<Equipment> getAllEquipment() {
        return repository.findAll();
    }

    @Retry(name = "retry")
    public List<Equipment> getAvailableEquipment() {
        return repository.findByStatus(Status.AVAILABLE);
    }

    @Retry(name = "retry")
    public List<Equipment> getRentedEquipment() {
        return repository.findByStatus(Status.RENTED);
    }

    @Retry(name = "retry")
    public List<Equipment> getDamagedEquipment() {
        return repository.findByStatus(Status.DAMAGED);
    }

    @Retry(name = "retry")
    public List<Equipment> getUnderRepairEquipment() {
        return repository.findByStatus(Status.UNDER_REPAIR);
    }

    @Retry(name = "retry")
    public List<Equipment> getAvailableEquipmentByType(EquipmentType type) {
        return repository.findByTypeAndStatus(type, Status.AVAILABLE);
    }

    @Retry(name = "retry")
    public Equipment saveEquipment(Equipment equipment) {
        return repository.save(equipment);
    }

    @Retry(name = "retry")
    public void updateStatus(Long id, Status newStatus) {
        Equipment equipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setStatus(newStatus);
        repository.save(equipment);
    }
}
