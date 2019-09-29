package com.garciagiovane.dropbox.contract.v1;

import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public UserResponse getAllUsers(@PathVariable String id){
        return contractFacade.findById(id);
    }

    @GetMapping
    public Page<UserResponse> findAllUsers(Pageable pageable){
        return contractFacade.findAllUsers(pageable);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserRequest userRequest){
        return contractFacade.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable String id){
        return contractFacade.deleteUser(id);
    }
}
