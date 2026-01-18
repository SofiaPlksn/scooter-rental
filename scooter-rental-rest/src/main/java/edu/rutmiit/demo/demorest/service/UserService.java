package edu.rutmiit.demo.demorest.service;

import com.rutmiit.demo.api.dto.UserRequest;
import com.rutmiit.demo.api.dto.UserResponse;
import com.rutmiit.demo.api.exception.ResourceNotFoundException;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final InMemoryStorage storage;

    public UserService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public List<UserResponse> getAllUsers() {
        return new ArrayList<>(storage.users.values());
    }

    public UserResponse getUserById(Long id) {
        UserResponse user = storage.users.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("User", id);
        }
        return user;
    }

    public UserResponse createUser(UserRequest request) {
        long id = storage.userSequence.incrementAndGet();
        UserResponse newUser = new UserResponse(id, request.fullName(), request.phone());
        storage.users.put(id, newUser);
        return newUser;
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        if (!storage.users.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        UserResponse updated = new UserResponse(id, request.fullName(), request.phone());
        storage.users.put(id, updated);
        return updated;
    }

    public void deleteUser(Long id) {
        if (storage.users.remove(id) == null) {
            throw new ResourceNotFoundException("User", id);
        }
    }
}