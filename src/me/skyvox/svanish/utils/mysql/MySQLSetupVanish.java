package me.skyvox.svanish.utils.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.skyvox.svanish.files.MySQLFile;

public class MySQLSetupVanish {
	private Connection connection;
	public String host;
	public String username;
	public String database;
	public String password;
	public String table;
	private int port;
	
	public MySQLSetupVanish() {
		this.host = MySQLFile.get().getString("MySQL.host");
		this.database = MySQLFile.get().getString("MySQL.database");
		this.username = MySQLFile.get().getString("MySQL.username");
		this.password = MySQLFile.get().getString("MySQL.password");
		this.table = MySQLFile.get().getString("MySQL.table");
		this.port = MySQLFile.get().getInt("MySQL.port");
		
		try {
			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
				connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `vanish` (UUID varchar(36), REAL_NAME varchar(16) NOT NULL UNIQUE, VANISHED boolean NOT NULL, PRIMARY KEY(UUID))");
				DatabaseMetaData dbm = connection.getMetaData();
				ResultSet result = dbm.getTables(null, null, table, null);
				if (!result.next()) {
					MySQLFile.get().set("MySQL.table", String.valueOf("vanish"));
					MySQLFile.save();
					this.table = "vanish";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getHost() {
		return host;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getTable() {
		return table;
	}
	
	public int getPort() {
		return port;
	}
	
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}