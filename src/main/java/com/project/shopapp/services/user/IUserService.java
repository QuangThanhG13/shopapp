package com.project.shopapp.services.user;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.User;
import org.apache.kafka.common.protocol.types.Field;


public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;

   String login(String phoneNumber, String password) throws Exception; //Sau nay cha ve token key
}
