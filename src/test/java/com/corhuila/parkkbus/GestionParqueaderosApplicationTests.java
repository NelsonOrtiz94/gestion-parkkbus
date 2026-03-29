package com.corhuila.parkkbus;

import com.corhuila.parkkbus.domain.port.ParkingSessionRepositoryPort;
import com.corhuila.parkkbus.domain.port.PaymentRepositoryPort;
import com.corhuila.parkkbus.domain.port.SpotRepositoryPort;
import com.corhuila.parkkbus.domain.port.TariffRepositoryPort;
import com.corhuila.parkkbus.domain.port.TokenServicePort;
import com.corhuila.parkkbus.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class GestionParqueaderosApplicationTests {

    @MockBean UserRepositoryPort userRepositoryPort;
    @MockBean TokenServicePort tokenServicePort;
    @MockBean SpotRepositoryPort spotRepositoryPort;
    @MockBean ParkingSessionRepositoryPort parkingSessionRepositoryPort;
    @MockBean PaymentRepositoryPort paymentRepositoryPort;
    @MockBean TariffRepositoryPort tariffRepositoryPort;

    @Test
    void contextLoads() {
    }

}
