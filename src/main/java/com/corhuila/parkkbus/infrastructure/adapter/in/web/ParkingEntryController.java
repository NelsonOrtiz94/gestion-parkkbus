package com.corhuila.parkkbus.infrastructure.adapter.in.web;

import com.corhuila.parkkbus.application.command.RegisterVehicleEntryCommand;
import com.corhuila.parkkbus.application.dto.ParkingSessionResponse;
import com.corhuila.parkkbus.application.usecase.RegisterVehicleEntryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking/entry")
public class ParkingEntryController {

    private final RegisterVehicleEntryUseCase registerVehicleEntryUseCase;

    public ParkingEntryController(RegisterVehicleEntryUseCase registerVehicleEntryUseCase) {
        this.registerVehicleEntryUseCase = registerVehicleEntryUseCase;
    }

    @PostMapping
    public ResponseEntity<ParkingSessionResponse> registerEntry(@RequestBody EntryRequest request) {
        ParkingSessionResponse response = registerVehicleEntryUseCase.execute(
                new RegisterVehicleEntryCommand(request.plate(), request.vehicleType())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public record EntryRequest(String plate, String vehicleType) {}
}
