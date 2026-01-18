package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.rutmiit.demo.api.dto.*;
import edu.rutmiit.demo.demorest.service.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@DgsComponent
public class ScooterDataFetcher {

    private final ScooterService scooterService;

    @Autowired
    public ScooterDataFetcher(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @DgsQuery
    public PagedResponse<ScooterResponse> scooters(@InputArgument Boolean available,
                                                   @InputArgument Integer page,
                                                   @InputArgument Integer size) {
        int actualPage = page != null ? page : 0;
        int actualSize = size != null ? size : 10;
        return scooterService.getAllScooters(available, actualPage, actualSize);
    }

    @DgsQuery
    public ScooterResponse scooterById(@InputArgument Long id) {
        return scooterService.getScooterById(id);
    }

    @DgsMutation
    public ScooterResponse createScooter(@InputArgument("input") Map<String, String> input) {
        ScooterRequest request = new ScooterRequest(input.get("model"), input.get("serialNumber"));
        return scooterService.createScooter(request);
    }

    @DgsMutation
    public ScooterResponse updateScooter(@InputArgument Long id, @InputArgument("input") Map<String, String> input) {
        UpdateScooterRequest request = new UpdateScooterRequest(input.get("model"), input.get("serialNumber"));
        return scooterService.updateScooter(id, request);
    }

    @DgsMutation
    public Long deleteScooter(@InputArgument Long id) {
        scooterService.deleteScooter(id);
        return id;
    }
}