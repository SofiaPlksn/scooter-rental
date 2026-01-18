package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.rutmiit.demo.api.dto.RentalRequest;
import com.rutmiit.demo.api.dto.RentalResponse;
import com.rutmiit.demo.api.dto.PagedResponse;
import edu.rutmiit.demo.demorest.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@DgsComponent
public class RentalDataFetcher {

    private final RentalService rentalService;

    @Autowired
    public RentalDataFetcher(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @DgsQuery
    public PagedResponse<RentalResponse> rentals(@InputArgument Long userId,
                                                 @InputArgument Integer page,
                                                 @InputArgument Integer size) {
        int actualPage = page != null ? page : 0;
        int actualSize = size != null ? size : 10;
        return rentalService.getAllRentals(userId, actualPage, actualSize);
    }

    @DgsQuery
    public RentalResponse rentalById(@InputArgument Long id) {
        return rentalService.getRentalById(id);
    }

    @DgsMutation
    public RentalResponse startRental(@InputArgument("input") Map<String, Object> input) {
        Long userId = Long.parseLong(input.get("userId").toString());
        Long scooterId = Long.parseLong(input.get("scooterId").toString());
        RentalRequest request = new RentalRequest(userId, scooterId);
        return rentalService.startRental(request);
    }

    @DgsMutation
    public RentalResponse finishRental(@InputArgument Long id) {
        return rentalService.finishRental(id);
    }
}