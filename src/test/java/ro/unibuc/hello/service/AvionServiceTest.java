package ro.unibuc.hello.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.data.AvionRepository;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.exception.DuplicateException;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.NullOrEmptyNumberException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AvionServiceTest {
    @Mock
    AvionRepository mockAvionRepository;
    @InjectMocks
    AvionService avionService = new AvionService();
    private static final String avionTemplate = "%s : %s -> %s";


    @Test
    void test_getAvionInfoByNumber_returnsInfoAvion(){
        // Arrange
        String number = "1";
        Avion avion = new Avion("1", "Doha", "Bangkok");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        when(mockAvionRepository.findByNumber(number)).thenReturn(avion);

        // Act
        InfoAvion resInfoAvion = avionService.getAvionInfoByNumber(number);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_getAvionInfoByNumber_whenAvionNotFound() {
        // Arrange
        String number = "1";

        when(mockAvionRepository.findByNumber(number)).thenReturn(null);

        try {
            // Act
            InfoAvion infoAvion = avionService.getAvionInfoByNumber(number);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 was not found");
        }
    }

    @Test
    void test_getAllAvioane() {
        // Arrange
        List<Avion> listAvioane=new ArrayList<Avion>();
        List<InfoAvion> listInfoAvion=new ArrayList<InfoAvion>();
        Avion avion1 = new Avion("1", "Doha", "Bangkok");
        Avion avion2 = new Avion("2", "Bangkok", "Tokyo");

        InfoAvion infoAvion1 = new InfoAvion(String.format(avionTemplate, avion1.number, avion1.from, avion1.to));
        InfoAvion infoAvion2 = new InfoAvion(String.format(avionTemplate, avion2.number, avion2.from, avion2.to));

        listAvioane.add(avion1);
        listAvioane.add(avion2);

        listInfoAvion.add(infoAvion1);
        listInfoAvion.add(infoAvion2);

        when(mockAvionRepository.findAll()).thenReturn(listAvioane);

            // Act
        List<InfoAvion> resListInfoAvioane = avionService.getAllAvioane();
        // Assert
        Assertions.assertEquals(listInfoAvion.get(0).getFlight(), resListInfoAvioane.get(0).getFlight());
        Assertions.assertEquals(listInfoAvion.get(1).getFlight(), resListInfoAvioane.get(1).getFlight());
        Assertions.assertEquals(listInfoAvion.size(),  resListInfoAvioane.size());
    }

    @Test
    void test_addAvion_returnsInfoAvion() throws Exception {
        // Arrange
        Avion avion = new Avion("1", "Doha", "Bangkok");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));
        when(mockAvionRepository.save(any())).thenReturn(avion);

        // Act
        InfoAvion resInfoAvion = avionService.addAvion(avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_addAvion_whenAvionNumberIsDuplicate() throws Exception {

        // Arrange
        Avion avion = new Avion("1","Doha","Bangkok");
        when(mockAvionRepository.findByNumber(any())).thenReturn(avion);

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
        Avion avion = new Avion("1", "Doha", "Bangkok");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));
        when(mockAvionRepository.findByNumber(any())).thenReturn(avion);

        // Act
        InfoAvion resInfoAvion = avionService.removeAvion(number);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_removeAvion_entityNotFound() throws Exception {
        // Arrange
        String number = "1";
        when(mockAvionRepository.findByNumber(number)).thenReturn(null);

        try {
            // Act
            InfoAvion infoAvion = avionService.removeAvion(number);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 was not found");
        }
    }

    @Test
    void test_updateAvion_returnsInfoAvion() throws Exception {
        // Arrange
        String number="1";
        Avion entity = new Avion("1", "Doha", "Bangkok");
        Avion avion = new Avion("2", "Singapore", "Brisbane");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        when(mockAvionRepository.findByNumber(number)).thenReturn(entity);

        when(mockAvionRepository.save(entity)).thenReturn(avion);

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
        Avion entity = new Avion("1", "Doha", "Bangkok");
        Avion avion = new Avion("2", null, "Paris");
        Avion result =new Avion("2", "Doha", "Paris");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, result.number, result.from, result.to));

        when(mockAvionRepository.findByNumber(number)).thenReturn(entity);

        when(mockAvionRepository.save(entity)).thenReturn(result);

        // Act
        InfoAvion resInfoAvion = avionService.updateAvion(number,avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }

    @Test
    void test_updateAvion_returnsInfoAvion_nullTo() throws Exception {
        // Arrange
        String number="1";
        Avion entity = new Avion("1", "Doha", "Bangkok");
        Avion avion = new Avion("2", "Colombo", null);
        Avion result =new Avion("2", "Colombo", "Bangkok");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, result.number, result.from, result.to));

        when(mockAvionRepository.findByNumber(number)).thenReturn(entity);

        when(mockAvionRepository.save(entity)).thenReturn(result);

        // Act
        InfoAvion resInfoAvion = avionService.updateAvion(number,avion);

        // Assert
        Assertions.assertEquals(infoAvion.getFlight(), resInfoAvion.getFlight());
    }
    @Test
    void test_updateAvion_entityNotFound() throws Exception {
        // Arrange
        String number="1";
        Avion avion = new Avion("2", "Singapore", "Brisbane");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        when(mockAvionRepository.findByNumber(number)).thenReturn(null);
        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), EntityNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 was not found");
        }
    }

    @Test
    void test_updateAvion_whenAvionNumberIsDuplicate() throws Exception {
        // Arrange
        String number="1";
        Avion avion = new Avion("1", "Singapore", "Brisbane");
        InfoAvion infoAvion = new InfoAvion(String.format(avionTemplate, avion.number, avion.from, avion.to));

        when(mockAvionRepository.findByNumber(any())).thenReturn(avion);

        try {
            // Act
            InfoAvion resInfoAvion = avionService.updateAvion(number,avion);
        } catch (Exception ex) {
            // Assert
            Assertions.assertEquals(ex.getClass(), DuplicateException.class);
            Assertions.assertEquals(ex.getMessage(), "Entity: 1 is duplicate!");
        }
    }

    @Test
    void test_fetchAvionByProperty() throws Exception {
        // Arrange
        List<Avion> listAvioane=new ArrayList<Avion>();
        List<InfoAvion> listInfoAvion=new ArrayList<InfoAvion>();

        Avion avion1 = new Avion("1", "Doha", "Bangkok");
        Avion avion2 = new Avion("3", "Doha", "Bucharest");

        InfoAvion infoAvion1 = new InfoAvion(String.format(avionTemplate, avion1.number, avion1.from, avion1.to));
        InfoAvion infoAvion2 = new InfoAvion(String.format(avionTemplate, avion2.number, avion2.from, avion2.to));

        listAvioane.add(avion1);
        listAvioane.add(avion2);

        listInfoAvion.add(infoAvion1);
        listInfoAvion.add(infoAvion2);
        when(mockAvionRepository.findAvionByProperties("Doha",null)).thenReturn(listAvioane);
        // Act
        List<InfoAvion> resInfoAvion = avionService.fetchAvionByProperty("Doha",null);

        // Assert
        Assertions.assertEquals(listInfoAvion.get(0).getFlight(), resInfoAvion.get(0).getFlight());
        Assertions.assertEquals(listInfoAvion.get(1).getFlight(), resInfoAvion.get(1).getFlight());
        Assertions.assertEquals(listInfoAvion.size(),  resInfoAvion.size());
    }

}
