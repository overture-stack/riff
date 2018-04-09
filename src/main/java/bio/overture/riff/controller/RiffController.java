/*
 * Copyright (c) 2018. The Ontario Institute for Cancer Research. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package bio.overture.riff.controller;

import bio.overture.riff.jwt.JWTFacadeInterface;
import bio.overture.riff.model.Riff;
import bio.overture.riff.model.ShortenRequest;
import bio.overture.riff.service.RiffService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/riff")
public class RiffController {

  private JWTFacadeInterface jwtFacade;
  private RiffService service;

  public RiffController(JWTFacadeInterface jwtFacade, RiffService service) {
    this.jwtFacade = jwtFacade;
    this.service = service;
  }

  @GetMapping("/user/{userId}")
  public List<Riff> getUserRiffs(@PathVariable String userId) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      return service.getUserRiffs(user.get());
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @GetMapping("/{id}")
  public Riff getRiff(@PathVariable("id") String id) {
    val riff = service.getRiff(id);
    if (riff.isPresent()) {
      return riff.get();
    } else {
      return null;
    }
  }

  @PostMapping("/shorten")
  public Riff makeRiff(@RequestBody ShortenRequest request) {
    val user = jwtFacade.getUser();

    if (user.isPresent()) {
      return service.makeRiff(user.get(), request);
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @PutMapping("/{id}")
  public Riff updateRiff(@PathParam("id") String id, @RequestBody ShortenRequest request) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      return service.updateRiff(user.get(), id, request);
    } else {
      return null;
    }
  }

  @DeleteMapping("/{id}")
  public Boolean deleteRiff(@PathVariable("id") String id) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      val deleted = service.deleteRiff(user.get(), id);
      return deleted;
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

}
