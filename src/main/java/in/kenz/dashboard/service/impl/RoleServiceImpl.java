package in.kenz.dashboard.service.impl;

import in.kenz.dashboard.dao.RoleDao;
import in.kenz.dashboard.dao.impl.RoleDaoImpl;
import in.kenz.dashboard.entity.Role;
import in.kenz.dashboard.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    RoleDao roleDao = new RoleDaoImpl();
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

}
