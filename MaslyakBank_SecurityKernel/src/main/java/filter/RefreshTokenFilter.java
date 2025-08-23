package filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import system.RefreshTokenProvider;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if ("/maslyakbank/tokenmanagment/token/refresh".equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod())) {

            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

            wrappedRequest.getInputStream().readAllBytes();

            String body = new String(wrappedRequest.getContentAsByteArray(), request.getCharacterEncoding());

            String refreshToken = null;
            if (!body.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    refreshToken = mapper.readTree(body).get("refresh").asText();
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            if (!refreshTokenProvider.isValid(refreshToken)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            chain.doFilter(wrappedRequest, response);
            return;
        }

        chain.doFilter(request, response);
    }


}
