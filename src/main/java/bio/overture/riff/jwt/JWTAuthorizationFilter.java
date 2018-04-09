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

package bio.overture.riff.jwt;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class JWTAuthorizationFilter extends GenericFilterBean {

  private final String USER_ROLE = "USER";
  private final String ADMIN_ROLE = "ADMIN";

  private final String APPROVED_STATUS = "Approved";
  private final String PENDING_STATUS = "Pending";

  @Override
  @SneakyThrows
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    val authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {

      val details = (OAuth2AuthenticationDetails) authentication.getDetails();
      val user = (JWTUser) details.getDecodedDetails();

      boolean hasCorrectRole = user.getRoles().contains(USER_ROLE) || user.getRoles().contains(ADMIN_ROLE);
      boolean hasCorrectStatus = user.getStatus().equalsIgnoreCase(APPROVED_STATUS) || user.getStatus().equalsIgnoreCase(PENDING_STATUS);

      if (!hasCorrectRole || !hasCorrectStatus) {
        SecurityContextHolder.clearContext();
      }
    }

    chain.doFilter(request, response);
  }

}
