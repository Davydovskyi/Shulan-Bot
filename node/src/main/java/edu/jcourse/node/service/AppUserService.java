package edu.jcourse.node.service;

import edu.jcourse.jpa.entity.AppUser;

public interface AppUserService {
    String registerUser(AppUser appUser);

    String setEmail(AppUser appUser, String email);
}