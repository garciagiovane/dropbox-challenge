package com.garciagiovane.dropbox.contract.v1;

import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/users")
@RestController
@AllArgsConstructor
public class Controller {

    private ContractFacade contractFacade;

    @PostMapping
    public UserResponse create(@RequestBody UserRequest userRequest){
        return contractFacade.create(userRequest);
    }
}
