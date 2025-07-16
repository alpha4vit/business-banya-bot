package ru.snptech.businessbanyabot.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegionRepository extends CrudRepository<Region, Long> {
    Region findByName(String name);
    @NotNull List<Region> findAll();
}
