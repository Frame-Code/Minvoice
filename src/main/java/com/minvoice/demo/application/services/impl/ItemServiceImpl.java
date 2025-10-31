package com.minvoice.demo.application.services.impl;

import com.minvoice.demo.application.services.interfaces.IItemService;
import com.minvoice.demo.domain.model.Item;
import com.minvoice.demo.domain.repository.IItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService {
    private final IItemRepository repository;

    @Override
    public Optional<Double> findItemPriceByName(String name) {
        return repository.getItemByName(name)
                .stream()
                .map(Item::getPrice)
                .findAny();
    }

    @Override
    public Optional<Double> findTotalPricesByCodes(List<String> codes) {
        List<Item> itemsSearched = new ArrayList<>();
        var items = repository.findAll();

        codes.forEach(code -> {
            var item = items.stream()
                    .filter(i -> i.getCode().trim().equalsIgnoreCase(code.trim()))
                    .findFirst();
            item.ifPresent(itemsSearched::add);
        });

        return itemsSearched.isEmpty()? Optional.empty() :
                Optional.of(itemsSearched.stream()
                        .mapToDouble(Item::getPrice)
                        .sum());
    }


}
