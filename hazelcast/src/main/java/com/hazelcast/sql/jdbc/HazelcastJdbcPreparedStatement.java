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

package com.hazelcast.sql.jdbc;

import com.hazelcast.sql.jdbc.impl.JdbcGateway;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.hazelcast.sql.jdbc.impl.JdbcUtils.unsupported;

/**
 * Prepared statement.
 */
@SuppressWarnings({"RedundantThrows", "checkstyle:MethodCount"})
public class HazelcastJdbcPreparedStatement extends HazelcastJdbcStatement implements PreparedStatement {
    /** SQL statement. */
    private final String sql;

    /** Parameters. */
    private ArrayList<Object> params;

    public HazelcastJdbcPreparedStatement(JdbcGateway gateway, HazelcastJdbcConnection connection, int pageSize, String sql) {
        super(gateway, connection, pageSize, true);

        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        boolean res = execute();

        if (!res) {
            throw new SQLException("Query didn't produce result set.");
        }

        return resultSet;
    }

    @Override
    public int executeUpdate() throws SQLException {
        throw unsupportedUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        execute0(sql, params);

        return true;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        throw notStatement("executeQuery");
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        throw notStatement("executeUpdate");
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        throw notStatement("execute");
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw notStatement("execute");
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw notStatement("execute");
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw notStatement("execute");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw notStatement("executeUpdate");
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw notStatement("executeUpdate");
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw notStatement("executeUpdate");
    }

    @Override
    public void addBatch() throws SQLException {
        throw unsupportedBatch();
    }

    @Override
    public void clearParameters() throws SQLException {
        checkClosed();

        params = null;
    }

    private void setParameter(int index, Object value) throws SQLException {
        checkClosed();

        if (index < 1) {
            throw new SQLException("Parameter index must be positive: " + index);
        }

        // TODO: Validate that the type which is set is the valid one.

        params.set(index - 1, value);
    }

    private List<Object> getParameters(int maxIndex) {
        if (params == null) {
            params = new ArrayList<>(maxIndex);
        }

        // Fill with null arguments for easy replacement in future.
        while (params.size() < maxIndex) {
            params.add(null);
        }

        return params;
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        setParameter(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        setString(parameterIndex, value);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        setDate(parameterIndex, x, null);
    }

    @Override
    public void setDate(int parameterIndex, Date date, Calendar cal) throws SQLException {
        // TODO: How to handle Calendar here?
        java.util.Date date0 = new java.util.Date(date.getTime());

        setParameter(parameterIndex, date0);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        setTime(parameterIndex, x, null);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        // TODO: Implement
        // TODO: How to handle Calendar?
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        setTimestamp(parameterIndex, x, null);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        // TODO: Implement
        // TODO: How to handle Calendar?
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        // TODO: Implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw closedUnsupported("setArray");
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        throw closedUnsupported("setRef");
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw closedUnsupported("setRowId");
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw closedUnsupported("setSQLXML");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw closedUnsupported("setBinaryStream");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw closedUnsupported("setBinaryStream");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw closedUnsupported("setBinaryStream");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw closedUnsupported("setAsciiStream");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw closedUnsupported("setAsciiStream");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw closedUnsupported("setAsciiStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw closedUnsupported("setCharacterStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw closedUnsupported("setCharacterStream");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw closedUnsupported("setCharacterStream");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw closedUnsupported("setNCharacterStream");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw closedUnsupported("setNCharacterStream");
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw closedUnsupported("setUnicodeStream");
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw closedUnsupported("setBlob");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw closedUnsupported("setBlob");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw closedUnsupported("setBlob");
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw closedUnsupported("setClob");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw closedUnsupported("setClob");
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw closedUnsupported("setClob");
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw closedUnsupported("setNClob");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw closedUnsupported("setNClob");
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw closedUnsupported("setNClob");
    }

    private SQLFeatureNotSupportedException closedUnsupported(String method) throws SQLException {
        checkClosed();

        throw unsupported(method + " is not supported.");
    }

    private SQLException notStatement(String methodName) throws SQLException {
        checkClosed();

        throw new SQLException("Method cannot be called on PreparedStatement: " + methodName);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO: Implement
        return null;
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        // TODO: Implement
        return null;
    }
}
