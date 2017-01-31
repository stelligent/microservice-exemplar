package com.stelligent.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


/**
 * A repository to manage our Banana objects
 */
@Repository
public class BananaRepository {

  private final AtomicLong counter = new AtomicLong();
  private final List<Banana> bananas = new ArrayList<>();

  /**
   * Upsert a Banana object.  If it exists, update it...otherwise add as new.
   *
   * @param entity the banana to save
   * @return the banana that has been saved.  If new, then an id will be generated
   */
  public Banana save(Banana entity) {
    Banana existing = findOne(entity.getId());
    if (Objects.isNull(existing)) {
      entity.setId(counter.incrementAndGet());
      bananas.add(entity);
      return entity;
    } else {
      existing.setPickedAt(entity.getPickedAt());
      existing.setPeeled(entity.getPeeled());
      return existing;
    }
  }

  /**
   * Find a Banana by it's id.
   *
   * @param id the id to search by
   * @return the Banana
   */
  public Banana findOne(Long id) {
    if(Objects.isNull(id)) {
      return null;
    }

    return bananas
            .stream()
            .filter(b -> id.equals(b.getId()))
            .findFirst()
            .orElse(null);
  }

  /**
   * Return all the Bananas
   *
   * @return a List of Banana objects
   */
  public List<Banana> findAll() {
    return Collections.unmodifiableList(bananas);
  }

  /**
   * Delete a given Banana object from this repository.
   *
   * @param entity
   */
  public void delete(Banana entity) {
    bananas.remove(entity);
  }
}