package edu.rutmiit.demo.demorest.controllers;

import com.rutmiit.demo.api.dto.*;
import com.rutmiit.demo.api.endpoints.ScooterApi;
import edu.rutmiit.demo.demorest.assemblers.ScooterModelAssembler;
import edu.rutmiit.demo.demorest.service.ScooterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScooterController implements ScooterApi {

    private final ScooterService scooterService;
    private final ScooterModelAssembler scooterModelAssembler;
    private final PagedResourcesAssembler<ScooterResponse> pagedResourcesAssembler;

    public ScooterController(ScooterService scooterService,
                             ScooterModelAssembler scooterModelAssembler,
                             PagedResourcesAssembler<ScooterResponse> pagedResourcesAssembler) {
        this.scooterService = scooterService;
        this.scooterModelAssembler = scooterModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<ScooterResponse> getScooterById(Long id) {
        ScooterResponse scooter = scooterService.getScooterById(id);
        return scooterModelAssembler.toModel(scooter);
    }

    @Override
    public PagedModel<EntityModel<ScooterResponse>> getAllScooters(Boolean available, int page, int size) {
        PagedResponse<ScooterResponse> pagedResponse = scooterService.getAllScooters(available, page, size);

        Page<ScooterResponse> scooterPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(scooterPage, scooterModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<ScooterResponse>> createScooter(ScooterRequest request) {
        ScooterResponse createdScooter = scooterService.createScooter(request);
        EntityModel<ScooterResponse> entityModel = scooterModelAssembler.toModel(createdScooter);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<ScooterResponse> updateScooter(Long id, UpdateScooterRequest request) {
        ScooterResponse updatedScooter = scooterService.updateScooter(id, request);
        return scooterModelAssembler.toModel(updatedScooter);
    }

    @Override
    public void deleteScooter(Long id) {
        scooterService.deleteScooter(id);
    }
}