package com.stelligent.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by casey.lee on 9/12/16.
 */
public class BananaRepositoryTest {
  private BananaRepository repository;

  @Before
  public void setup() {
    repository = new BananaRepository();
  }

  @After
  public void teardown() {
    repository = null;
  }

  private Banana newBanana(boolean peeled, LocalDateTime pickedAt) {
    Banana b = new Banana();
    b.setPeeled(peeled);
    b.setPickedAt(pickedAt);
    return b;
  }

  @Test
  public void testSaveNew() {
    List<Banana> results= repository.findAll();
    Assert.assertTrue("results are empty", results.isEmpty());

    Banana b = repository.save(newBanana(false, LocalDateTime.now()));
    Assert.assertNotNull("id", b.getId());

    results = repository.findAll();
    Assert.assertEquals("results size", 1, results.size());
    Assert.assertEquals("id", b.getId(), results.get(0).getId());
  }

  @Test
  public void testSaveExisting() {
    Banana b = repository.save(newBanana(false, LocalDateTime.now()));
    List<Banana> results= repository.findAll();
    Assert.assertEquals("results size", 1, results.size());

    b.setPeeled(true);
    repository.save(b);

    results = repository.findAll();
    Assert.assertEquals("results size", 1, results.size());
    Assert.assertTrue("peeled", results.get(0).getPeeled());
  }

  @Test
  public void testFindOneEmpty() {
    Banana b = repository.findOne(100L);
    Assert.assertNull("result is null", b);
  }

  @Test
  public void testFindOneNullId() {
    Banana b = repository.findOne(null);
    Assert.assertNull("result is null", b);
  }

  @Test
  public void testFindOneWithResults() {
    repository.save(newBanana(false, LocalDateTime.now()));
    Banana expectedBanana = repository.save(newBanana(true, LocalDateTime.now()));
    Banana actualBanana = repository.findOne(expectedBanana.getId());

    Assert.assertEquals("id", expectedBanana.getId(), actualBanana.getId());
    Assert.assertEquals("peeled", expectedBanana.getPeeled(), actualBanana.getPeeled());
    Assert.assertEquals("pickedAt", expectedBanana.getPickedAt(), actualBanana.getPickedAt());

  }

  @Test
  public void testFindAllEmpty() {
    List<Banana> results= repository.findAll();
    Assert.assertTrue("results are empty", results.isEmpty());
  }

  @Test
  public void testFindAllWithResults() {
    repository.save(newBanana(false, LocalDateTime.now()));
    repository.save(newBanana(true, LocalDateTime.now()));
    List<Banana> results= repository.findAll();
    Assert.assertEquals("results size", 2, results.size());
  }

  @Test
  public void testDeleteNotFound() {
    repository.save(newBanana(false, LocalDateTime.now()));
    repository.save(newBanana(true, LocalDateTime.now()));
    List<Banana> results= repository.findAll();
    repository.delete(null);
    Banana b = new Banana();
    b.setId(100L);
    repository.delete(b);
    Assert.assertEquals("results size", 2, results.size());
  }

  @Test
  public void testDeleteFound() {
    repository.save(newBanana(true, LocalDateTime.now()));
    Banana b = repository.save(newBanana(true, LocalDateTime.now()));
    List<Banana> results = repository.findAll();
    Assert.assertEquals("results size", 2, results.size());

    repository.delete(b);
    results = repository.findAll();
    Assert.assertEquals("results size", 1, results.size());
    Assert.assertNotEquals("results size", b.getId(), results.get(0).getId());
  }

}
