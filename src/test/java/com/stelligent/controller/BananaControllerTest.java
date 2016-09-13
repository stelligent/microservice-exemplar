package com.stelligent.controller;

import com.stelligent.domain.Banana;
import com.stelligent.domain.BananaRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BananaController.class)
public class BananaControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private BananaRepository bananaRepository;

  @Test
  public void testGetAllShouldReturnEmpty() throws Exception {
    given(this.bananaRepository.findAll())
            .willReturn(new ArrayList<>());

    this.mvc.perform(get("/bananas")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  public void testGetAllShouldReturnMatch() throws Exception {
    List<Banana> bananaList = new ArrayList<>();
    bananaList.add(newBanana(1, true, LocalDateTime.now()));
    bananaList.add(newBanana(2, false, LocalDateTime.now().minusDays(1)));
    bananaList.add(newBanana(3, true, LocalDateTime.now().minusDays(1)));

    given(this.bananaRepository.findAll())
            .willReturn(bananaList);

    this.mvc.perform(get("/bananas")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));

    this.mvc.perform(get("/bananas")
            .param("peeled","true")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

    this.mvc.perform(get("/bananas")
            .param("pickedAfter",LocalDateTime.now().minusHours(1).toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void testGetOneShouldReturnMatch() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(b);

    this.mvc.perform(get("/bananas/2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.peeled", is(true)))
            .andExpect(jsonPath("$._links.self.href", is("http://localhost/bananas/2")));
  }

  @Test
  public void testGetOneShouldReturn404() throws Exception {
    given(this.bananaRepository.findOne(2L)).willReturn(null);

    this.mvc.perform(get("/bananas/2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testPostShouldSucceed() throws Exception {
    Banana b = newBanana(20,true,LocalDateTime.now());
    given(this.bananaRepository.save(b)).willReturn(b);

    this.mvc.perform(post("/bananas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(b))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.peeled", is(true)))
            .andExpect(jsonPath("$._links.self.href", is("http://localhost/bananas/20")));
  }

  @Test
  public void testPutOneShouldReturnMatch() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(b);
    given(this.bananaRepository.save(b)).willReturn(b);

    this.mvc.perform(put("/bananas/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(b))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.peeled", is(true)))
            .andExpect(jsonPath("$._links.self.href", is("http://localhost/bananas/2")));
  }

  @Test
  public void testPutOneShouldReturn404() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(null);

    this.mvc.perform(put("/bananas/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(b))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testPatchOneShouldReturnMatch() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(b);
    given(this.bananaRepository.save(b)).willReturn(b);

    this.mvc.perform(put("/bananas/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(b))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.peeled", is(true)))
            .andExpect(jsonPath("$._links.self.href", is("http://localhost/bananas/2")));
  }

  @Test
  public void testPatchOneShouldReturn404() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(null);

    this.mvc.perform(patch("/bananas/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(b))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteOneShouldSucceed() throws Exception {
    Banana b = newBanana(2,true,LocalDateTime.now());
    given(this.bananaRepository.findOne(2L)).willReturn(b);

    this.mvc.perform(delete("/bananas/2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    then(this.bananaRepository).should().delete(b);
  }

  @Test
  public void testDeleteOneShouldReturn404() throws Exception {
    given(this.bananaRepository.findOne(2L)).willReturn(null);

    this.mvc.perform(delete("/bananas/2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  private Banana newBanana(int id, boolean peeled, LocalDateTime pickedAt) {
    Banana b = new Banana();
    b.setId(Integer.toUnsignedLong(id));
    b.setPeeled(peeled);
    b.setPickedAt(pickedAt);
    return b;
  }

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

}
