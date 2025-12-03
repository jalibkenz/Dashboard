package in.kenz.dashboard.service;

import in.kenz.dashboard.entity.User;

public interface AuthService {
    User login(String username, String password);
}
