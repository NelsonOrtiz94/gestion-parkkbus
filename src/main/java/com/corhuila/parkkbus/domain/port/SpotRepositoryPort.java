package com.corhuila.parkkbus.domain.port;

import com.corhuila.parkkbus.domain.model.Spot;
import java.util.List;

public interface SpotRepositoryPort {
    List<Spot> findAvailableSpots();
}