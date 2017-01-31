package com.stelligent.controller;

import com.stelligent.domain.Banana;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * A ResourceAssembler to convert a Banana domain object into a HATEOAS Resource (with links)
 */
@Component
public class BananaResourceAssembler extends ResourceAssemblerSupport<Banana, BananaResource> {
  public BananaResourceAssembler() {
    super(BananaController.class, BananaResource.class);
  }

  /**
   * Convert a Banana into a BananaResource and add links to the resource
   *
   * @param banana The Banana domain object
   * @return A BananaResource representation of the Banana
   * @throws NullPointerException if banana is null
   */
  public BananaResource toResource(Banana banana) throws NullPointerException {
    if(Objects.isNull(banana)) {
      throw new NullPointerException();
    }

    BananaResource bananaResource = createResourceWithId(banana.getId(), banana); // adds a "self" link
    bananaResource.pickedAt = banana.getPickedAt();
    bananaResource.peeled = banana.getPeeled();
    return bananaResource;
  }
}
