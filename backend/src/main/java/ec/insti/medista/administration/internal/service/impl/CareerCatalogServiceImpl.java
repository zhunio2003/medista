package ec.insti.medista.administration.internal.service.impl;

import ec.insti.medista.administration.api.service.CareerCatalogService;
import ec.insti.medista.administration.internal.entity.Career;
import ec.insti.medista.administration.internal.repository.CareerRepository;
import ec.insti.medista.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareerCatalogServiceImpl implements CareerCatalogService {

    private final CareerRepository careerRepository;

    @Override
    public boolean existsActiveById(Long id){

        return careerRepository.existsByIdAndIsActiveTrue(id);

    }

    @Override
    public String findNameById(Long id){

        return careerRepository.findById(id)
                .map(Career::getName)
                .orElseThrow(() -> new ResourceNotFoundException("Career not found" + id));

    }

    @Override
    public Map<Long, String> findNamesByIds(Collection<Long> ids){

        if(ids.isEmpty()) return Map.of();

        return careerRepository.findAllByIdIn(ids).stream()
                .collect(Collectors.toMap(Career::getId, Career::getName));
    }

}
