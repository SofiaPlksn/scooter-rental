package edu.rutmiit.demo.demorest.assemblers;

import com.rutmiit.demo.api.dto.ScooterResponse;
import edu.rutmiit.demo.demorest.controllers.RentalController;
import edu.rutmiit.demo.demorest.controllers.ScooterController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ScooterModelAssembler implements RepresentationModelAssembler<ScooterResponse, EntityModel<ScooterResponse>> {

    @Override
    public EntityModel<ScooterResponse> toModel(ScooterResponse scooter) {
        return EntityModel.of(scooter,
                linkTo(methodOn(ScooterController.class).getScooterById(scooter.getId())).withSelfRel(),
                linkTo(methodOn(ScooterController.class).getAllScooters(null, 0, 10)).withRel("collection"),
                linkTo(methodOn(RentalController.class).startRental(null)).withRel("rent")
        );
    }
}