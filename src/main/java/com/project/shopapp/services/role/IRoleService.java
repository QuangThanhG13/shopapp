package com.project.shopapp.services.role;

import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IRoleService {
    List<Role> getAllRoles();
}
