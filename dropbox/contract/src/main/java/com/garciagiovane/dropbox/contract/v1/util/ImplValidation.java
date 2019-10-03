package com.garciagiovane.dropbox.contract.v1.util;

import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.impl.v1.user.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ImplValidation {
    public static void validateUserFields(UserRequest user) {
        List<Exception> exceptions = new ArrayList<>();

        if (ObjectUtils.isEmpty(user.getName()))
            exceptions.add(new IllegalArgumentException("name must not be null"));
        if (ObjectUtils.isEmpty(user.getEmail()))
            exceptions.add(new IllegalArgumentException("email must not be null"));

        if (!exceptions.isEmpty())
            throw new ApiException(exceptions, HttpStatus.BAD_REQUEST);
    }
}
