package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.SubscriptionPermission;
import java.util.List;

@Repository
public interface SubscriptionPermissionRepository extends JpaRepository<SubscriptionPermission, String> {

    SubscriptionPermission findBySubscriptionType(String subscriptionType);

}
