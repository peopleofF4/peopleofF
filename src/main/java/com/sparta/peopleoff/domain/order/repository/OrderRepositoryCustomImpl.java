package com.sparta.peopleoff.domain.order.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.entity.QOrderEntity;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import com.sparta.peopleoff.domain.orderdetail.entity.QOrderDetailEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  QOrderEntity orderEntity = QOrderEntity.orderEntity;
  QOrderDetailEntity orderDetail = QOrderDetailEntity.orderDetailEntity;

  @Override
  public Page<OrderEntity> searchOrder(OrderType orderType, UUID menuId, Pageable pageable) {

    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(orderEntity.deletionStatus.eq(DeletionStatus.ACTIVE));

    if (orderType != null) {
      predicate.and(orderEntity.orderType.eq(orderType));
    }

    if (menuId != null) {
      predicate.and(orderDetail.menu.id.eq(menuId));
    }

    JPAQuery<OrderEntity> query = jpaQueryFactory.selectFrom(orderEntity)
        .leftJoin(orderEntity.orderDetailList, orderDetail)
        .where(predicate)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    Map<String, OrderSpecifier> sortingMap = new HashMap<>();
    sortingMap.put("created_At", orderEntity.createdAt.desc());
    sortingMap.put("updated_At", orderEntity.updatedAt.desc());

    if (pageable.getSort().isSorted()) {
      pageable.getSort().forEach(order -> {
        OrderSpecifier orderSpecifier = sortingMap.get(order.getProperty());
        if (orderSpecifier != null) {
          query.orderBy(orderSpecifier);
        }
      });
    }

    List<OrderEntity> result = query.fetch();

    long total = jpaQueryFactory.selectFrom(orderEntity)
        .leftJoin(orderEntity.orderDetailList, orderDetail)
        .where(predicate)
        .fetchCount();

    return new PageImpl<>(result, pageable, total);
  }
}
