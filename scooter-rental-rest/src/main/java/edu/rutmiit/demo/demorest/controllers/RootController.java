package edu.rutmiit.demo.demorest.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
                linkTo(methodOn(ScooterController.class).getAllScooters(null, 0, 10)).withRel("scooters"),
                linkTo(methodOn(RentalController.class).getAllRentals(null, 0, 10)).withRel("rentals"),
                linkTo(methodOn(RootController.class).getApiDocumentation()).withRel("documentation")
        );
        return rootModel;
    }

    @GetMapping("/docs")
    public RepresentationModel<?> getApiDocumentation() {
        RepresentationModel<?> docsModel = new RepresentationModel<>();
        docsModel.add(
                linkTo(methodOn(RootController.class).getRoot()).withRel("api-root"),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users-api"),
                linkTo(methodOn(ScooterController.class).getAllScooters(null, 0, 10)).withRel("scooters-api"),
                linkTo(methodOn(RentalController.class).getAllRentals(null, 0, 10)).withRel("rentals-api")
        );
        return docsModel;
    }
}