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

import bio.overture.riff.jwt.JWTUser;
import bio.overture.riff.model.ShortenRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RiffServiceTest {

  private static boolean setupDone = false;
  private static String UID = UUID.randomUUID().toString();
  @Autowired
  private RiffService service;

  private JWTUser user;

  @Before
  public void setUp() {
    this.user = new JWTUser();
    user.setUid(UID);
    user.setRoles(Lists.emptyList());
    user.setCreatedAt(DateTime.now().toString());
    user.setFirstName("Foo");
    user.setLastName("Bar");
    user.setName("Foo Bar");
    user.setEmail("foobar@test.org");

    if (setupDone) {
      return;
    }

    val req = new ShortenRequest();
    req.setAlias("Alias");
    req.setContent(ImmutableMap.of("thing", "value"));
    req.setSharedPublicly(false);

    val resp = service.makeRiff(user, req);
    assert(resp!=null);
    setupDone = true;
  }

  @Test
  public void getUserRiffs() {
    val riffs = service.getUserRiffs(this.user);
    assertThat(riffs).hasSize(1);
  }

  @Test
  public void getRiff() {
    val riff = service.getRiff("1");
    assertThat(riff).isNotNull();
    assertThat(riff.getAlias()).isEqualTo("Alias");
  }

  @Test
  public void updateRiff() {
  }
}