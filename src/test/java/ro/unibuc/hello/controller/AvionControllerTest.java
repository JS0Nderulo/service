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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.InfoAvion;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.AvionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}