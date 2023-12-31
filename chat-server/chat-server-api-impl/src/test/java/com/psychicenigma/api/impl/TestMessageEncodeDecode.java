package com.psychicenigma.api.impl;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.psychicenigma.api.HelloService;
import com.psychicenigma.api.impl.model.Message;
import com.psychicenigma.api.impl.websocket.MessageDecoder;
import com.psychicenigma.api.impl.websocket.MessageEncoder;
import junit.framework.TestCase;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.ApplicationParameter;
import org.apache.catalina.startup.Tomcat;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.context.ContextLoaderListener;

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;

/**
 * @author Olivier Lamy
 */
@RunWith( JUnit4.class )
public class TestMessageEncodeDecode
    extends TestCase
{

    @Before
    public void startTomcat()
        throws Exception
    {
    }

    @After
    public void stopTomcat()
        throws Exception
    {
    }

    @Test
    public void testEncodeDecode() throws EncodeException, DecodeException {
        Message msg = new Message();
        msg.setFrom("X");
        msg.setTo("Y");
        msg.setContent("Hello world!");
        MessageEncoder messageEncoder = new MessageEncoder();
        String encodedMessage = messageEncoder.encode(msg);
        System.out.println(encodedMessage);
        MessageDecoder messageDecoder = new MessageDecoder();
        Message decodedMsg = messageDecoder.decode(encodedMessage);
        assert(decodedMsg.equals(msg) && msg.equals(decodedMsg));
    }
}
