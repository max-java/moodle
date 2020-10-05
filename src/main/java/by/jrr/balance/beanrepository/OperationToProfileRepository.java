package by.jrr.balance.beanrepository;


import by.jrr.balance.bean.OperationToProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ideapad on 27.9.17.
 */
@Repository
public interface OperationToProfileRepository extends CrudRepository<OperationToProfile, Long> {

    List<OperationToProfile> findAllByStreamId(Long id);

}
