package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Role;

import java.util.List;

public interface RoleDao {
    void save(Role role);
    Role findByRoleId(Long roleId);
    Role findByRoleName(String roleName);
    List<Role> findAll();
    void deleteByRoleId(Long roleId);
}
