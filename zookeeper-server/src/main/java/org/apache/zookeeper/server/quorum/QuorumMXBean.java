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

package org.apache.zookeeper.server.quorum;

/**
 * An MBean representing a zookeeper cluster nodes (aka quorum peers)
 */
public interface QuorumMXBean {

    /**
     * @return the name of the quorum
     */
    String getName();

    /**
     * @return configured number of peers in the quorum
     */
    int getQuorumSize();

    /**
     * @return the number of ticks that the initial synchronization phase can take
     */
    int getInitLimit();

    /**
     * @return the number of ticks that can pass between sending a request and getting an acknowledgment
     */
    int getSyncLimit();

    /**
     * @param initLimit the number of ticks that the initial synchronization phase can take
     */
    void setInitLimit(int initLimit);

    /**
     * @param syncLimit the number of ticks that can pass between sending a request and getting an acknowledgment
     */
    void setSyncLimit(int syncLimit);

    /**
     * @return SSL communication between quorum members required
     */
    boolean isSslQuorum();

    /**
     * @return SSL communication between quorum members enabled
     */
    boolean isPortUnification();

    /**
     * @return Observer Leader Election Reconnect Delay time in MS
     */
    long getObserverElectionDelayMS();

    /**
     * Set the Observer Leader Election Reconnect Delay time in MS
     */
    void setObserverElectionDelayMS(long delayMS);

    boolean getDigestEnabled();

    void disableDigest();
}
