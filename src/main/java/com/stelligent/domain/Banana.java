package com.stelligent.domain;

import org.springframework.hateoas.Identifiable;
import java.time.LocalDateTime;

/**
 * Domain model representing the Banana object
 */
public class Banana implements Identifiable<Long>{
  private Long id;
  private LocalDateTime pickedAt;
  private Boolean peeled;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getPickedAt() {
    return pickedAt;
  }

  public void setPickedAt(LocalDateTime pickedAt) {
    this.pickedAt = pickedAt;
  }

  public Boolean getPeeled() {
    return peeled;
  }

  public void setPeeled(Boolean peeled) {
    this.peeled = peeled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Banana banana = (Banana) o;

    return id != null ? id.equals(banana.id) : banana.id == null;

  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
