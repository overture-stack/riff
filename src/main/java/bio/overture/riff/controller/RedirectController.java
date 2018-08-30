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
import bio.overture.riff.service.RiffService;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/s")
public class RedirectController {

  private RiffService service;
  private RiffConfig config;

  @Autowired
  public RedirectController(RiffService service, RiffConfig config) {
    this.service = service;
    this.config = config;
  }

  @GetMapping("/{id}")
  @SneakyThrows
  public void handleResolution(HttpServletResponse response, @PathVariable("id") String id) {
    val key = config.getUrlKey();
    val riff = service.getRiff(id);
    response.sendRedirect(riff.getContent().get(key).toString());
  }

}
