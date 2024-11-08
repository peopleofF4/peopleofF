package com.sparta.peopleoff.domain.store.repository;

import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

}
