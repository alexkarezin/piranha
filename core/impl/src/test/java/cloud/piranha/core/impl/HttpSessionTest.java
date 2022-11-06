/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import static jakarta.servlet.SessionTrackingMode.SSL;
import static jakarta.servlet.SessionTrackingMode.URL;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpSession API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpSessionTest extends cloud.piranha.core.tests.HttpSessionTest {

    @Override
    protected WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }

    @Override
    protected WebApplicationRequest createWebApplicationRequest() {
        return new DefaultWebApplicationRequest();
    }

    @Override
    protected WebApplicationResponse createWebApplicationResponse() {
        return new DefaultWebApplicationResponse();
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCreateListener() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(TestCreateListenerHttpSessionListener.class));
    }

    /**
     * Test createSession method.
     */
    @Test
    void testCreateSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(assertThrows(NullPointerException.class,
                () -> sessionManager.createSession(null)));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeRedirectURL(null, "test"));
    }

    /**
     * Test encodeURL method.
     */
    @Test
    void testEncodeURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeURL(null, "test"));
    }

    /**
     * Test getComment method.
     */
    @Test
    void testGetComment() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setComment("COMMENT");
        assertNull(sessionManager.getComment());
    }

    /**
     * Test getCookies method.
     */
    /*

      REVIEW FOR SERVLET 6

    @Test
    void testGetCookies() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApp);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);

        sessionManager.setComment("Comment");
        sessionManager.setDomain("domain");
        sessionManager.setHttpOnly(true);
        sessionManager.setName("SessionCookie");
        sessionManager.setMaxAge(100);
        sessionManager.setPath("/context");
        sessionManager.setSecure(true);

        sessionManager.createSession(request);

        Cookie sessionCookie = response.getCookies()
                .stream()
                .filter(cookie -> "SessionCookie".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(sessionCookie);
        assertEquals(sessionManager.getComment(), sessionCookie.getComment());
        assertEquals(sessionManager.getDomain(), sessionCookie.getDomain());
        assertTrue(sessionCookie.isHttpOnly());
        assertEquals(sessionManager.getMaxAge(), sessionCookie.getMaxAge());
        assertEquals(sessionManager.getPath(), sessionCookie.getPath());
        assertTrue(sessionCookie.getSecure());
    }
    */

    /**
     * Test getDomain method.
     */
    @Test
    void testGetDomain() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setDomain("domain");
        assertEquals("domain", sessionManager.getDomain());
    }

    /**
     * Test getEffectivfeSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.getDefaultSessionTrackingModes(), sessionManager.getEffectiveSessionTrackingModes());
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(SessionTrackingMode.URL);
        webApp.setSessionTrackingModes(trackingModes);
        assertTrue(webApp.getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL));
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(SessionTrackingMode.URL);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApp.setSessionTrackingModes(trackingModes)));
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    void testGetMaxAge() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertEquals(-1, sessionManager.getMaxAge());
        sessionManager.setMaxAge(60);
        assertEquals(60, sessionManager.getMaxAge());
    }

    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setName("JSESSIONID");
        assertEquals("JSESSIONID", sessionManager.getName());
    }

    /**
     * Test getPath method.
     */
    @Test
    void testGetPath() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setPath("/");
        assertEquals("/", sessionManager.getPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(assertThrows(NullPointerException.class,
                () -> sessionManager.getSession(null, null)));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApp);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(request, session.getId()));
    }

    /**
     * Test getSession.
     *
     * @throws Exception
     */
    @Test
    void testGetSession3() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic = webApp.addServlet("session", TestGetSession3Servlet.class);
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApp.initialize();
        webApp.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/session");
        TestWebApplicationResponse response = new TestWebApplicationResponse();

        webApp.service(request, response);

        assertNotNull(response.getResponseBytes());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession4() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession5() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession6() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    void testGetSessionCookieConfig() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(sessionManager.getSessionCookieConfig());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    void testGetSessionManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setHttpSessionManager(null);
        assertNull(webApp.getManager().getHttpSessionManager());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    void testGetSessionManager2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getManager().getHttpSessionManager());
    }

    /**
     * Test getSessionTimeout method.
     */
    @Test
    void testGetSessionTimeout() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertEquals(10, sessionManager.getSessionTimeout());
        sessionManager.setSessionTimeout(5);
        assertEquals(5, sessionManager.getSessionTimeout());
    }

    /**
     * Test invalidate method.
     *
     * @throws IllegalStateException when the session is invalid.
     */
    @Test
    void testInvalidate2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.invalidate();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> session.setAttribute("TEST", "TEST")));
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    void testIsHttpOnly() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertFalse(sessionManager.isHttpOnly());
        sessionManager.setHttpOnly(true);
        assertTrue(sessionManager.isHttpOnly());
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertFalse(sessionManager.isSecure());
        sessionManager.setSecure(true);
        assertTrue(sessionManager.isSecure());
    }

    /**
     * Test setComment.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetComment() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApplication.addServlet("TestSetCommentServlet", new TestSetCommentServlet());
        webApplication.addServletMapping("TestSetCommnetrServlet", "/*");
        webApplication.addListener(new TestSetCommentListener());
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
        } catch (ServletException se) {
            fail();
        }
        webApplication.stop();
    }

    /**
     * Test setComment.
     */
    @Test
    void testSetComment2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setComment("MY COMMENT")));
        webApplication.stop();
    }

    /**
     * Test setRequestedSessionIdFromCookie method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRequestedSessionIdFromCookie() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
        request.setRequestedSessionIdFromCookie(true);
        assertTrue(request.isRequestedSessionIdFromCookie());
        request.close();
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRequestedSessionIdFromURL() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
        request.setRequestedSessionIdFromURL(true);
        assertTrue(request.isRequestedSessionIdFromURL());
        request.close();
    }

    /**
     * Test setSessionTimeout method.
     */
    @Test
    void testSetSessionTimeout() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSessionTimeout(50);
        assertEquals(50, webApp.getSessionTimeout());
    }

    /**
     * Test setSessionTimeout method
     */
    @Test
    void testSessionTimeout2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApp.setSessionTimeout(50)));
    }

    /**
     * Test setSessionTrackingModes method.
     */
    @Test
    void testSetSessionTrackingModes() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();

        EnumSet<SessionTrackingMode> sslAndUrl = EnumSet.of(SSL, URL);
        assertNotNull(assertThrows(IllegalArgumentException.class,
                () -> sessionManager.setSessionTrackingModes(sslAndUrl)));

        EnumSet<SessionTrackingMode> sslAndCookie = EnumSet.of(COOKIE, SSL);
        assertNotNull(assertThrows(IllegalArgumentException.class,
                () -> sessionManager.setSessionTrackingModes(sslAndCookie)));
    }

    /**
     * Test HttpSessionListener to validate createListener was called.
     */
    public static class TestCreateListenerHttpSessionListener implements HttpSessionListener {

        /**
         * Constructor.
         */
        public TestCreateListenerHttpSessionListener() {
        }
    }

    /**
     * Test HttpServlet to validate getSession works.
     */
    public static class TestGetSession3Servlet extends HttpServlet {

        /**
         * Processes the request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/plain");
            try (PrintWriter out = response.getWriter()) {
                if (request.isRequestedSessionIdValid()) {
                    HttpSession session = request.getSession(false);
                    out.println("Session is " + session);
                    if (session == null) {
                        session = request.getSession();
                        out.println("Session is " + session);
                    }
                } else {
                    HttpSession session = request.getSession();
                    out.println("Session is " + session + ", from request");
                }
            }
        }

        /**
         * Handles the GET request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            processRequest(request, response);
        }

        /**
         * Handles the POST request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            processRequest(request, response);
        }
    }

    /**
     * Test ServletContextListener that sets the comment of the session cookie
     * config.
     */
    public static class TestSetCommentListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent event) {
            event.getServletContext().getSessionCookieConfig().setComment("MY COMMENT");
        }
    }

    /**
     * Test HttpServlet to validate the servlet context listener can change
     * session cookie config values.
     */
    public static class TestSetCommentServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

            if (!request.getServletContext().getSessionCookieConfig()
                    .getComment().equals("MY COMMENT")) {
                throw new ServletException("ServletContextListener did not work");
            }
        }
    }
}
