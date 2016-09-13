package com.stelligent.controller;

import com.stelligent.domain.Banana;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class BananaResourceAssemblerTest {
  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @InjectMocks
  private BananaResourceAssembler bananaResourceAssembler;

  @Before
  public void setUp() {

    initMocks(this);
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
  }

  @Test(expected = NullPointerException.class)
  public void testToResourceWithNull() {
    bananaResourceAssembler.toResource(null);
  }

  @Test
  public void testToResource() {
    Banana banana = new Banana();
    banana.setId(100L);
    banana.setPeeled(true);
    banana.setPickedAt(LocalDateTime.now());
    BananaResource bananaResource = bananaResourceAssembler.toResource(banana);

    Assert.assertEquals("peeled",banana.getPeeled(), bananaResource.peeled);
    Assert.assertEquals("picked at",banana.getPickedAt(), bananaResource.pickedAt);

    List<Link> links = bananaResource.getLinks();
    Assert.assertEquals("links size",1,links.size());
    Assert.assertEquals("links rel","self",links.get(0).getRel());
    Assert.assertEquals("links href","http://localhost/bananas/100",links.get(0).getHref());
  }
}
