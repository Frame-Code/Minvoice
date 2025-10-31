package com.minvoice.demo.application.services.interfaces;

import com.minvoice.demo.domain.model.GeneralStatus;

import java.util.List;

public interface IGeneralStatusService {
    List<GeneralStatus> findByGroup(String group);
}
