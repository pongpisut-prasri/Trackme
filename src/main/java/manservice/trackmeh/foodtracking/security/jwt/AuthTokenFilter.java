package manservice.trackmeh.foodtracking.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manservice.trackmeh.foodtracking.entity.SubscriptionPermission;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.SubscriptionPermissionRepository;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;
import manservice.trackmeh.foodtracking.service.impl.UserDetailServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailServiceImpl userDetailsService;

  // @Autowired
  // private RolePermissionService rolePermissionService;

  @Autowired
  private SubscriptionPermissionRepository subscriptionPermissionRepository;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    HttpServletRequest cachedRequest = new ContentCachingRequestWrapper(request);
    try {
      String jwt = parseJwt(cachedRequest);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        UserModel userModel = jwtUtils.getUserDetail(jwt);
        try {
          boolean isCurrentJwt = jwtUtils.validateJwtToken(userDetails.getUserModel().getJwt());
          if (!parseJwt(userDetails.getUserModel().getJwt()).equals(jwt)) {
            throw new Exception("header jwt is invalid");
          }
        } catch (Exception e) {
          throw new Exception(e.getMessage());
        }
        SubscriptionPermission subsciptionPermission = subscriptionPermissionRepository
            .findBySubscriptionType(userModel.getSubscriptionType());
        List<SimpleGrantedAuthority> authorities = subsciptionPermission.getPermission().stream()
            .map(o -> new SimpleGrantedAuthority(o)).collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(cachedRequest, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    return parseJwt(bearerToken);
  }

  public static String parseJwt(String bearerToken) {

    if (StringUtils.hasText(bearerToken)) {
      if (bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
      } else {
        return bearerToken;
      }

    }
    return null;
  }

}
