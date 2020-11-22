package br.com.burnhop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackendApplicationTests {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
	@Test
	void contextLoads() {

	}
}
