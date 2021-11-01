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

package bio.overture.riff.repository;

import bio.overture.riff.model.Riff;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface RiffRepository extends CrudRepository<Riff, Long> {

    List<Riff> findByUidAndSharedPublicly(String uid, Boolean shared);

    @Modifying
    @Transactional
    @Query("DELETE FROM Riff r WHERE r.creationDate <= :creationDate AND r.alias = :alias")
    int deleteByAliasAndCreationDateBefore(@Param("alias") String alias, @Param("creationDate") Date creationDate);

}
