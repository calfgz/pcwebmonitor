package org.jwebap.plugin.tracer.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.Calendar;

import org.jwebap.core.Trace;

/**
 * 用于检测SQL执行轨迹的PreparedStatement代理类
 * 
 * @author leadyu
 */
public class ProxyPreparedStatement extends ProxyStatement
		implements PreparedStatement {

	protected PreparedStatement _stmt = null;

	private String _sql=null;
	
	private int _batchCount=0;

	public ProxyPreparedStatement(ProxyConnection conn,
			PreparedStatement stmt) {
		super(conn, stmt);
		_stmt = stmt;
	}

	public ProxyPreparedStatement(ProxyConnection conn,
			PreparedStatement stmt, String sql) {
		this(conn, stmt);
		_sql=sql;

	}

	private void endSQL() {
		Trace[] ts = getChildTraces();
		for (int i = 0; i < ts.length; i++) {
			ts[i].inActive();
		}

	}

	public ResultSet executeQuery() throws SQLException {
		checkOpen();
		long startTime = System.currentTimeMillis();
		try {
			//增加SQL轨迹
			Trace child = new Trace(this);
			child.setContent(_sql);
			ResultSet rs = _stmt.executeQuery();
			//获取数量
			int count = 0;
			try {
				if(rs.last()){
					count = rs.getRow();
					rs.beforeFirst();
				}
				child.setCount(count);
			} catch (Exception e) {
				//System.err.println("PC Web Monitor ERROR SQL: " + _sql + " 统计返回数量异常.");
				child.setCount(-1);
			}
			
			return rs;
		} catch (SQLException e) {
			throw e;
		} finally {
			endSQL();
			long activeTime = System.currentTimeMillis() - startTime;
			JdbcComponent.frequencyAnalyser.inactiveProcess(_sql, activeTime);
		}
	}


	public void addBatch() throws SQLException {
		checkOpen();
		
		try {
			_stmt.addBatch();
		} catch (SQLException e) {
			throw e;
		}finally{
			_batchCount++;
			//记录sql内容为批量更新
			_sql="(batch update "+_batchCount+"): ";
		}
	}
	
	public int executeUpdate() throws SQLException {
		checkOpen();
		long startTime = System.currentTimeMillis();
		try {
			//增加SQL轨迹
			Trace child = new Trace(this);
			child.setContent(_sql);
			//获取数量
			int count = _stmt.executeUpdate();
			child.setCount(count);
			
			return count;
		} catch (SQLException e) {
			throw e;
		} finally {
			endSQL();
			long activeTime = System.currentTimeMillis() - startTime;
			JdbcComponent.frequencyAnalyser.inactiveProcess(_sql, activeTime);
		}
	}

	public boolean execute() throws SQLException {
		checkOpen();
		long startTime = System.currentTimeMillis();
		try {
			//增加SQL轨迹
			Trace child = new Trace(this);
			child.setContent(_sql);
			
			boolean result = _stmt.execute();
			int count = result == true ? 1 : 0;
			child.setCount(count);
			
			return result;
		} catch (SQLException e) {
			throw e;
		} finally {
			endSQL();
			long activeTime = System.currentTimeMillis() - startTime;
			JdbcComponent.frequencyAnalyser.inactiveProcess(_sql, activeTime);
		}
	}

	public void close() throws SQLException {
		super.close();
		this._stmt = null;

	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		checkOpen();
		try {
			_stmt.setNull(parameterIndex, sqlType);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		checkOpen();
		try {
			_stmt.setBoolean(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		checkOpen();
		try {
			_stmt.setByte(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		checkOpen();
		try {
			_stmt.setShort(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		checkOpen();
		try {
			_stmt.setInt(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		checkOpen();
		try {
			_stmt.setLong(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		checkOpen();
		try {
			_stmt.setFloat(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		checkOpen();
		try {
			_stmt.setDouble(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setBigDecimal(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		checkOpen();
		try {
			_stmt.setString(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		checkOpen();
		try {
			_stmt.setBytes(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setDate(int parameterIndex, java.sql.Date x)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setDate(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setTime(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setAsciiStream(int parameterIndex, java.io.InputStream x,
			int length) throws SQLException {
		checkOpen();
		try {
			_stmt.setAsciiStream(parameterIndex, x, length);
		} catch (SQLException e) {
			throw e;
		}
	}

	/** @deprecated */
	public void setUnicodeStream(int parameterIndex, java.io.InputStream x,
			int length) throws SQLException {
		checkOpen();
		try {
			_stmt.setUnicodeStream(parameterIndex, x, length);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setBinaryStream(int parameterIndex, java.io.InputStream x,
			int length) throws SQLException {
		checkOpen();
		try {
			_stmt.setBinaryStream(parameterIndex, x, length);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void clearParameters() throws SQLException {
		checkOpen();
		try {
			_stmt.clearParameters();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		checkOpen();
		try {
			_stmt.setObject(parameterIndex, x, targetSqlType, scale);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setObject(parameterIndex, x, targetSqlType);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		checkOpen();
		try {
			_stmt.setObject(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setCharacterStream(int parameterIndex, java.io.Reader reader,
			int length) throws SQLException {
		checkOpen();
		try {
			_stmt.setCharacterStream(parameterIndex, reader, length);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setRef(int i, Ref x) throws SQLException {
		checkOpen();
		try {
			_stmt.setRef(i, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setBlob(int i, Blob x) throws SQLException {
		checkOpen();
		try {
			_stmt.setBlob(i, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setClob(int i, Clob x) throws SQLException {
		checkOpen();
		try {
			_stmt.setClob(i, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setArray(int i, Array x) throws SQLException {
		checkOpen();
		try {
			_stmt.setArray(i, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		checkOpen();
		try {
			return _stmt.getMetaData();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setDate(parameterIndex, x, cal);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setTime(int parameterIndex, java.sql.Time x, Calendar cal)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setTime(parameterIndex, x, cal);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setTimestamp(int parameterIndex, java.sql.Timestamp x,
			Calendar cal) throws SQLException {
		checkOpen();
		try {
			_stmt.setTimestamp(parameterIndex, x, cal);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		checkOpen();
		try {
			_stmt.setNull(paramIndex, sqlType, typeName);
		} catch (SQLException e) {
			throw e;
		}
	}

	// ------------------- JDBC 3.0 -----------------------------------------

	public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
		checkOpen();
		try {
			_stmt.setURL(parameterIndex, x);
		} catch (SQLException e) {
			throw e;
		}
	}

	public java.sql.ParameterMetaData getParameterMetaData()
			throws SQLException {
		checkOpen();
		try {
			return _stmt.getParameterMetaData();
		} catch (SQLException e) {
			throw e;
		}
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	public boolean isWrapperFor(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object unwrap(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
