/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.httpclient.util.URIUtil;
import org.eclipse.smarthome.config.core.ConfigConstants;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.nest.NestBindingConstants;
import org.openhab.binding.nest.config.NestBridgeConfiguration;
import org.openhab.binding.nest.internal.data.AccessTokenData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Keeps track of the access token, refreshing it if needed.
 *
 * @author David Bennett - Initial contribution
 */
public class NestAccessToken {
    private Logger logger = LoggerFactory.getLogger(NestAccessToken.class);
    private NestBridgeConfiguration config;
    private String accessToken;
    private String folderName;

    /**
     * Create the helper class for the nest access token. Also creates the folder
     * to put the access token data in if it does not already exist.
     *
     * @param config The configuration to use for the token
     */
    public NestAccessToken(NestBridgeConfiguration config) {
        this.config = config;
        this.folderName = ConfigConstants.getUserDataFolder() + "/" + NestBindingConstants.BINDING_ID;
        File folder = new File(folderName);
        if (!folder.exists()) {
            logger.debug("Creating directory {}", folderName);
            folder.mkdirs();
        }
    }

    /**
     * Get the current access token, refreshing if needed. Also reads it from the disk
     * if it is stored there.
     */
    public String getAccessToken() throws IOException {
        if (config.accessToken == null) {
            // See if it is written to disk.
            File file = new File(this.folderName, "accessToken.txt");
            if (file.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String line = reader.readLine();
                    reader.close();
                    if (line != null && line.length() > 0) {
                        this.accessToken = line;
                    }
                } catch (IOException e) {
                    logger.error("Error reading access token file {}", file, e);
                }
            }
            refreshAccessToken();
        } else {
            accessToken = config.accessToken;
        }
        return accessToken;
    }

    private void refreshAccessToken() throws IOException {
        try {
            StringBuilder stuff = new StringBuilder().append("client_id=").append(URIUtil.encodeQuery(config.clientId))
                    .append("&client_secret=").append(URIUtil.encodeQuery(config.clientSecret)).append("&code=")
                    .append(config.pincode).append("&grant_type=authorization_code");
            logger.info("Result " + stuff.toString());
            InputStream stream = new ByteArrayInputStream(stuff.toString().getBytes(StandardCharsets.UTF_8));
            String result = HttpUtil.executeUrl("POST", NestBindingConstants.NEST_ACCESS_TOKEN_URL, stream,
                    "application/x-www-form-urlencoded", 10000);
            logger.info("Result " + result);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            AccessTokenData data = gson.fromJson(result, AccessTokenData.class);
            accessToken = data.getAccessToken();
            logger.info("Access token " + accessToken);
            logger.info("Expiration Time " + data.getExpiresIn());
            // Write the token to a file so we can reload it later.
            File file = new File(this.folderName, "accessToken.txt");
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write(accessToken);
                writer.close();
            } catch (IOException e) {
                logger.error("Error reading access token file {}", file, e);
            }
        } catch (IOException e) {
            logger.error("Unable to get the nest access token ", e);
            throw e;
        }
    }
}
