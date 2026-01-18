package edu.rutmiit.demo.demorest.assemblers;

import com.rutmiit.demo.api.dto.UserResponse;
import edu.rutmiit.demo.demorest.controllers.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<UserResponse>> toCollectionModel(Iterable<? extends UserResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }
}