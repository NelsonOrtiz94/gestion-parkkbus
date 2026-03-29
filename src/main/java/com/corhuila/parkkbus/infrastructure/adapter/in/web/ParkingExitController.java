package com.corhuila.parkkbus.infrastructure.adapter.in.web;

import com.corhuila.parkkbus.application.command.RegisterVehicleExitCommand;
import com.corhuila.parkkbus.application.dto.VehicleExitResponse;
import com.corhuila.parkkbus.application.usecase.RegisterVehicleExitUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking/exit")
public class ParkingExitController {

    private final RegisterVehicleExitUseCase registerVehicleExitUseCase;

    public ParkingExitController(RegisterVehicleExitUseCase registerVehicleExitUseCase) {
        this.registerVehicleExitUseCase = registerVehicleExitUseCase;
    }

    @PostMapping
    public ResponseEntity<VehicleExitResponse> registerExit(@RequestBody ExitRequest request) {
        VehicleExitResponse response = registerVehicleExitUseCase.execute(
                new RegisterVehicleExitCommand(request.plate())
        );
        return ResponseEntity.ok(response);
    }

    public record ExitRequest(String plate) {}
}
