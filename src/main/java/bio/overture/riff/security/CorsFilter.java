package bio.overture.riff.security;

import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

  @Override
  @SneakyThrows
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
    final HttpServletResponse response = (HttpServletResponse) res;
    final HttpServletRequest request = (HttpServletRequest) req;
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET,HEAD,PUT,PATCH,POST,DELETE");
    response.addHeader("Access-Control-Allow-Headers","*");
    response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
    response.addHeader("Access-Control-Allow-Credentials", "true");
    response.addIntHeader("Access-Control-Max-Age", 100);
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } else {
      filterChain.doFilter(request, response);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void destroy() {

  }

}
