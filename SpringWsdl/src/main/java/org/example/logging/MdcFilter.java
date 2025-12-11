package org.example.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

/**
 * Populate MDC with request-scoped values so Log4j2 pattern can include them.
 */
@Component
public class MdcFilter implements Filter {

    private static final org.apache.commons.logging.Log logger =
        org.apache.commons.logging.LogFactory.getLog(MdcFilter.class);

    public static final String MDC_REQUEST_ID = "requestId";
    public static final String MDC_USER = "user";
    public static final String MDC_FUNCTION = "function";
    public static final String MDC_TIMESTAMP = "timestamp";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestId = req.getHeader("X-Request-Id");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        String user = req.getHeader("X-User");
        if (user == null) {
            user = "anonymous";
        }

        MDC.put(MDC_REQUEST_ID, requestId);
        MDC.put(MDC_USER, user);
        MDC.put(MDC_TIMESTAMP, Instant.now().toString());

        try {
            res.setHeader("X-Request-Id", requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_FUNCTION);
            MDC.remove(MDC_REQUEST_ID);
            MDC.remove(MDC_USER);
            MDC.remove(MDC_TIMESTAMP);
        }
    }

    @Override
    public void destroy() {
        // no-op
    }
}
