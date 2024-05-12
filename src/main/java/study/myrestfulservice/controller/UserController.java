package study.myrestfulservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import study.myrestfulservice.bean.User;
import study.myrestfulservice.dao.UserDaoService;
import study.myrestfulservice.exception.UserNotFoundException;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDaoService service;

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUser(@PathVariable int id) {
        User findUser = service.findOne(id);
        if (findUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return ResponseEntity.ok(findUser);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {

        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        User deletedUser = service.deleteById(id);

        if(deletedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return ResponseEntity.noContent().build();
    }
}
