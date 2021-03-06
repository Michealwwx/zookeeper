#  Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at

#     http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from distutils.core import setup, Extension
import os

zookeeper_basedir = "../../"

zookeeper_macros = [("THREADED", None)]

# Assume the C extension includes OpenSSL support unless told
# otherwise.
if not os.environ.get("ZKPYTHON_NO_SSL"):
    zookeeper_macros.append(("HAVE_OPENSSL_H", True))

zookeepermodule = Extension("zookeeper",
                            sources=["src/c/zookeeper.c"],
                            define_macros=zookeeper_macros,
                            include_dirs=[zookeeper_basedir + "/zookeeper-client/zookeeper-client-c/include",
                                          zookeeper_basedir + "/zookeeper-client/zookeeper-client-c/target/c",
                                          zookeeper_basedir + "/zookeeper-client/zookeeper-client-c/generated"],
                            libraries=["zookeeper_mt"],
                            library_dirs=[zookeeper_basedir + "/zookeeper-client/zookeeper-client-c/.libs/",
                                          zookeeper_basedir + "/zookeeper-client/zookeeper-client-c/target/c/.libs/",
                                          zookeeper_basedir + "/build/test/test-cppunit/.libs",
                                          "/usr/local/lib"
                                          ])

setup( name="ZooKeeper",
       version = "0.4",
       description = "ZooKeeper Python bindings",
       ext_modules=[zookeepermodule] )
