package ntukhpi.ddy.webjavaddylab3.repository;

import ntukhpi.ddy.webjavaddylab3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}