package com.blue.database;

import java.io.Serializable;
import java.sql.*;
import java.sql.PreparedStatement;

public class DatabaseBean implements Serializable {
	private static final String jdbcDriverClass ="org.postgresql.Driver";
	private static final String jdbcDatabaseURL ="jdbc:postgresql://localhost:5433/petroleum";//petroleum
        private static final String username ="root";//postgres
        private static final String password ="admin12345";
	private Connection conn;

	public DatabaseBean() throws Exception {
		try {
			Class.forName(jdbcDriverClass);
			connect();
		} catch(SQLException ex) {
			cleanup();
                        System.err.println("SQLException:"+ ex);                        
			throw(ex);
		} catch(ClassNotFoundException e){
			cleanup();
                        System.err.println("ClassNotFoundException:"+ e);
			throw(e);
                }
	}

	public final void connect() throws SQLException {
		if(conn != null) {
			cleanup();
			conn = null;
		}
		conn = DriverManager.getConnection(jdbcDatabaseURL, username, password);
	}
        public Connection DBconnect() throws SQLException {
		if(conn != null) {
			cleanup();
			conn = null;
		}
		conn = DriverManager.getConnection(jdbcDatabaseURL, username, password);
                return conn;
	}

        public final void cleanup() throws SQLException {
		if(conn != null) {
			conn.close();
		} 
	}

	public ResultSet query(String queryStatement) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(queryStatement);
		} catch(SQLException ex) {
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch(SQLException ex2) { }
			finally {
				throw(ex);
			}
		}
		return(rs);
	}

        public ResultSet preparedState(PreparedStatement pstmt) throws SQLException {
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch(SQLException ex) {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
			} catch(SQLException ex2) { }
			finally {
				throw(ex);
			}
		}
		return(rs);
	}

	public int insert(String insertStatement) throws SQLException {
		return(update(insertStatement));
	}

	public int delete(String deleteStatement) throws SQLException {
		return(update(deleteStatement));
	}

	public int update(String updateStatement) throws SQLException {
		Statement stmt = null;
		int numRows = -1;
		try {
			stmt = conn.createStatement();
			numRows = stmt.executeUpdate(updateStatement);
		} catch(SQLException ex) {
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch(SQLException ex2) {
			
			} finally {
				throw(ex);
			}
		} finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
			} catch(SQLException ex) { }
		}
		return(numRows);
	}

	public Connection getConnection() {
		return(conn);
	}

        public int rowCount(ResultSet rs) throws SQLException {
                int rowNum = 0;
                while(rs.next()){
                    rowNum = rs.getInt(1);
                }
                return rowNum;
        }
}
