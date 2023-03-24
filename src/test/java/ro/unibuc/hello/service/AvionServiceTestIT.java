package ro.unibuc.hello.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.data.AvionRepository;
import ro.unibuc.hello.dto.InfoAvion;

@SpringBootTest
//@Tag("IT")
public class AvionServiceTestIT {
    @Autowired
    AvionRepository avionRepository;

    @Autowired
    AvionService avionService;

    private static final String avionTemplate = "%s : %s -> %s";

    @BeforeEach
    public void setupDb() {
        avionRepository.deleteAll();
        Avion avion1 = new Avion("1", "Bucharest", "Honolulu");
        Avion avion2 = new Avion("2", "Doha", "Male");
        Avion avion3 = new Avion("3", "Viena", "Paris");
        Avion avion4 = new Avion("4", "Singapore", "Dubai");
        avionRepository.save(avion1);
        avionRepository.save(avion2);
        avionRepository.save(avion3);
        avionRepository.save(avion4);
    }

    @Test
    void test_getAvionInfoByNumber_returnsInfoAvion(){
        // Arrange
        String number = "2";

        // Act
        InfoAvion infoAvion = avionService.getAvionInfoByNumber(number);

        // Assert
        Assertions.assertEquals(String.format(avionTemplate, "2", "Doha", "Male"), infoAvion.getFlight());

    }

}
