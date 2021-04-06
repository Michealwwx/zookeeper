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

package org.apache.zookeeper;

import org.apache.yetus.audience.InterfaceAudience;

/**
 * This interface specifies the public interface an event handler class must
 * implement. A ZooKeeper client will get various events from the ZooKeeper
 * server it connects to. An application using such a client handles these
 * events by registering a callback object with the client. The callback object
 * is expected to be an instance of a class that implements Watcher interface.
 *
 */
@InterfaceAudience.Public
public interface Watcher {

    /**
     * This interface defines the possible states an Event may represent
     */
    @InterfaceAudience.Public
    interface Event {

        /**
         * Enumeration of states the ZooKeeper may be at the event
         */
        @InterfaceAudience.Public
        enum KeeperState {
            /** Unused, this state is never generated by the server */
            @Deprecated
            Unknown(-1),

            /** The client is in the disconnected state - it is not connected
             * to any server in the ensemble. */
            Disconnected(0),

            /** Unused, this state is never generated by the server */
            @Deprecated
            NoSyncConnected(1),

            /** The client is in the connected state - it is connected
             * to a server in the ensemble (one of the servers specified
             * in the host connection parameter during ZooKeeper client
             * creation). */
            SyncConnected(3),

            /**
             * Auth failed state
             */
            AuthFailed(4),

            /**
             * The client is connected to a read-only server, that is the
             * server which is not currently connected to the majority.
             * The only operations allowed after receiving this state is
             * read operations.
             * This state is generated for read-only clients only since
             * read/write clients aren't allowed to connect to r/o servers.
             */
            ConnectedReadOnly(5),

            /**
             * SaslAuthenticated: used to notify clients that they are SASL-authenticated,
             * so that they can perform Zookeeper actions with their SASL-authorized permissions.
             */
            SaslAuthenticated(6),

            /** The serving cluster has expired this session. The ZooKeeper
             * client connection (the session) is no longer valid. You must
             * create a new client connection (instantiate a new ZooKeeper
             * instance) if you with to access the ensemble. */
            Expired(-112),

            /**
             * The client has been closed. This state is never generated by
             * the server, but is generated locally when a client calls
             * {@link ZooKeeper#close()} or {@link ZooKeeper#close(int)}
             */
            Closed(7);

            private final int intValue;     // Integer representation of value
            // for sending over wire

            KeeperState(int intValue) {
                this.intValue = intValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public static KeeperState fromInt(int intValue) {
                switch (intValue) {
                case -1:
                    return KeeperState.Unknown;
                case 0:
                    return KeeperState.Disconnected;
                case 1:
                    return KeeperState.NoSyncConnected;
                case 3:
                    return KeeperState.SyncConnected;
                case 4:
                    return KeeperState.AuthFailed;
                case 5:
                    return KeeperState.ConnectedReadOnly;
                case 6:
                    return KeeperState.SaslAuthenticated;
                case -112:
                    return KeeperState.Expired;
                case 7:
                    return KeeperState.Closed;

                default:
                    throw new RuntimeException("Invalid integer value for conversion to KeeperState");
                }
            }
        }

        /**
         * Enumeration of types of events that may occur on the ZooKeeper
         */
        @InterfaceAudience.Public
        enum EventType {
            None(-1),
            NodeCreated(1),
            NodeDeleted(2),
            NodeDataChanged(3),
            NodeChildrenChanged(4),
            DataWatchRemoved(5),
            ChildWatchRemoved(6),
            PersistentWatchRemoved (7);

            private final int intValue;     // Integer representation of value
            // for sending over wire

            EventType(int intValue) {
                this.intValue = intValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public static EventType fromInt(int intValue) {
                switch (intValue) {
                case -1:
                    return EventType.None;
                case 1:
                    return EventType.NodeCreated;
                case 2:
                    return EventType.NodeDeleted;
                case 3:
                    return EventType.NodeDataChanged;
                case 4:
                    return EventType.NodeChildrenChanged;
                case 5:
                    return EventType.DataWatchRemoved;
                case 6:
                    return EventType.ChildWatchRemoved;
                case 7:
                    return EventType.PersistentWatchRemoved;

                default:
                    throw new RuntimeException("Invalid integer value for conversion to EventType");
                }
            }
        }

    }

    /**
     * Enumeration of types of watchers
     */
    @InterfaceAudience.Public
    enum WatcherType {
        Children(1),
        Data(2),
        Any(3);

        // Integer representation of value
        private final int intValue;

        WatcherType(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public static WatcherType fromInt(int intValue) {
            switch (intValue) {
            case 1:
                return WatcherType.Children;
            case 2:
                return WatcherType.Data;
            case 3:
                return WatcherType.Any;

            default:
                throw new RuntimeException("Invalid integer value for conversion to WatcherType");
            }
        }
    }

    void process(WatchedEvent event);

}
