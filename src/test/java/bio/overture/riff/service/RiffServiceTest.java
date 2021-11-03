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

import bio.overture.riff.AbstractTest;
import bio.overture.riff.exception.RiffNotFoundException;
import bio.overture.riff.model.ShortenRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
public class RiffServiceTest extends AbstractTest {

    private static boolean setupDone = false;
    private static String userId1 = UUID.randomUUID().toString();
    private static String userId2 = UUID.randomUUID().toString();

    private static String riffId1 = "";
    private static String riffId2 = "";
    private static String riffId3 = "";

    @PostConstruct
    public void setUp() {
        if (setupDone) {
            return;
        }

        val req1 = new ShortenRequest();
        req1.setAlias("Alias");
        req1.setContent(ImmutableMap.of("thing", "value"));
        req1.setSharedPublicly(false);

        val req2 = new ShortenRequest();
        req2.setAlias("Alias");
        req2.setContent(ImmutableMap.of("thing", "value"));
        req2.setSharedPublicly(false);

        val req3 = new ShortenRequest();
        req3.setAlias("Alias 3");
        req3.setContent(ImmutableMap.of("thing", "value"));
        req3.setSharedPublicly(false);

        val resp1 = service.makeRiff(userId1, req1);
        val resp2 = service.makeRiff(userId1, req2);
        val resp3 = service.makeRiff(userId2, req3);
        assert (resp1 != null && resp2 != null && resp3 != null);
        riffId1 = resp1.getId();
        riffId2 = resp2.getId();
        riffId3 = resp3.getId();

        setupDone = true;
    }

    @Test
    public void getUserRiffs() {
        val riffs = service.getUserRiffs(userId1);
        assertThat(riffs).hasSize(2);
    }

    @Test
    public void getRiff() {
        val riff = service.getRiff(riffId1);
        assertThat(riff).isNotNull();
        assertThat(riff.getAlias()).isEqualTo("Alias");
    }

    @Test
    public void updateRiff() {
        val testAlias = "test alias";
        val request = new ShortenRequest();

        request.setAlias(testAlias);
        service.updateRiff(userId1, riffId2, request);
        val newRiff = service.getRiff(riffId2);
        assertThat(newRiff.getId()).isEqualTo(riffId2);
        assertThat(newRiff.getAlias()).isEqualTo(testAlias);
    }

    @Test
    public void updateRiffNotAuthorized() {
        val testAlias = "test alias";
        val request = new ShortenRequest();

        request.setAlias(testAlias);
        assertThatExceptionOfType(RiffNotFoundException.class).isThrownBy(() -> service.updateRiff(userId1, riffId3, request));

        val notUpdatedRiff = service.getRiff(riffId3);
        assertThat(notUpdatedRiff.getId()).isEqualTo(riffId3);
        assertThat(notUpdatedRiff.getAlias()).isEqualTo("Alias 3");
        assertThat(notUpdatedRiff.getUid()).isEqualTo(userId2);
    }

    @Test
    public void deleteRiff(){
        val req = new ShortenRequest();
        req.setAlias("Alias to delete");
        req.setContent(ImmutableMap.of("thing", "value"));
        req.setSharedPublicly(false);
        val resp = service.makeRiff(userId1, req);
        boolean isDeleted = service.deleteRiff(userId1, resp.getId());
        assertThat(isDeleted).isTrue();
        val riffs = service.getUserRiffs(userId1);
        assertThat(riffs).hasSize(2);
    }

    @Test
    public void deleteRiffNotAuthorized(){
        boolean isDeleted = service.deleteRiff(userId1, riffId3);
        assertThat(isDeleted).isFalse();
    }
}