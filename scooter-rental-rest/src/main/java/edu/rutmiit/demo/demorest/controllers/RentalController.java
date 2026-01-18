package edu.rutmiit.demo.demorest.controllers;

import com.rutmiit.demo.api.dto.PagedResponse;
import com.rutmiit.demo.api.dto.RentalRequest;
import com.rutmiit.demo.api.dto.RentalResponse;
import com.rutmiit.demo.api.endpoints.RentalApi;
import edu.rutmiit.demo.demorest.assemblers.RentalModelAssembler;
import edu.rutmiit.demo.demorest.service.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RentalController implements RentalApi {

    private final RentalService rentalService;
    private final RentalModelAssembler rentalModelAssembler;
    private final PagedResourcesAssembler<RentalResponse> pagedResourcesAssembler;

    public RentalController(RentalService rentalService,
                            RentalModelAssembler rentalModelAssembler,
                            PagedResourcesAssembler<RentalResponse> pagedResourcesAssembler) {
        this.rentalService = rentalService;
        this.rentalModelAssembler = rentalModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<RentalResponse> getRentalById(Long id) {
        RentalResponse rental = rentalService.getRentalById(id);
        return rentalModelAssembler.toModel(rental);
    }

    @Override
    public PagedModel<EntityModel<RentalResponse>> getAllRentals(Long userId, int page, int size) {
        PagedResponse<RentalResponse> pagedResponse = rentalService.getAllRentals(userId, page, size);

        Page<RentalResponse> rentalPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(rentalPage, rentalModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<RentalResponse>> startRental(RentalRequest request) {
        RentalResponse rental = rentalService.startRental(request);
        EntityModel<RentalResponse> entityModel = rentalModelAssembler.toModel(rental);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<RentalResponse> finishRental(Long id) {
        RentalResponse rental = rentalService.finishRental(id);
        return rentalModelAssembler.toModel(rental);
    }
}