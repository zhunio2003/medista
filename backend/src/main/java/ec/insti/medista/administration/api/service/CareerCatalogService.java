package ec.insti.medista.administration.api.service;

import java.util.Collection;
import java.util.Map;

public interface CareerCatalogService {

    boolean existsActiveById(Long id);
    String findNameById(Long id);
    Map<Long, String> findNamesByIds(Collection<Long> ids);

}
