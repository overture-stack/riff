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

import bio.overture.riff.config.RiffConfig;
import bio.overture.riff.jwt.JWTFacadeInterface;
import bio.overture.riff.model.RiffResponse;
import bio.overture.riff.model.ShortenRequest;
import bio.overture.riff.service.RiffService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/riff")
public class RiffController {

  private JWTFacadeInterface jwtFacade;
  private RiffService service;

  @Autowired
  public RiffController(JWTFacadeInterface jwtFacade, RiffService service) {
    this.jwtFacade = jwtFacade;
    this.service = service;
  }

  @GetMapping("/user/{userId}")
  public List<RiffResponse> getUserRiffs(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) final String accessToken,
                                         @PathVariable String userId) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      return service.getUserRiffs(user.get());
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @GetMapping("/{id}")
  public RiffResponse getRiff(@PathVariable("id") String id) {
    return service.getRiff(id);
  }

  @PostMapping("/shorten")
  public RiffResponse makeRiff(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) final String accessToken,
                               @RequestBody ShortenRequest request) {
    val user = jwtFacade.getUser();

    if (user.isPresent()) {
      return service.makeRiff(user.get(), request);
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @PutMapping("/{id}")
  public RiffResponse updateRiff(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) final String accessToken,
                                 @PathVariable("id") String id, @RequestBody ShortenRequest request) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      return service.updateRiff(user.get(), id, request);
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @DeleteMapping("/{id}")
  public boolean deleteRiff(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) final String accessToken,
                            @PathVariable("id") String id) {
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      return service.deleteRiff(user.get(), id);
    } else {
      throw new UnauthorizedUserException("No user");
    }
  }

  @DeleteMapping("/delete-phantom-sets")
  public long deletePhantomSets(@RequestHeader(value = HttpHeaders.AUTHORIZATION) final String accessToken) {
    long result;
    val user = jwtFacade.getUser();
    if (user.isPresent()) {
      result = service.deletePhantomSets(user.get());
    } else {
      throw new UnauthorizedUserException("No user");
    }
    return result;
  }

}
