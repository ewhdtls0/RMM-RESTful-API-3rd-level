package study.myrestfulservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import study.myrestfulservice.bean.User;
import study.myrestfulservice.dao.UserDaoService;

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
    public User findUser(@PathVariable int id) {
        return service.findOne(id);
    }
}
