/*
 * Copyright (c) 2018. Ontario Institute for Cancer Research
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
@RequestMapping("/")
public class RiffController {

  private JWTFacadeInterface jwtFacade;
  private RiffService service;

  public RiffController(JWTFacadeInterface jwtFacade, RiffService service) {
    this.jwtFacade = jwtFacade;
    this.service = service;
  }

  @GetMapping
  public List<Riff> getUserRiffs() {
    //val user = jwtFacade.getUser();
    // TODO: Return a users riffs.
    return null;
  }

  @GetMapping("{id}")
  public Riff getRiff(@PathVariable("id") String id) {
    return service.getRiff(id);
  }

  @PostMapping("shorten")
  public String makeRiff(@RequestBody ShortenRequest request) {
    val user = jwtFacade.getUser();

    if (user.isPresent()) {
      return service.makeRiff(user.get(), request);
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @PutMapping("{id}")
  public Riff updateRiff(@PathParam("id") String id, @RequestBody ShortenRequest request) {
    // TODO: do update
    return service.getRiff(id);
  }

  @DeleteMapping("{id}")
  public String deleteRiff(@PathParam("id") String id) {
    // val user = jwtFacade.getUser();
    // TODO: Make sure to only delete if user matches owner of riff.
    return "ok";
  }

}