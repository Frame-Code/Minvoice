package com.minvoice.demo.application.services.interfaces;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    Optional<Double> findItemPriceByName(String name);
    Optional<Double> findTotalPricesByCodes(List<String> codes);
}
