package com.example.storage;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return service.getAllEquipment();
    }

    @GetMapping("/available")
    public List<Equipment> getAvailableEquipment() {
        return service.getAvailableEquipment();
    }

    @GetMapping("/rented")
    public List<Equipment> getRentedEquipment() {
        return service.getRentedEquipment();
    }

    @GetMapping("/damaged")
    public List<Equipment> getDamagedEquipment() {
        return service.getDamagedEquipment();
    }

    @GetMapping("/underrepair")
    public List<Equipment> getUnderRepairEquipment() {
        return service.getUnderRepairEquipment();
    }

    @GetMapping("/available/{type}")
    public List<Equipment> getAvailableEquipmentByType(@PathVariable EquipmentType type) {
        return service.getAvailableEquipmentByType(type);
    }

    @PostMapping
    public Equipment saveEquipment(@RequestBody Equipment equipment) {
        return service.saveEquipment(equipment);
    }

    public static class StatusRequest {
        private Status status;

        // Getters and setters
        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        service.updateStatus(id, request.getStatus());
    }

}
