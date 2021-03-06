/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.server;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import org.apache.zookeeper.ZKTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerStatsTest extends ZKTestCase {

    private ServerStats.Provider providerMock;

    @BeforeEach
    public void setUp() {
        providerMock = mock(ServerStats.Provider.class);
    }

    @Test
    public void testPacketsMetrics() {
        // Given ...
        ServerStats serverStats = new ServerStats(providerMock);
        int incrementCount = 20;

        // When increment ...
        for (int i = 0; i < incrementCount; i++) {
            serverStats.incrementPacketsSent();
            serverStats.incrementPacketsReceived();
            serverStats.incrementPacketsReceived();
        }

        // Then ...
        assertEquals(incrementCount, serverStats.getPacketsSent());
        assertEquals(incrementCount * 2, serverStats.getPacketsReceived());

        // When reset ...
        serverStats.resetRequestCounters();

        // Then ...
        assertAllPacketsZero(serverStats);

    }

    @Test
    public void testLatencyMetrics() {
        // Given ...
        ServerStats serverStats = new ServerStats(providerMock);

        // When incremented...
        Request fakeRequest = new Request(0, 0, 0, null, null, 0);
        serverStats.updateLatency(fakeRequest, fakeRequest.createTime + 1000);
        serverStats.updateLatency(fakeRequest, fakeRequest.createTime + 2000);

        // Then ...
        assertThat("Max latency check", 2000L, lessThanOrEqualTo(serverStats.getMaxLatency()));
        assertThat("Min latency check", 1000L, lessThanOrEqualTo(serverStats.getMinLatency()));
        assertEquals(1500, serverStats.getAvgLatency(), 200);

        // When reset...
        serverStats.resetLatency();

        // Then ...
        assertAllLatencyZero(serverStats);
    }

    @Test
    public void testFsyncThresholdExceedMetrics() {
        // Given ...
        ServerStats serverStats = new ServerStats(providerMock);
        int incrementCount = 30;

        // When increment ...
        for (int i = 0; i < incrementCount; i++) {
            serverStats.incrementFsyncThresholdExceedCount();
        }

        // Then ...
        assertEquals(incrementCount, serverStats.getFsyncThresholdExceedCount());

        // When reset ...
        serverStats.resetFsyncThresholdExceedCount();

        // Then ...
        assertFsyncThresholdExceedCountZero(serverStats);

    }

    @Test
    public void testReset() {
        // Given ...
        ServerStats serverStats = new ServerStats(providerMock);

        assertAllPacketsZero(serverStats);
        assertAllLatencyZero(serverStats);

        // When ...
        Request fakeRequest = new Request(0, 0, 0, null, null, 0);
        serverStats.incrementPacketsSent();
        serverStats.incrementPacketsReceived();
        serverStats.updateLatency(fakeRequest, fakeRequest.createTime + 1000);

        serverStats.reset();

        // Then ...
        assertAllPacketsZero(serverStats);
        assertAllLatencyZero(serverStats);
    }

    private void assertAllPacketsZero(ServerStats serverStats) {
        assertEquals(0L, serverStats.getPacketsSent());
        assertEquals(0L, serverStats.getPacketsReceived());
    }

    private void assertAllLatencyZero(ServerStats serverStats) {
        assertEquals(0L, serverStats.getMaxLatency());
        assertEquals(0L, serverStats.getMinLatency());
        assertEquals(0, serverStats.getAvgLatency(), 0.00001);
    }

    private void assertFsyncThresholdExceedCountZero(ServerStats serverStats) {
        assertEquals(0L, serverStats.getFsyncThresholdExceedCount());
    }

}
