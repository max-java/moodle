package by.jrr.crm.controller.admin.repo;

import by.jrr.constant.Endpoint;
import by.jrr.crm.controller.admin.bean.ErrorsLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = Endpoint.REGISTER_USER_ADMIN_REST_ERRORS)
public interface ErrorsLogRestRepository extends PagingAndSortingRepository<ErrorsLog, Long> {
}
