/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.engine;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiClient {

    @PostConstruct
    public abstract void start() throws IOException, InterruptedException;

    @PreDestroy
    public abstract void stop() throws IOException, InterruptedException;
}
