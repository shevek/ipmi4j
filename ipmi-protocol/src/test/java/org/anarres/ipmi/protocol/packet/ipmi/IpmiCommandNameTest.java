/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.SystemLogChute;
import org.apache.velocity.tools.ToolManager;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is really a code generator, not a test.
 *
 * @author shevek
 */
@Ignore
public class IpmiCommandNameTest {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiCommandNameTest.class);

    @Test
    public void testTemplate() throws Exception {
        ToolManager manager = new ToolManager(true);
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new SystemLogChute());
        engine.init();

        Context context = manager.createContext();
        context.put("values", IpmiCommandName.values());

        URL templateUrl = Resources.getResource(IpmiCommandNameTest.class, "ipmihandler.vm");
        CharSource source = Resources.asCharSource(templateUrl, StandardCharsets.UTF_8);

        File file = new File("IpmiClientCommandHandler.java");
        CharSink sink = Files.asCharSink(file, StandardCharsets.UTF_8);

        try (Reader r = source.openBufferedStream()) {
            try (Writer w = sink.openBufferedStream()) {
                engine.evaluate(context, w, "IpmiClientCommandHandler", r);
            }
        }
    }
}