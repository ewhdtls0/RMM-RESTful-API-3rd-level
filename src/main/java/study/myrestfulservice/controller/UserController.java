package study.myrestfulservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import study.myrestfulservice.bean.User;
import study.myrestfulservice.dao.UserDaoService;
import study.myrestfulservice.exception.UserNotFoundException;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러입니다.")
public class UserController {
    private final UserDaoService service;

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return service.findAll();
    }

    @Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 이용해서 사용자 상세 정보 조회를 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "USER NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!"),
    })
    @GetMapping("/users/{id}")
    public EntityModel<User> findUser(@Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable int id) {
        User findUser = service.findOne(id);
        if (findUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        EntityModel entityModel = EntityModel.of(findUser);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return entityModel;
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
