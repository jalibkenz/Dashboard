package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.User;

public interface UserDao {
    void save(User user);
    User findByUserId(Long userId);
    User findByUserName(String userName);
    User findByUserUsername(String userUsername);
    User findByUserRoleName(String userRoleName);
    void deleteByUserId(Long userId);
}
