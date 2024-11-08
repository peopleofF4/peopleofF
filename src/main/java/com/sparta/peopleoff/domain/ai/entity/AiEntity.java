package com.sparta.peopleoff.domain.ai.entity;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ai")
public class AiEntity {

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
}
