package uz.ecobin.ecobin.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import uz.ecobin.ecobin.model.User;
import uz.ecobin.ecobin.service.CommonService;

public class CommonServiceImpl implements CommonService {
    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
