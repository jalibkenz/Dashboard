package in.kenz.dashboard.service.impl;

import in.kenz.dashboard.dao.UserDao;
import in.kenz.dashboard.dao.impl.UserDaoImpl;
import in.kenz.dashboard.entity.User;
import in.kenz.dashboard.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public void saveUser(User user) {
        userDao.save(user);
    }

    @Override
    public User findByUserId(Long userId) {
        return userDao.findByUserId(userId);
    }

    @Override
    public User findByUserUsername(String userUsername) {
        return userDao.findByUserUsername(userUsername);
    }
}
