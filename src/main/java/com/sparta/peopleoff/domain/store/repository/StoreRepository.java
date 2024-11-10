package com.sparta.peopleoff.domain.store.repository;

import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

}
