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

package org.eclipse.jetty.ee10.session.hazelcast.client;

import org.eclipse.jetty.ee10.session.AbstractClusteredSessionScavengingTest;
import org.eclipse.jetty.session.SessionDataStoreFactory;
import org.eclipse.jetty.session.test.tools.HazelcastTestHelper;
import org.junit.jupiter.api.AfterEach;

public class ClientSessionScavengingClientTest
    extends AbstractClusteredSessionScavengingTest
{
    HazelcastTestHelper _testHelper = new HazelcastTestHelper(getClass().getSimpleName() + System.nanoTime());

    @Override
    public SessionDataStoreFactory createSessionDataStoreFactory()
    {
        return _testHelper.createSessionDataStoreFactory(true);
    }

    @AfterEach
    public void shutdown()
    {
        _testHelper.tearDown();
    }
}
