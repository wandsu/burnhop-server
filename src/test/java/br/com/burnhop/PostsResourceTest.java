package br.com.burnhop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class PostsResourceTest{
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /*
    @Test
    @Order(1)
    void testGetAllPostsWithPosts() throws Exception{
        mockMvc.perform(get("/posts/get-all"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    void testPostingRequest() throws Exception{
        String texto = "Posting";
        String user_email = "test1@test.com";
        
        CreatedPostDto createdPostDto = new CreatedPostDto();

        createdPostDto.setTexto(texto);
        createdPostDto.setUser_email(user_email);

		mockMvc.perform(post("/posts")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(createdPostDto)))
				.andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void testGetAllPosts() throws Exception{
        mockMvc.perform(get("/posts/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    */
    
}