package me.skyvox.svanish.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.files.MySQLFile;
import me.skyvox.svanish.utils.ChatUtil;
import me.skyvox.svanish.utils.VanishManager;
import me.skyvox.svanish.utils.cooldown.CooldownAPI;
import me.skyvox.svanish.utils.menu.VanishListMenu;

public class VanishCmd implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				if (player.hasPermission("vanish.vanish")) {
					if (!player.hasPermission("vanish.bypass.cooldown")) {
						if (!CooldownAPI.isOnCooldown(player.getUniqueId(), "VanishToggle")) {
							CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "VanishToggle", ConfigFile.get().getInt("WaitToToggleVanish"));
							cooldown.start();
						} else {
							if (ConfigFile.get().contains("Messages.IsOnCooldown")) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.IsOnCooldown").replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()).replace("%time%", "" + CooldownAPI.getTimeLeft(player.getUniqueId(), "VanishToggle"))));
							}
							return true;
						}
					}
					VanishManager.toggle(player);
					return true;
				} else {
					ChatUtil.noPermission(player);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("toggle")) {
				if (player.hasPermission("vanish.vanish.others")) {
					if (args.length >= 2) {
						if (!player.hasPermission("vanish.bypass.cooldown")) {
							if (!CooldownAPI.isOnCooldown(player.getUniqueId(), "VanishToggle")) {
								CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "VanishToggle", ConfigFile.get().getInt("WaitToToggleVanish"));
								cooldown.start();
							} else {
								if (ConfigFile.get().contains("Messages.IsOnCooldown")) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.IsOnCooldown").replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()).replace("%time%", "" + CooldownAPI.getTimeLeft(player.getUniqueId(), "VanishToggle"))));
								}
								return true;
							}
						}
						Player target = Bukkit.getPlayer(args[1]);
						if (VanishManager.isVanished(target)) {
							if (ConfigFile.get().contains("Messages.UnVanish-Others")) {
								for (String msgs : ConfigFile.get().getStringList("Messages.UnVanish-Others")) {
									String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()).replace("%target%", target.getName()));
									player.sendMessage(msg);
								}
							}
						} else {
							if (ConfigFile.get().contains("Messages.Vanish-Others")) {
								for (String msgs : ConfigFile.get().getStringList("Messages.Vanish-Others")) {
									String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()).replace("%target%", target.getName()));
									player.sendMessage(msg);
								}
							}
						}
						VanishManager.toggle(target);
						return true;
					} else {
						if (ConfigFile.get().contains("Messages.Illegal-Args")) {
							String usage = "/vanish toggle <Player>";
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.Illegal-Args").replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()).replace("%usage%", usage)));
						}
						return true;
					}
				} else {
					ChatUtil.noPermission(player);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("list")) {
				if (player.hasPermission("vanish.list")) {
					VanishListMenu.vanishedPlayersMenu(player);
					return true;
				} else {
					ChatUtil.noPermission(player);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
				if (player.hasPermission("vanish.reload")) {
					ConfigFile.reload();
					MySQLFile.reload();
					MySQLFile.save();
					if (ConfigFile.get().contains("Messages.Vanish-Reload")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.Vanish-Reload")));
					}
					return true;
				} else {
					ChatUtil.noPermission(player);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("help")) {
				if (ConfigFile.get().contains("CommandsHelp")) {
					for (String msg : ConfigFile.get().getStringList("CommandsHelp")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName())));
					}
				}
				return true;
			}
			return true;
		} else {
			if (args.length == 0) {
				if (ConfigFile.get().contains("CommandsHelp")) {
					for (String msg : ConfigFile.get().getStringList("CommandsHelp")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
					}
				}
				return true;
			} else if (args[0].equalsIgnoreCase("toggle")) {
				if (sender.hasPermission("vanish.vanish.others")) {
					if (args.length >= 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (VanishManager.isVanished(target)) {
							if (ConfigFile.get().contains("Messages.UnVanish-Others")) {
								for (String msgs : ConfigFile.get().getStringList("Messages.UnVanish-Others")) {
									String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", sender.getName()).replace("%target%", target.getName()));
									sender.sendMessage(msg);
								}
							}
						} else {
							if (ConfigFile.get().contains("Messages.Vanish-Others")) {
								for (String msgs : ConfigFile.get().getStringList("Messages.Vanish-Others")) {
									String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", sender.getName()).replace("%target%", target.getName()));
									sender.sendMessage(msg);
								}
							}
						}
						VanishManager.toggle(target);
						return true;
					} else {
						if (ConfigFile.get().contains("Messages.Illegal-Args")) {
							String usage = "/vanish toggle <Player>";
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.Illegal-Args").replace("%usage%", usage)));
						}
						return true;
					}
				} else {
					ChatUtil.noPermission(sender);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("list")) {
				if (sender.hasPermission("vanish.list")) {
					for (UUID uuid : VanishManager.getVanishList()) {
						Player player = Bukkit.getPlayer(uuid);
						sender.sendMessage(ChatColor.GREEN + player.getName() + " are vanished!");
					}
					return true;
				} else {
					ChatUtil.noPermission(sender);
				}
				return true;
			} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
				if (sender.hasPermission("vanish.reload")) {
					ConfigFile.reload();
					MySQLFile.reload();
					MySQLFile.save();
					if (ConfigFile.get().contains("Messages.Vanish-Reload")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.Vanish-Reload")));
					}
					return true;
				} else {
					ChatUtil.noPermission(sender);
				}
				return true;
			}
		}
		return false;
	}
}