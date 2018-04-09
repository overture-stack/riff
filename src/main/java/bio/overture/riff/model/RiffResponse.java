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

package bio.overture.riff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class RiffResponse {

  String id;
  String uid;
  Map<String, Object> content;
  String alias;
  boolean sharedPublicly;
  Date creationDate;
  Date updatedDate;

  public RiffResponse(Riff riff) {
    this.id = Long.toString(riff.id, 36);
    this.uid = riff.uid;
    this.content = riff.content;
    this.alias = riff.alias;
    this.sharedPublicly = riff.sharedPublicly;
    this.creationDate = riff.getCreationDate();
    this.updatedDate = riff.getUpdatedDate();
  }

}
