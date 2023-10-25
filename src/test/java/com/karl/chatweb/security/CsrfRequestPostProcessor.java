package com.karl.chatweb.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.support.WebTestUtils;
import org.springframework.security.web.csrf.*;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Base64;
import java.util.Objects;

/**
 * A request post processor to add <em>csrf</em> information.
 */
public final class CsrfRequestPostProcessor implements RequestPostProcessor {
    private static final byte[] INVALID_TOKEN_BYTES = new byte[]{1, 1, 1, 96, 99, 98};
    private static final String INVALID_TOKEN_VALUE;
    private boolean asHeader;
    private boolean useInvalidToken;

    private CsrfRequestPostProcessor() {
    }

    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        CsrfTokenRepository repository = WebTestUtils.getCsrfTokenRepository(request);
        // This line got the default xorhandler
        //CsrfTokenRequestHandler handler = WebTestUtils.getCsrfTokenRequestHandler(request);
        CsrfTokenRequestHandler handler = new CsrfTokenRequestAttributeHandler();
        if (!(repository instanceof CsrfRequestPostProcessor.TestCsrfTokenRepository)) {
            repository = new CsrfRequestPostProcessor.TestCsrfTokenRepository(new HttpSessionCsrfTokenRepository());
            WebTestUtils.setCsrfTokenRepository(request, (CsrfTokenRepository)repository);
        }

        CsrfRequestPostProcessor.TestCsrfTokenRepository.enable(request);
        MockHttpServletResponse response = new MockHttpServletResponse();
        DeferredCsrfToken deferredCsrfToken = ((CsrfTokenRepository)repository).loadDeferredToken(request, response);
        Objects.requireNonNull(deferredCsrfToken);
        handler.handle(request, response, deferredCsrfToken::get);
        CsrfToken token = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
        String tokenValue = this.useInvalidToken ? INVALID_TOKEN_VALUE : token.getToken();
        if (this.asHeader) {
            request.addHeader(token.getHeaderName(), tokenValue);
        } else {
            request.setParameter(token.getParameterName(), tokenValue);
        }

        return request;
    }

    public static CsrfRequestPostProcessor csrf() {
        return new CsrfRequestPostProcessor();
    }


    public CsrfRequestPostProcessor asHeader() {
        this.asHeader = true;
        return this;
    }

    public CsrfRequestPostProcessor useInvalidToken() {
        this.useInvalidToken = true;
        return this;
    }

    static {
        INVALID_TOKEN_VALUE = Base64.getEncoder().encodeToString(INVALID_TOKEN_BYTES);
    }

    static class TestCsrfTokenRepository implements CsrfTokenRepository {
        static final String TOKEN_ATTR_NAME = CsrfRequestPostProcessor.TestCsrfTokenRepository.class.getName().concat(".TOKEN");
        static final String ENABLED_ATTR_NAME = CsrfRequestPostProcessor.TestCsrfTokenRepository.class.getName().concat(".ENABLED");
        private final CsrfTokenRepository delegate;

        TestCsrfTokenRepository(CsrfTokenRepository delegate) {
            this.delegate = delegate;
        }

        public CsrfToken generateToken(HttpServletRequest request) {
            return this.delegate.generateToken(request);
        }

        public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
            if (this.isEnabled(request)) {
                request.setAttribute(TOKEN_ATTR_NAME, token);
            } else {
                this.delegate.saveToken(token, request, response);
            }

        }

        public CsrfToken loadToken(HttpServletRequest request) {
            return this.isEnabled(request) ? (CsrfToken)request.getAttribute(TOKEN_ATTR_NAME) : this.delegate.loadToken(request);
        }

        static void enable(HttpServletRequest request) {
            request.setAttribute(ENABLED_ATTR_NAME, Boolean.TRUE);
        }

        boolean isEnabled(HttpServletRequest request) {
            return Boolean.TRUE.equals(request.getAttribute(ENABLED_ATTR_NAME));
        }
    }
}