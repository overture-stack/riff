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

package bio.overture.riff.service;

import bio.overture.riff.jwt.JWTUser;
import bio.overture.riff.model.Riff;
import bio.overture.riff.model.ShortenRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RiffService {

  public Riff getRiff(String id) {
    return Riff.builder()
        .id(id)
        .content("foobar")
        .uid("fakeuser")
        .alias("example")
        .shared("false")
        .createdDate(new Date())
        .updatedDate(new Date())
        .build();
  }

  public String makeRiff(JWTUser user, ShortenRequest request) {
    return "ABC123";
  }

}
