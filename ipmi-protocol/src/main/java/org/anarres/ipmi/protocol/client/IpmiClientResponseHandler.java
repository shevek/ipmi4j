/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public interface IpmiClientResponseHandler {

    public static class Ignore implements IpmiClientResponseHandler {

        @Override
        public void handleResponse(IpmiResponse response) {
        }
    }
    public static final IpmiClientResponseHandler IGNORE = new Ignore();

    public static class Log implements IpmiClientResponseHandler {

        private static final Logger LOG = LoggerFactory.getLogger(Log.class);

        @Override
        public void handleResponse(IpmiResponse response) {
            LOG.info(String.valueOf(response));
        }
    }
    public static final IpmiClientResponseHandler LOG = new Log();

    public void handleResponse(@Nonnull IpmiResponse response);
}
