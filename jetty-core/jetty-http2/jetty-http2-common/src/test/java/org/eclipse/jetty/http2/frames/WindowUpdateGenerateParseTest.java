//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.http2.frames;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http2.generator.HeaderGenerator;
import org.eclipse.jetty.http2.generator.WindowUpdateGenerator;
import org.eclipse.jetty.http2.parser.Parser;
import org.eclipse.jetty.io.ArrayByteBufferPool;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.RetainableByteBuffer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WindowUpdateGenerateParseTest
{
    private final ByteBufferPool bufferPool = new ArrayByteBufferPool();

    @Test
    public void testGenerateParse() throws Exception
    {
        WindowUpdateGenerator generator = new WindowUpdateGenerator(new HeaderGenerator(bufferPool));

        final List<WindowUpdateFrame> frames = new ArrayList<>();
        Parser parser = new Parser(bufferPool, 8192);
        parser.init(new Parser.Listener()
        {
            @Override
            public void onWindowUpdate(WindowUpdateFrame frame)
            {
                frames.add(frame);
            }
        });

        int streamId = 13;
        int windowUpdate = 17;

        // Iterate a few times to be sure generator and parser are properly reset.
        for (int i = 0; i < 2; ++i)
        {
            RetainableByteBuffer.Mutable accumulator = new RetainableByteBuffer.DynamicCapacity();
            generator.generateWindowUpdate(accumulator, streamId, windowUpdate);

            frames.clear();
            UnknownParseTest.parse(parser, accumulator);
            accumulator.release();
        }

        assertEquals(1, frames.size());
        WindowUpdateFrame frame = frames.get(0);
        assertEquals(streamId, frame.getStreamId());
        assertEquals(windowUpdate, frame.getWindowDelta());
    }

    @Test
    public void testGenerateParseOneByteAtATime() throws Exception
    {
        WindowUpdateGenerator generator = new WindowUpdateGenerator(new HeaderGenerator(bufferPool));

        final List<WindowUpdateFrame> frames = new ArrayList<>();
        Parser parser = new Parser(bufferPool, 8192);
        parser.init(new Parser.Listener()
        {
            @Override
            public void onWindowUpdate(WindowUpdateFrame frame)
            {
                frames.add(frame);
            }
        });

        int streamId = 13;
        int windowUpdate = 17;

        // Iterate a few times to be sure generator and parser are properly reset.
        for (int i = 0; i < 2; ++i)
        {
            RetainableByteBuffer.Mutable accumulator = new RetainableByteBuffer.DynamicCapacity();
            generator.generateWindowUpdate(accumulator, streamId, windowUpdate);

            frames.clear();
            parser.parse(accumulator.getByteBuffer());
            accumulator.release();

            assertEquals(1, frames.size());
            WindowUpdateFrame frame = frames.get(0);
            assertEquals(streamId, frame.getStreamId());
            assertEquals(windowUpdate, frame.getWindowDelta());
        }
    }
}
