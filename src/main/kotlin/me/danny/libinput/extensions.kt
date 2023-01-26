package me.danny.libinput

import org.bukkit.*
import org.bukkit.command.*

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)
fun CommandSender.msg(msg: String) = this.sendMessage(msg.color())