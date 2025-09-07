package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import manservice.trackmeh.foodtracking.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {

}
