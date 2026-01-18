package edu.rutmiit.demo.demorest.assemblers;

import com.rutmiit.demo.api.dto.RentalResponse;
import edu.rutmiit.demo.demorest.controllers.RentalController;
import edu.rutmiit.demo.demorest.controllers.ScooterController;
import edu.rutmiit.demo.demorest.controllers.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RentalModelAssembler implements RepresentationModelAssembler<RentalResponse, EntityModel<RentalResponse>> {

    @Override
    public EntityModel<RentalResponse> toModel(RentalResponse rental) {
        EntityModel<RentalResponse> entityModel = EntityModel.of(rental,
                linkTo(methodOn(RentalController.class).getRentalById(rental.getId())).withSelfRel(),
                linkTo(methodOn(RentalController.class).getAllRentals(null, 0, 10)).withRel("collection"),
                linkTo(methodOn(UserController.class).getUserById(rental.getUser().getId())).withRel("user"),
                linkTo(methodOn(ScooterController.class).getScooterById(rental.getScooter().getId())).withRel("scooter")
        );

        // Добавляем ссылку для завершения аренды, если она еще не завершена
        if (rental.getEndTime() == null) {
            entityModel.add(linkTo(methodOn(RentalController.class).finishRental(rental.getId())).withRel("finish"));
        }

        return entityModel;
    }
}