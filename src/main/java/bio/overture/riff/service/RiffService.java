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

package bio.overture.riff.service;

import bio.overture.riff.exception.RiffNotFoundException;
import bio.overture.riff.jwt.JWTUser;
import bio.overture.riff.model.Riff;
import bio.overture.riff.model.RiffResponse;
import bio.overture.riff.model.ShortenRequest;
import bio.overture.riff.repository.RiffRepository;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiffService {

  private RiffRepository repository;

  @Autowired
  public RiffService(RiffRepository repository) {
    this.repository = repository;
  }

  public List<RiffResponse> getUserRiffs(JWTUser user) {
    return repository.findByUidAndSharedPublicly(user.getUid(), false)
      .stream()
      .map(RiffResponse::new)
      .collect(Collectors.toList());
  }

  @SneakyThrows
  public RiffResponse getRiff(String id) {
    val opt = repository.findById(Long.valueOf(id, 36));
    if (opt.isPresent()) {
      return new RiffResponse(opt.get());
    } else {
      throw new RiffNotFoundException();
    }
  }

  @SneakyThrows
  public RiffResponse makeRiff(JWTUser user, ShortenRequest request) {
    val riff = Riff.builder()
      .content(request.getContent())
      .uid(user.getUid())
      .alias(request.getAlias())
      .sharedPublicly(request.isSharedPublicly())
      .creationDate(new Date())
      .updatedDate(new Date())
      .build();

    val newRiff = repository.save(riff);
    return new RiffResponse(newRiff);
  }

  public boolean deleteRiff(JWTUser user, String id) {
    val optionalRiff = repository.findById(Long.valueOf(id, 36));
    if (optionalRiff.isPresent()) {
      val riff = optionalRiff.get();
      if (user.getUid().equals(riff.getUid())) {
        repository.delete(riff);
        return true;
      }
    }
    return false;
  }

  @SneakyThrows
  public RiffResponse updateRiff(JWTUser user, String id, ShortenRequest request) {
    val optionalRiff = repository.findById(Long.valueOf(id, 36));
    if (optionalRiff.isPresent()) {
      val riff = Riff.builder()
        .content(request.getContent())
        .uid(user.getUid())
        .alias(request.getAlias())
        .sharedPublicly(request.isSharedPublicly())
        .creationDate(optionalRiff.get().getCreationDate())
        .updatedDate(new Date())
        .build();
      val updated = repository.save(riff);
      return new RiffResponse(updated);
    } else {
     throw new RiffNotFoundException();
    }
  }

}
