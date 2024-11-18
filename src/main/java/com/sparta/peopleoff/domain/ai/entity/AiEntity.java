package com.sparta.peopleoff.domain.ai.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_ai")
public class AiEntity extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 255)
  private String aiRequest;

  @Column(nullable = false, length = 255)
  private String aiResponse;

  @ManyToOne
  @JoinColumn(name = "menu_id", nullable = false)
  private MenuEntity menu;

  public AiEntity(String prompt, String message, MenuEntity menu) {
    this.aiRequest = prompt;
    this.aiResponse = message;
    this.menu = menu;
  }
}
