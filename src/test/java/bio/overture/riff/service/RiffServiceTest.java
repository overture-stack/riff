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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RiffServiceTest {

    private static boolean setupDone = false;
    private static String UID = UUID.randomUUID().toString();
    private static String UID2 = UUID.randomUUID().toString();
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
        req.setDescription("This is a description");
        req.setSharedPublicly(false);

        val req2 = new ShortenRequest();
        req2.setAlias("Alias");
        req2.setContent(ImmutableMap.of("thing", "value"));
        req2.setSharedPublicly(false);

        val resp = service.makeRiff(user, req);
        val resp2 = service.makeRiff(user, req2);
        assert (resp != null && resp2 != null);

        val user2 = new JWTUser();
        user2.setUid(UID2);
        user2.setRoles(Lists.emptyList());
        user2.setCreatedAt(DateTime.now().toString());
        user2.setFirstName("Another");
        user2.setLastName("User");
        user2.setName("Another User");
        user2.setEmail("anotheruser@test.org");
        val req3 = new ShortenRequest();
        req3.setAlias("Alias 3");
        req3.setContent(ImmutableMap.of("thing", "value"));
        req3.setSharedPublicly(false);
        service.makeRiff(user2, req3);
        setupDone = true;
    }

    @Test
    public void getUserRiffs() {
        val riffs = service.getUserRiffs(this.user);
        assertThat(riffs).hasSize(2);
    }

    @Test
    public void getRiff() {
        val riff = service.getRiff("1");
        assertThat(riff).isNotNull();
        assertThat(riff.getAlias()).isEqualTo("Alias");
        assertThat(riff.getDescription()).isEqualTo("This is a description");
    }

    @Test
    public void updateRiff() {
        val riffId = "2";
        val testAlias = "test alias";
        val request = new ShortenRequest();

        request.setAlias(testAlias);
        service.updateRiff(this.user, riffId, request);
        val newRiff = service.getRiff(riffId);
        assertThat(newRiff.getId()).isEqualTo(riffId);
        assertThat(newRiff.getAlias()).isEqualTo(testAlias);
    }

    @Test
    public void updateRiffNotAuthorized() {
        val riffId = "3";
        val testAlias = "test alias";
        val request = new ShortenRequest();

        request.setAlias(testAlias);
        assertThatExceptionOfType(RiffNotFoundException.class).isThrownBy(() -> service.updateRiff(this.user, riffId, request));

        val notUpdatedRiff = service.getRiff(riffId);
        assertThat(notUpdatedRiff.getId()).isEqualTo(riffId);
        assertThat(notUpdatedRiff.getAlias()).isEqualTo("Alias 3");
        assertThat(notUpdatedRiff.getUid()).isEqualTo(UID2);
    }

    @Test
    public void deleteRiff(){
        val req = new ShortenRequest();
        req.setAlias("Alias to delete");
        req.setContent(ImmutableMap.of("thing", "value"));
        req.setSharedPublicly(false);
        val resp = service.makeRiff(user, req);
        boolean isDeleted = service.deleteRiff(user, resp.getId());
        assertThat(isDeleted).isTrue();
        val riffs = service.getUserRiffs(this.user);
        assertThat(riffs).hasSize(2);
    }

    @Test
    public void deleteRiffNotAuthorized(){
        boolean isDeleted = service.deleteRiff(user, "3");
        assertThat(isDeleted).isFalse();

    }
}