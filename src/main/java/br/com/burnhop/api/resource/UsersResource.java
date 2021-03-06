package br.com.burnhop.api.resource;

import br.com.burnhop.model.dto.CreatedUserDto;
import br.com.burnhop.model.dto.UpdatedUserDto;
import br.com.burnhop.model.dto.UserDto;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import br.com.burnhop.repository.PostsRepository;
import br.com.burnhop.repository.UsersRepository;
import br.com.burnhop.repository.LoginRepository;
import br.com.burnhop.api.controller.UserController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController()
@CrossOrigin("*")
@RequestMapping("/users")
public class UsersResource {

    private UserController userController;

    public UsersResource(LoginRepository login_repository, UsersRepository user_repository, PostsRepository posts_repository){
        userController = new UserController(login_repository, user_repository, posts_repository);
    }

    @PostMapping()
    @ApiOperation(value = "Criar um novo usuário", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário cadastrado com sucesso"),
            @ApiResponse(code = 409, message = "Usuário com este e-mail já está cadastrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<UserDto> createUser(@RequestBody CreatedUserDto newUser) throws NoSuchAlgorithmException {

        try {
            UserDto user = userController.createUser(newUser.toUser());
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @ApiOperation(value = "Autenticação", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário autenticado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não autenticado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<String> login(
            @RequestHeader(value = "email") String email,
            @RequestHeader(value = "password") String password) throws NoSuchAlgorithmException {

        try {
            boolean authenticate = userController.authenticateUser(email, password);

            if (authenticate)
                return new ResponseEntity<>("Autenticado\n", HttpStatus.OK);

            return new ResponseEntity<>("Não autenticado\n", HttpStatus.UNAUTHORIZED);

        } catch (IllegalAccessError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email/{email}")
    @ApiOperation(value = "Retorna usuário baseado no e-mail")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário com e-mail"),
            @ApiResponse(code = 404, message = "Usuário com e-mail não encontrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable(value = "email") String email) {

        try {
            UserDto user = userController.getUserByEmail(email);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{id}")
    @ApiOperation(value = "Retorna usuário baseado no id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário com id"),
            @ApiResponse(code = 404, message = "Usuário com id não encontrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<UserDto> getUserById(
            @PathVariable(value = "id") int id) {

        try {
            UserDto user = userController.getUserById(id);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    @ApiOperation(value = "Retorna todos os usuários")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de todos os usuários"),
            @ApiResponse(code = 404, message = "Nenhum usuário foi encontrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<ArrayList<UserDto>> getAllUsers() {

        try {
            ArrayList<UserDto> users = userController.getAllUsers();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "Retorna Usuário atualizado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário atualizado"),
            @ApiResponse(code = 404, message = "Nenhum usuário foi encontrado"),
            @ApiResponse(code = 409, message = "Usuário com este e-mail já está cadastrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UpdatedUserDto userToUpdate,
            @PathVariable(value = "id") int id) {

        try {
            UserDto user = userController.getUserById(id);

            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserDto updatedUser = userController.updateUser(id, user, userToUpdate);

            if(updatedUser == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/image")
    @ApiOperation(value = "Retorna Usuário atualizado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário atualizado"),
            @ApiResponse(code = 404, message = "Nenhum usuário foi encontrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<UserDto> updateUser(
            @RequestParam String imagePath,
            @RequestParam int id) {

        try {
            UserDto user = userController.getUserById(id);

            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            UserDto updatedUser = userController.updateImagePath(id, imagePath);

            if(updatedUser == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping()
    @ApiOperation(value = "Delete usuário informado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário deletado com sucesso"),
            @ApiResponse(code = 404, message = "Nenhum usuário foi encontrado"),
            @ApiResponse(code = 500, message = "Ocorreu um erro para processar a requisição")
    })
    public ResponseEntity<String> deleteUser(
            @RequestParam int id) {

        try {
            UserDto user = userController.getUserById(id);

            if(user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            boolean deleted = userController.deleteUser(id);

            if(deleted)
                return new ResponseEntity<>("Usuário deletado com sucesso", HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
