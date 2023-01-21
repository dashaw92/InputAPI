package me.danny.libinput

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)
fun CommandSender.msg(msg: String) = this.sendMessage(msg.color())