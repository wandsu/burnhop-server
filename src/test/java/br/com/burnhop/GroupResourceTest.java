package br.com.burnhop;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.burnhop.model.dto.CreatedGroupDto;
import br.com.burnhop.model.dto.AssociatedUserGroupDto;
import br.com.burnhop.model.dto.GroupDto;
import br.com.burnhop.model.Login;
import br.com.burnhop.model.Users;
import br.com.burnhop.model.Groups;
import br.com.burnhop.repository.LoginRepository;
import br.com.burnhop.repository.UsersRepository;
import br.com.burnhop.repository.GroupsRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GroupResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    private CreatedGroupDto makeGroup(int id, String name){

        CreatedGroupDto createdGroupDto = new CreatedGroupDto();

        createdGroupDto.setName(name);
        createdGroupDto.setAdmin(id);

        return createdGroupDto;
    }

    private void saveUser(String name){
        Date data_nasc = Date.valueOf("2000-01-01");
        Timestamp created_on = new Timestamp(System.currentTimeMillis());

        Login login = new Login(name+"@email.com", "12345");
        Users newUser = new Users(name, name, data_nasc, created_on);
        newUser.setLogin(login);

        loginRepository.save(newUser.getLogin());
        usersRepository.save(newUser);
    }

    private int getUserId(String email){
        Users user = usersRepository.findByEmail(email);
        return user.getId();
    }

    @Test
    void testCreateGroup() throws Exception{
        saveUser("groups");
        int id = getUserId("groups@email.com");

        CreatedGroupDto createdGroupDto = makeGroup(id, "New Group");

        mockMvc.perform(post("/groups")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createdGroupDto)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/groups")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createdGroupDto)))
                .andExpect(status().isConflict());
    }
	
	@Test
	void testAssociateUser() throws Exception{
		saveUser("adminassociateuser");
		Users admin = usersRepository.findByEmail("adminassociateuser@email.com");

		saveUser("associateuser");
		int id = getUserId("associateuser@email.com");

		Groups group = new Groups("AssociateUser", "Descrição", new Timestamp(System.currentTimeMillis()));
		group.setAdmin(admin);
		groupsRepository.save(group);
        Optional<Groups> groups = groupsRepository.findByName("AssociateUser");
        GroupDto group_dto = groups.map(GroupDto::new).orElse(null);
		int id_group = group_dto.getId();

		AssociatedUserGroupDto associatedUserGroupDto = new AssociatedUserGroupDto();
		associatedUserGroupDto.setGroupId(id_group);
		associatedUserGroupDto.setUserId(id);

		mockMvc.perform(post("/groups/user")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(associatedUserGroupDto)))
                .andExpect(status().isOk());
                
        mockMvc.perform(post("/groups/user")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(associatedUserGroupDto)))
                .andExpect(status().isConflict());

        associatedUserGroupDto.setUserId(1111);

        mockMvc.perform(post("/groups/user")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(associatedUserGroupDto)))
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    void testGetAllGroups() throws Exception{
        saveUser("getallgroups");
		Users admin = usersRepository.findByEmail("adminassociateuser@email.com");

		Groups group = new Groups("GetAllGroups", "Descrição", new Timestamp(System.currentTimeMillis()));
		group.setAdmin(admin);
        groupsRepository.save(group);
        
        mockMvc.perform(get("/groups/get-all"))
                .andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
	void testGetGroupById() throws Exception{
		saveUser("getgroupbyid");
        Users admin = usersRepository.findByEmail("getgroupbyid@email.com");
        
		Groups group = new Groups("GetGroupById", "Descrição", new Timestamp(System.currentTimeMillis()));
		group.setAdmin(admin);
		groupsRepository.save(group);
        Optional<Groups> groups = groupsRepository.findByName("GetGroupById");
        GroupDto group_dto = groups.map(GroupDto::new).orElse(null);
        int id_group = group_dto.getId();
        int notFoundId = 1111;

        mockMvc.perform(get("/groups/id/{id}", id_group))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        mockMvc.perform(get("/groups/id/{id}", notFoundId))
                        .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteGroup() throws Exception{
        saveUser("deleteGroup");
        Users admin = usersRepository.findByEmail("deleteGroup@email.com");
        
		Groups group = new Groups("DeleteGroup", "Descrição", new Timestamp(System.currentTimeMillis()));
		group.setAdmin(admin);
		groupsRepository.save(group);
        Optional<Groups> groups = groupsRepository.findByName("DeleteGroup");
        GroupDto group_dto = groups.map(GroupDto::new).orElse(null);
        int id_group = group_dto.getId();
        int notFoundId = 1111;

        mockMvc.perform(delete("/groups/{id}", String.valueOf(id_group)))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/groups/{id}", String.valueOf(notFoundId)))
				.andExpect(status().isNotFound());
    }
}