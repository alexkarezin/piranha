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

import jakarta.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultHttpSessionManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpSessionManagerTest {

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
        assertEquals(sessionManager.getDefaultSessionTrackingModes(), 
                sessionManager.getEffectiveSessionTrackingModes());
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(request, session.getId()));
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
}