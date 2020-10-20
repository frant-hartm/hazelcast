/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.jdbc.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.sql.SqlColumnMetadata;
import com.hazelcast.sql.SqlColumnType;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlRowMetadata;
import com.hazelcast.sql.SqlStatement;
import com.hazelcast.sql.impl.client.SqlClientResult;

import java.util.Iterator;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Gateway for IO communication between JDBC driver and the cluster.
 */
public class JdbcGateway {
    /** Client member responsible for JDBC communication. */
    private final HazelcastInstance client;

    /** Close the gateway. */
    private boolean closed;

    public JdbcGateway(HazelcastInstance client) {
        this.client = client;
    }

    public JdbcCursor execute(String sql, List<Object> args, int pageSize, long timeout) {
        SqlStatement query = new SqlStatement(sql).setParameters(args).setCursorBufferSize(pageSize).setTimeoutMillis(timeout);

        SqlClientResult cursor = (SqlClientResult) client.getSql().execute(query);
        Iterator<SqlRow> iterator;
        if (cursor.updateCount() == 0) {
            InternalSerializationService ss = new DefaultSerializationServiceBuilder().build();

            cursor = new SqlClientResult(cursor.getService(), cursor.getConnection(), cursor.getQueryId(),
                    new SqlRowMetadata(singletonList(new SqlColumnMetadata("", SqlColumnType.INTEGER))),
                    singletonList(singletonList(ss.toData(0))), true, Integer.MAX_VALUE, -1
            );
        }
        iterator = cursor.iterator();

        return new JdbcCursor(cursor, iterator);
    }

    public void close() {
        if (!closed) {
            client.shutdown();

            closed = true;
        }
    }
}
