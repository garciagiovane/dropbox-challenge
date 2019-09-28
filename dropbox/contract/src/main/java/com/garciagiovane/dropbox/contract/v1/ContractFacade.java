package com.garciagiovane.dropbox.contract.v1;

import com.garciagiovane.dropbox.contract.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import com.garciagiovane.dropbox.impl.v1.ImplFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContractFacade {

    private ImplFacade implFacade;

    public UserResponse create(UserRequest userRequest){
        return UserMapper.mapToContract(implFacade.create(UserMapper.mapToImpl(userRequest)));
    }

}
