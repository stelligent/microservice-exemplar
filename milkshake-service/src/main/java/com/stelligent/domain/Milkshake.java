package com.stelligent.domain;

import org.springframework.hateoas.Identifiable;
import java.time.LocalDateTime;

/**
 * Domain model representing the Milkshake object
 */
public class Milkshake implements Identifiable<Long>{
  private Long id;
  private Flavor flavor;

  public enum Flavor {
    Banana
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Flavor getFlavor() {
    return flavor;
  }

  public void setFlavor(Flavor flavor) {
    this.flavor = flavor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Milkshake milkshake = (Milkshake) o;

    return id != null ? id.equals(milkshake.id) : milkshake.id == null;

  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
