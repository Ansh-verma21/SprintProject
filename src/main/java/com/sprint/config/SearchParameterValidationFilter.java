package com.sprint.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SearchParameterValidationFilter implements Filter {

    private static final String SEARCH_PATH_FRAGMENT = "/search/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        // Check if this is a findByFirstNameAndLastName search request
        if (httpRequest.getRequestURI().contains("/customers/search/findByFirstNameAndLastName")) {
            String firstName = httpRequest.getParameter("firstName");
            String lastName = httpRequest.getParameter("lastName");

            // Validate both parameters are present and not blank
            if (firstName == null || firstName.trim().isEmpty() || 
                lastName == null || lastName.trim().isEmpty()) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\":\"Both firstName and lastName parameters are required\"}");
                return;
            }
        }

        chain.doFilter(request, responseWrapper);

        if ("GET".equalsIgnoreCase(httpRequest.getMethod())
                && httpRequest.getRequestURI().contains(SEARCH_PATH_FRAGMENT)
                && responseWrapper.getStatus() == HttpServletResponse.SC_OK) {
            byte[] body = responseWrapper.getContentAsByteArray();
            if (body.length > 0) {
                JsonNode root = objectMapper.readTree(new String(body, StandardCharsets.UTF_8));
                JsonNode totalElements = root.path("page").path("totalElements");
                boolean emptyPage = totalElements.isNumber() && totalElements.asLong() == 0L;
                boolean emptyCollection = root.path("content").isArray()
                        && root.path("content").size() > 0
                        && root.path("content").get(0).path("value").isArray()
                        && root.path("content").get(0).path("value").size() == 0;
                if (emptyPage || emptyCollection) {
                    responseWrapper.resetBuffer();
                    responseWrapper.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    responseWrapper.setContentType("application/json");
                    responseWrapper.getWriter().write("{\"error\":\"Not found\",\"message\":\"No results found for the given value\"}");
                }
            }
        }

        responseWrapper.copyBodyToResponse();
    }
}
