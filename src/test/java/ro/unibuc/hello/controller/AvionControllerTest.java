package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.data.Avion;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.exception.DuplicateException;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.AvionService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class AvionControllerTest {

    @Mock
    private AvionService avionService;

    @InjectMocks
    private AvionController avionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(avionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void test_getAvion() throws Exception {
        // Arrange
        InfoAvion infoAvion = new InfoAvion("1: Doha -> Bucharest");

        when(avionService.getAvionInfoByNumber(any())).thenReturn(infoAvion);

        // Act
        MvcResult result = mockMvc.perform(get("/avion/1")
                        .content(objectMapper.writeValueAsString(infoAvion))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvion));
    }
    

    @Test
    void test_getAvion_entityNotFound() throws Exception {

        // Arrange
        String number = "100";
        when(avionService.getAvionInfoByNumber(any())).thenThrow(new EntityNotFoundException(number));

        MvcResult result = mockMvc.perform(get("/avion/100")
                        .content("Avion entity with the requested number not found.")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), "Avion entity with the requested number not found.");
    }

    @Test
    void test_getAllAvioane_oneFlight() throws Exception {
        // Arrange
        InfoAvion infoAvion = new InfoAvion("1: Doha -> Bucharest");
        List<InfoAvion> infoAvionList_oneFlight= new ArrayList<InfoAvion>();
        infoAvionList_oneFlight.add(infoAvion);

        when(avionService.getAllAvioane()).thenReturn(infoAvionList_oneFlight);

        // Act
        MvcResult result = mockMvc.perform(get("/avion")
                        .content(objectMapper.writeValueAsString(infoAvionList_oneFlight))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvionList_oneFlight));
    }

    @Test
    void test_getAllAvioane_multipleFlights() throws Exception {
        // Arrange
        InfoAvion infoAvion1 = new InfoAvion("1: Doha -> Bucharest");
        InfoAvion infoAvion2 = new InfoAvion("2: Viena -> Paris");
        InfoAvion infoAvion3 = new InfoAvion("3: Male -> Buenos Aires");

        List<InfoAvion> infoAvionList_multipleFlights= new ArrayList<InfoAvion>();
        infoAvionList_multipleFlights.add(infoAvion1);
        infoAvionList_multipleFlights.add(infoAvion2);
        infoAvionList_multipleFlights.add(infoAvion3);

        when(avionService.getAllAvioane()).thenReturn(infoAvionList_multipleFlights);

        // Act
        MvcResult result = mockMvc.perform(get("/avion")
                        .content(objectMapper.writeValueAsString(infoAvionList_multipleFlights))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvionList_multipleFlights));
    }

    @Test
    void test_getAllAvioane_noFlights() throws Exception {
        // Arrange

        List<InfoAvion> infoAvionList_noFlights= new ArrayList<InfoAvion>();

        when(avionService.getAllAvioane()).thenReturn(infoAvionList_noFlights);

        // Act
        MvcResult result = mockMvc.perform(get("/avion")
                        .content(objectMapper.writeValueAsString(infoAvionList_noFlights))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvionList_noFlights));
    }

    @Test
    void test_addAvion() throws Exception {
        // Arrange
        Avion avion = new Avion("1","Doha","Bangkok");
        InfoAvion infoAvion= new InfoAvion("1: Doha -> Bangkok");
        when(avionService.addAvion(any())).thenReturn(infoAvion);

        // Act
        MvcResult result = mockMvc.perform(post("/avion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avion))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flight").value(infoAvion.getFlight()))
                .andExpect(status().isOk())
                .andReturn();
        
        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvion));
    }

    @Test
    void test_addAvion_duplicate() throws Exception {

        // Arrange
        Avion avion = new Avion("1","Doha","Bangkok");
        String duplicateExceptionMessage = "An avion entity with the same number already exists so the state of the DB wasn't modified.";

        when(avionService.addAvion(any())).thenThrow(new DuplicateException(avion.getNumber()));

        // Act
        MvcResult result = mockMvc.perform(post("/avion")
                        .content(objectMapper.writeValueAsString(avion))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avion)))
                .andExpect(status().isOk())
                .andReturn();


        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), duplicateExceptionMessage);

    }

    @Test
    void test_deleteAvion() throws Exception {
        // Arrange
        Avion avion = new Avion("1","Doha","Bangkok");
        InfoAvion infoAvion= new InfoAvion("1: Doha -> Bangkok");
        when(avionService.removeAvion(any())).thenReturn(infoAvion);

        // Act
        MvcResult result = mockMvc.perform(delete("/avion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avion))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flight").value(infoAvion.getFlight()))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(infoAvion));
    }

    @Test
    void test_deleteAvion_entityNotFound() throws Exception {
        // Arrange
        String number = "100";
        String entityNotFoundMessage = "Avion entity with the requested number was not found so the state of the DB wasn't modified.";

        when(avionService.removeAvion(any())).thenThrow(new EntityNotFoundException(number));

        // Act
        MvcResult result = mockMvc.perform(delete("/avion/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Assertions.assertEquals(result.getResponse().getContentAsString(), entityNotFoundMessage);
    }

}