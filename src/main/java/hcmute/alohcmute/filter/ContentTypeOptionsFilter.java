package hcmute.alohcmute.filter;

import java.io.IOException;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class ContentTypeOptionsFilter implements Filter{
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        chain.doFilter(request, httpResponse);
    }
}

