package me.skyvox.svanish.utils.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.skyvox.svanish.Vanish;

public class MySQLPlayerSetupVisibility {
	
	public void setPlayer(final UUID uuid, boolean isVanished) {
		try {
			PreparedStatement statement = Vanish.vanishData.getConnection().prepareStatement("SELECT * FROM `" + Vanish.visibilityData.getTable() + "` WHERE `UUID`=?");
			statement.setString(1, uuid.toString());
			ResultSet result = statement.executeQuery();
			result.next();
			if (!playerExits(uuid)) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
				PreparedStatement set = Vanish.vanishData.getConnection().prepareStatement("INSERT INTO " + Vanish.visibilityData.getTable() + " (UUID,REAL_NAME,VANISHED) VALUES (?,?,?)");
				set.setString(1, uuid.toString());
				set.setString(2, player.getName());
				set.setBoolean(3, isVanished);
				set.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean playerExits(UUID uuid) {
		try {
			PreparedStatement statement = Vanish.vanishData.getConnection().prepareStatement("SELECT * FROM `" + Vanish.visibilityData.getTable() + "` WHERE `UUID`=?");
			statement.setString(1, uuid.toString());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void refresh(UUID uuid, boolean isVanished) {
		setPlayer(uuid, isVanished);
		int i = isVanished ? 1 : 0;
		try {
			Statement statement = Vanish.visibilityData.getConnection().createStatement();
			statement.executeUpdate("UPDATE `" + Vanish.visibilityData.getTable() + "` SET `VANISHED` = '" + i + "' WHERE `UUID` = '" + uuid.toString() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPlayer(UUID uuid) {
		try {
			Statement statement = Vanish.visibilityData.getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM `" + Vanish.visibilityData.getTable() + "` WHERE `UUID`='" + uuid.toString() + "'");
			if (result.next()) {
				return result.getBoolean(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}