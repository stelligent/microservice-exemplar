package com.stelligent.controller;

import com.stelligent.domain.Milkshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;


/**
 * REST service to handle the CRUD operations for a Milkshake
 */
@RequestMapping("/milkshakes")
@RestController
public class MilkshakeController {

  private final AtomicLong counter = new AtomicLong();
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Handle a POST method by creating a new Milkshake.
   *  Queries appropriate fruit service to check for inventory and consume the fruit into the milkshake
   *
   * @param flavor to create
   * @return a newly created Milkshake
   */
  @RequestMapping(method = RequestMethod.POST)
  public @ResponseBody Milkshake create(@RequestParam Milkshake.Flavor flavor) {
    // TODO: call the banana service to check for inventory and DELETE 2 bananas for the milkshake

    Milkshake milkshake = new Milkshake();
    milkshake.setId(counter.incrementAndGet());
    milkshake.setFlavor(flavor);
    return milkshake;
  }

}