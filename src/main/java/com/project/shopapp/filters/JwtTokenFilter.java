package com.project.shopapp.filters;

import com.nimbusds.jose.util.Pair;
import com.project.shopapp.Components.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    //kiem tra xem token co hop le hay khong
    // 2 cai request login va register la dc di qua.
    @Override
    //– Khi máy khách yêu cầu một tài nguyên web, chẳng hạn như Servlet hoặc trang JSP, thì trình chứa web sẽ gọi hàm này.
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response); //mo pass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing");
                return;
            }
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

                if (phoneNumber != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        //set authentication token cho security context
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }

            filterChain.doFilter(request, response); // mo pass

        } catch (Exception e) {
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

//        if (authHeader != null
//        && SecurityContextHolder.getContext().getAuthentication() == null) {
//            final String token = authHeader.substring(7);
//            //kiem tra token
//            //neu token hop le thi set authentication cho security context
//            //neu token khong hop le thi tra ve loi
//        }
    }
    private boolean isByPassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String ,String>> isBypassTokens = Arrays.asList(
                Pair.of("/api/v1/products" , "GET"),
                Pair.of("/api/v1/categories" , "GET"),
                Pair.of("/api/v1/users/login", "POST"),
                Pair.of("/api/v1/users/register", "POST")
        );
        for (Pair<String , String> isBypassToken : isBypassTokens) {
            if (request.getServletPath().contains(isBypassToken.getLeft()) &&
                    request.getMethod().equals(isBypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
