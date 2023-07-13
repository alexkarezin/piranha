/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.feature.https;

import cloud.piranha.feature.api.Feature;
import cloud.piranha.feature.api.FeatureManager;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.reflect.InvocationTargetException;

/**
 * The HTTPS feature that exposes an HTTPS endpoint.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpsFeature implements Feature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpsFeature.class.getName());
    
    /**
     * Stores the feature manager.
     */
    private FeatureManager featureManager;

    /**
     * Stores the HTTPS server.
     */
    private HttpServer httpsServer;

    /**
     * Stores the HTTPS server class.
     */
    private String httpsServerClass = DefaultHttpServer.class.getName();

    /**
     * Stores the HTTPS port.
     */
    private int port = 8043;

    @Override
    public void destroy() {
        httpsServer = null;
    }

    @Override
    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    /**
     * Get the HTTPS server.
     *
     * @return the HTTPS server.
     */
    public HttpServer getHttpsServer() {
        return httpsServer;
    }

    /**
     * Get the HTTPS server class.
     *
     * @return the HTTPS server class.
     */
    public String getHttpsServerClass() {
        return httpsServerClass;
    }

    /**
     * Get the port.
     *
     * @return the port.
     */
    public int getPort() {
        return port;
    }

    @Override
    public void init() {
        if (port > 0) {
            try {
                httpsServer = (HttpServer) Class.forName(httpsServerClass)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException
                    | IllegalArgumentException | InstantiationException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException t) {
                LOGGER.log(ERROR, "Unable to construct HTTP server", t);
            }
            if (httpsServer != null) {
                httpsServer.setServerPort(port);
                httpsServer.setSSL(true);
            }
        }
    }

    @Override
    public void setFeatureManager(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    /**
     * Set the HTTPS server.
     *
     * @param httpsServer the HTTPS server.
     */
    public void setHttpsServer(HttpServer httpsServer) {
        this.httpsServer = httpsServer;
    }

    /**
     * Set the HTTP server class.
     *
     * @param httpsServerClass the HTTP server class.
     */
    public void setHttpsServerClass(String httpsServerClass) {
        if (httpsServerClass != null) {
            this.httpsServerClass = httpsServerClass;
        } else {
            this.httpsServerClass = DefaultHttpServer.class.getName();
        }
    }

    /**
     * Set the port.
     *
     * @param port the port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        if (httpsServer != null) {
            httpsServer.start();
        }
    }

    @Override
    public void stop() {
        if (httpsServer != null) {
            httpsServer.stop();
        }
    }
}