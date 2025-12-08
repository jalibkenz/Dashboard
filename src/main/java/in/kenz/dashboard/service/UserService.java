package in.kenz.dashboard.service;

import in.kenz.dashboard.entity.User;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);
    User findByUserId(Long userId);
    User findByUserUsername(String userUsername);

}

