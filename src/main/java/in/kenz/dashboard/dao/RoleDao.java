package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Role;

public interface RoleDao {
    void save(Role role);
    Role findByRoleId(Long roleId);
    Role findByRoleName(String roleName);
    void deleteByRoleId(Long roleId);
}
