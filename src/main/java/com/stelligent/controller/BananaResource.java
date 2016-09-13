package com.stelligent.controller;

import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;

public class BananaResource extends ResourceSupport {
  public LocalDateTime pickedAt;
  public Boolean peeled;
}
