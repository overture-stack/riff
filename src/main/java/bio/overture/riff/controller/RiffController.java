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

import bio.overture.riff.model.RiffResponse;
import bio.overture.riff.model.ShortenRequest;
import bio.overture.riff.service.RiffService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.keycloak.KeycloakPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/riff")
public class RiffController {

  private RiffService service;

  @Autowired
  public RiffController(RiffService service) {
    this.service = service;
  }

  @GetMapping("/user/{userId}") // path variable userId is not used but we keep it for backward compatibility.
  public List<RiffResponse> getUserRiffs(@PathVariable String userId) {
    val userIdFromJwtToken = ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
    return service.getUserRiffs(userIdFromJwtToken);
  }

  @GetMapping("/{id}")
  public RiffResponse getRiff(@PathVariable("id") String id) {
    return service.getRiff(id);
  }

  @PostMapping("/shorten")
  public RiffResponse makeRiff(@RequestBody ShortenRequest request) {
    val userId = ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
    return service.makeRiff(userId, request);
  }

  @PutMapping("/{id}")
  public RiffResponse updateRiff(@PathVariable("id") String id, @RequestBody ShortenRequest request) {
    val userId = ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
    return service.updateRiff(userId, id, request);
  }

  @DeleteMapping("/{id}")
  public boolean deleteRiff(@PathVariable("id") String id) {
    val userId = ((KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
    return service.deleteRiff(userId, id);
  }

}
