package me.bloodskreaper.cf_clans.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bloodskreaper.cf_clans.CF_Clans;
import me.bloodskreaper.cf_clans.clansystem.Clan;
import me.bloodskreaper.cf_clans.clansystem.Invite;

public class DenyInviteCommand implements CommandInterface {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (args.length == 1) {
			if (!CF_Clans.getInviteManager().playerIsInvited(p.getUniqueId())) {
				CF_Clans.sendMessageToPlayer(p, "�cDu hast keine Einladungen!");
				return false;
			}
			List<Invite> invites = CF_Clans.getInviteManager().getInvitesOfPlayer(p.getUniqueId());
			CF_Clans.sendMessageToPlayer(p,
					"�aVon folgenden Clans steht eine Einladung aus: " + convertInviteListToCommaString(invites));

			return false;
		}
		if (args.length > 2) {
			CF_Clans.sendMessageToPlayer(p, "�cZu viele Angaben! �b/clan denyinvite <CLANNAME>");
			return false;
		}
		Clan clan = CF_Clans.getClanManager().getClanFromName(args[1]);
		if (clan == null) {
			CF_Clans.sendMessageToPlayer(p, "�cDer Clan existiert nicht! Achte auf die Schreibweise des Namens.");
			return false;
		}
		if (!CF_Clans.getInviteManager().playerHasInviteFromClan(p.getUniqueId(), clan)) {
			CF_Clans.sendMessageToPlayer(p, "�cDu hast keine Einladung vom Clan �6" + args[1]);
			return false;
		}
		Invite inv = CF_Clans.getInviteManager().getInviteOfPlayerAndClan(p.getUniqueId(), clan);
		Player leader = Bukkit.getPlayer(clan.getLeader());
		if (leader != null) {
			CF_Clans.sendMessageToPlayer(leader,
					"�cDer Spieler �6" + p.getName() + " �c hat die Clan-Einladung abgelehnt!");
		}
		CF_Clans.getInviteManager().removeInvite(inv);
		CF_Clans.sendMessageToPlayer(p,
				"�aDu hast die Einladung vom Clan �6" + inv.getClan().getName() + " �aabgelehnt!");

		return false;
	}

	private String convertInviteListToCommaString(List<Invite> invites) {
		int i = invites.size();
		String output = "�6";
		for (Invite in : invites) {
			if (i == 1) {
				output = output + in.getClan().getName();

			} else {
				output = output + in.getClan().getName() + "�a, �6";

			}
			i = i - 1;
		}
		return output;
	}
}
