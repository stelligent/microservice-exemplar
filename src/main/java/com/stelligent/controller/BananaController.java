package com.stelligent.controller;

import com.stelligent.domain.Banana;
import com.stelligent.domain.BananaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * REST service to handle the CRUD operations for a Banana
 */
@RequestMapping("/bananas")
@RestController
public class BananaController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BananaRepository bananaRepository;

  @Autowired
  private BananaResourceAssembler bananaResourceAssembler;

  /**
   * Handle a POST method by creating a new Banana
   *
   * @param banana to create
   * @return a BananaResource representation of the newly created Banana
   */
  @RequestMapping(method = RequestMethod.POST)
  public @ResponseBody BananaResource create(@RequestBody Banana banana) {
    banana = bananaRepository.save(banana);
    return bananaResourceAssembler.toResource(banana);
  }

  /**
   * Handle the GET method by searching for Bananas
   *
   * @param pickedAfter a DateTime to filter Bananas that have a pickedAt greater than
   * @param peeled filter for Bananas with matching peeled value
   * @return a list of BananaResource representations of the matching Bananas
   */
  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody List<BananaResource> search(
          @RequestParam(value="pickedAfter", required=false)
          @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
                  LocalDateTime pickedAfter,
          @RequestParam(value="peeled", required=false)
                  Boolean peeled) {

    return bananaRepository
            .findAll()
            .stream()
            .filter(b -> {
              if (Objects.nonNull(pickedAfter) && b.getPickedAt().isBefore(pickedAfter)) {
                return false;
              } else if (Objects.nonNull(peeled) && !b.getPeeled().equals(peeled)) {
                return false;
              } else {
                return true;
              }
            })
            .map(bananaResourceAssembler::toResource)
            .collect(Collectors.toList());
  }

  /**
   * Handle the GET/HEAD method by retrieving a specific Banana by id
   *
   * @param id the Banana to get
   * @return a BananaResource representation of the matching Banana
   */
  @RequestMapping(path = "/{id}", method =  {RequestMethod.GET, RequestMethod.HEAD})
  public @ResponseBody BananaResource retrieve(@PathVariable long id) {
    return bananaResourceAssembler.toResource(bananaRepository.findOne(id));
  }

  /**
   * Handle the PUT method by updating a specific Banana by id
   *
   * @param id the Banana to update
   * @param banana the Banana value to apply
   * @return a BananaResource representation of the updated Banana
   */
  @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
  public @ResponseBody BananaResource update(@PathVariable long id, @RequestBody Banana banana) {
    banana.setId(id);
    return bananaResourceAssembler.toResource(bananaRepository.save(banana));
  }

  /**
   * Handle the PATCH method by updating a specific Banana by id.  The incoming Banana object can be sparse and only non-null attributes will be updated.
   *
   * @param id the Banana to update
   * @param banana the Banana value to apply
   * @return a BananaResource representation of the updated Banana
   */
  @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
  public @ResponseBody BananaResource patch(@PathVariable long id, @RequestBody Banana banana) {
    Banana existing = bananaRepository.findOne(id);
    if(Objects.isNull(banana)) {
      return null;
    }

    if(Objects.nonNull(banana.getPeeled())) {
      existing.setPeeled(banana.getPeeled());
    }
    if(Objects.nonNull(banana.getPickedAt())) {
      existing.setPickedAt(banana.getPickedAt());
    }

    return bananaResourceAssembler.toResource(bananaRepository.save(existing));
  }

  /**
   * Handle the DELETE method by deleting a specific Banana by id.
   *
   * @param id the Banana to update
   * @return a ResponseEntity with 200 if deleted or 404 if not found
   */
  @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity delete(@PathVariable long id) {
    Banana banana = bananaRepository.findOne(id);
    if(Objects.isNull(banana)) {
      return ResponseEntity.notFound().build();
    }

    bananaRepository.delete(banana);
    return ResponseEntity.ok().build();
  }

  /**
   * Handle NullPointerException as a 404
   *
   * @param ex The exception that was raised
   * @param response the HttpServletResponse object
   */
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public void handleNullPointerException(NullPointerException ex, HttpServletResponse response) {
    logger.warn("handling NullPointerException", ex);
  }

}