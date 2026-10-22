package addressbook;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Pin the REST path so buddies are always at /api/buddyInfos.
@RepositoryRestResource(path = "buddyInfos", collectionResourceRel = "buddyInfos")
public interface BuddyInfoRepository extends CrudRepository<BuddyInfo, Long> {
}
