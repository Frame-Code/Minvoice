package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.interfaces.IGeneralStatusService;
import com.minvoice.demo.domain.model.GeneralStatus;
import com.minvoice.demo.domain.repository.IGeneralStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CommonsLog
@RequiredArgsConstructor
public class GeneralStatusServiceImpl implements IGeneralStatusService {
    private final IGeneralStatusRepository repository;

    @Override
    public List<GeneralStatus> findByGroup(String group) {
        return repository.FindByGroup(group);
    }
}
