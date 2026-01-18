package com.rutmiit.demo.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse extends RepresentationModel<UserResponse> {
    private final Long id;
    private final String fullName;
    private final String phone;

    public UserResponse(Long id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, fullName, phone);
    }
}