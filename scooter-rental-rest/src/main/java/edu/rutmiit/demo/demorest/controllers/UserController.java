package edu.rutmiit.demo.demorest.controllers;

import com.rutmiit.demo.api.dto.UserRequest;
import com.rutmiit.demo.api.dto.UserResponse;
import com.rutmiit.demo.api.endpoints.UserApi;
import edu.rutmiit.demo.demorest.assemblers.UserModelAssembler;
import edu.rutmiit.demo.demorest.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> getAllUsers() {
        return userModelAssembler.toCollectionModel(userService.getAllUsers());
    }

    @Override
    public EntityModel<UserResponse> getUserById(Long id) {
        UserResponse user = userService.getUserById(id);
        return userModelAssembler.toModel(user);
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> createUser(UserRequest request) {
        UserResponse createdUser = userService.createUser(request);
        EntityModel<UserResponse> entityModel = userModelAssembler.toModel(createdUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<UserResponse> updateUser(Long id, UserRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return userModelAssembler.toModel(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}