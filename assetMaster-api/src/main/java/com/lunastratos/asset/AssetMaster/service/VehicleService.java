package com.lunastratos.asset.AssetMaster.service;

import com.lunastratos.asset.AssetMaster.dto.VehicleRequest;
import com.lunastratos.asset.AssetMaster.entity.Vehicle;
import com.lunastratos.asset.AssetMaster.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getVehiclesByRoom(Long roomId) {
        return vehicleRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
    }

    @Transactional
    public Vehicle addVehicle(Long roomId, VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setRoomId(roomId);
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setDescription(request.getDescription());
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Optional<Vehicle> updateVehicle(Long vehicleId, VehicleRequest request) {
        return vehicleRepository.findById(vehicleId).map(vehicle -> {
            vehicle.setLicensePlate(request.getLicensePlate());
            vehicle.setVehicleType(request.getVehicleType());
            vehicle.setDescription(request.getDescription());
            return vehicleRepository.save(vehicle);
        });
    }

    @Transactional
    public boolean deleteVehicle(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).map(vehicle -> {
            vehicleRepository.delete(vehicle);
            return true;
        }).orElse(false);
    }
}
