package ro.unibuc.hello.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.data.AvionRepository;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.exception.DuplicateException;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.NullOrEmptyNumberException;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void test_getAvionInfoByNumber_whenAvionNotFound() {
        // Arrange
        String number = "10";

        try {
            // Act
            InfoAvion infoAvion = avionService.getAvionInfoByNumber(number);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 10 was not found");
        }
    }

    @Test
    void test_getAllAvioane() {
        // Arrange
        List<InfoAvion> listInfoAvion=new ArrayList<InfoAvion>();

        InfoAvion infoAvion1 = new InfoAvion(String.format(avionTemplate, "1", "Bucharest", "Honolulu"));
        InfoAvion infoAvion2 = new InfoAvion(String.format(avionTemplate, "2", "Doha", "Male"));


        listInfoAvion.add(infoAvion1);
        listInfoAvion.add(infoAvion2);

        // Act
        List<InfoAvion> resListInfoAvioane = avionService.getAllAvioane();
        // Assert
        Assertions.assertEquals(listInfoAvion.get(0).getFlight(), resListInfoAvioane.get(0).getFlight());
        Assertions.assertEquals(listInfoAvion.get(1).getFlight(), resListInfoAvioane.get(1).getFlight());
    }

    @Test
    void test_addAvion_returnsInfoAvion() throws Exception {
        // Arrange
        Avion avion = new Avion("5", "Copenhaga", "New Delhi");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        // Act
        InfoAvion resInfoAvion = avionService.addAvion(avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_addAvion_whenAvionNumberIsDuplicate() throws Exception {
        // Arrange
        Avion avion = new Avion("1","Copenhaga", "New Delhi");

        try {
            // Act
            InfoAvion resInfoAvion = avionService.addAvion(avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), DuplicateException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 is duplicate!");
        }
    }


    @Test
    void test_addAvion_whenAvionNumberIsNull() throws Exception {
        // Arrange
        Avion avion = new Avion(null,"Doha","Bangkok");

        try {
            // Act
            InfoAvion resInfoAvion = avionService.addAvion(avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), NullOrEmptyNumberException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: the provided number is null or empty!");
        }
    }

    @Test
    void test_addAvion_whenAvionNumberIsEmpty() throws Exception {
        // Arrange
        Avion avion = new Avion("","Doha","Bangkok");

        try {
            // Act
            InfoAvion resInfoAvion = avionService.addAvion(avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), NullOrEmptyNumberException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: the provided number is null or empty!");
        }
    }

    @Test
    void test_removeAvion_returnsInfoAvion() throws Exception {
        // Arrange
        String number = "1";
        Avion avion = new Avion("1", "Bucharest", "Honolulu");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        // Act
        InfoAvion resInfoAvion = avionService.removeAvion(number);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_removeAvion_entityNotFound() throws Exception {
        // Arrange
        String number = "11";

        try {
            // Act
            InfoAvion infoAvion = avionService.removeAvion(number);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 11 was not found");
        }
    }
    @Test
    void test_updateAvion_returnsInfoAvion() throws Exception {
        // Arrange
        String number="1";
        Avion entity = new Avion("1", "Bucharest", "Honolulu");
        Avion avion = new Avion("9", "Singapore", "Brisbane");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        // Act
        InfoAvion resInfoAvion = avionService.updateAvion(number,avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_updateAvion_exception_nullNumber() throws Exception {
        // Arrange
        String number="1";
        Avion avion = new Avion(null, "Dubai", "Jeddah");

        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), NullOrEmptyNumberException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: the provided number is null or empty!");
        }
    }

    @Test
    void test_updateAvion_exception_emptyNumber() throws Exception {
        // Arrange
        String number="1";
        Avion avion = new Avion("", "Dubai", "Jeddah");

        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), NullOrEmptyNumberException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: the provided number is null or empty!");
        }
    }

    @Test
    void test_updateAvion_returnsInfoAvion_nullFrom() throws Exception {
        // Arrange
        String number="1";
        Avion entity = new Avion("1", "Bucharest", "Honolulu");
        Avion avion = new Avion("9", null, "Paris");
        Avion result =new Avion("9", "Bucharest", "Paris");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, result.number, result.from, result.to));


        // Act
        InfoAvion resInfoAvion = avionService.updateAvion(number,avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_updateAvion_returnsInfoAvion_nullTo() throws Exception {
        // Arrange
        String number="1";
        Avion entity = new Avion("1", "Bucharest", "Honolulu");
        Avion avion = new Avion("9", "Colombo", null);
        Avion result =new Avion("9", "Colombo", "Honolulu");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, result.number, result.from, result.to));


        // Act
        InfoAvion resInfoAvion = avionService.updateAvion(number,avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }
    @Test
    void test_updateAvion_entityNotFound() throws Exception {
        // Arrange
        String number="11";
        Avion avion = new Avion("11", "Singapore", "Brisbane");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 11 was not found");
        }
    }

    @Test
    void test_updateAvion_whenAvionNumberIsDuplicate() throws Exception {
        // Arrange
        String number="1";
        Avion avion = new Avion("1", "Bucharest", "Honolulu");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), DuplicateException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 is duplicate!");
        }
    }
}
