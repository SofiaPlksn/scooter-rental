package edu.rutmiit.demo.demorest.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.rutmiit.demo.api.dto.UserRequest;
import com.rutmiit.demo.api.dto.UserResponse;
import edu.rutmiit.demo.demorest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;

    @Autowired
    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public List<UserResponse> users() {
        return userService.getAllUsers();
    }

    @DgsQuery
    public UserResponse userById(@InputArgument Long id) {
        return userService.getUserById(id);
    }

    @DgsMutation
    public UserResponse createUser(@InputArgument("input") Map<String, String> input) {
        UserRequest request = new UserRequest(input.get("fullName"), input.get("phone"));
        return userService.createUser(request);
    }

    @DgsMutation
    public UserResponse updateUser(@InputArgument Long id, @InputArgument("input") Map<String, String> input) {
        UserRequest request = new UserRequest(input.get("fullName"), input.get("phone"));
        return userService.updateUser(id, request);
    }

    @DgsMutation
    public Long deleteUser(@InputArgument Long id) {
        userService.deleteUser(id);
        return id;
    }
}