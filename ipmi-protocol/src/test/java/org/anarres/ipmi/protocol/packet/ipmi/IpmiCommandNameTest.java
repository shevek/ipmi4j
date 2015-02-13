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
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpMessageType;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.SystemLogChute;
import org.apache.velocity.tools.ToolManager;
import org.junit.Before;
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
    private final ToolManager manager = new ToolManager(true);
    private final VelocityEngine engine = new VelocityEngine();

    @Before
    public void setUp() {
        engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new SystemLogChute());
        engine.init();
    }

    private void process(Context context, String templateName, String targetPath) throws IOException {
        URL templateUrl = Resources.getResource(IpmiCommandNameTest.class, templateName);
        CharSource source = Resources.asCharSource(templateUrl, StandardCharsets.UTF_8);

        File file = new File(targetPath);
        CharSink sink = Files.asCharSink(file, StandardCharsets.UTF_8);

        try (Reader r = source.openBufferedStream()) {
            try (Writer w = sink.openBufferedStream()) {
                engine.evaluate(context, w, file.getName(), r);
            }
        }
    }

    @Test
    public void testIpmiCommandHandler() throws Exception {
        Context context = manager.createContext();
        context.put("values", IpmiCommandName.values());
        process(context, "ipmihandler.vm", "IpmiClientIpmiCommandHandler.java");
    }

    @Test
    public void testAsfDataHandler() throws Exception {
        Context context = manager.createContext();
        context.put("values", AsfRmcpMessageType.values());
        process(context, "asfhandler.vm", "IpmiClientAsfMessageHandler.java");
    }
}