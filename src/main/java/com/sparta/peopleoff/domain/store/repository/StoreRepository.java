package com.sparta.peopleoff.domain.store.repository;

import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

  List<StoreEntity> findByStoreNameContainingOrCategory_CategoryNameContainingOrStoreAddressContainingOrStorePhoneNumberContaining(
      String storeName, String categoryName, String storeAddress, String storePhoneNumber);
}
