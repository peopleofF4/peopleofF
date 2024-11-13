package com.sparta.peopleoff.domain.ai.repository;

import com.sparta.peopleoff.domain.ai.entity.AiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiRepository extends JpaRepository<AiEntity, UUID> {

}
