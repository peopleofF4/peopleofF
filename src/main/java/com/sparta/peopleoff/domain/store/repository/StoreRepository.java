package com.sparta.peopleoff.domain.store.repository;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, UUID> {

  Page<StoreEntity> findByDeletionStatusAndStoreNameContainingOrCategory_CategoryNameContainingOrStoreAddressContainingOrStorePhoneNumberContaining(
      DeletionStatus deletionStatus,
      String storeName,
      String categoryName,
      String storeAddress,
      String storePhoneNumber,
      Pageable pageable);

  Page<StoreEntity> findByDeletionStatus(DeletionStatus deletionStatus, Pageable pageable);
}
