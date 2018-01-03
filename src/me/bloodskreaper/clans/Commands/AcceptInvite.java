package me.bloodskreaper.clans.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bloodskreaper.clans.Invite;
import me.bloodskreaper.clans.Main;

public class AcceptInvite implements CommandInterface{

	 
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player p = (Player) sender;
        if(args.length == 1) {
        	if(!Main.getInviteManager().isInvited(p.getUniqueId())) {
        		Main.sendMessageToPlayer(p, "�cDu hast keine Einladungen!");
        	}else {
        		List<Invite> invites = Main.getInviteManager().getInvitesOfPlayer(p.getUniqueId());
        		Main.sendMessageToPlayer(p, "�aVon folgenden Clans steht eine Einladung aus: "+ListToCommaString(invites));
        		
        	}
        	
        }else {
        	if(args.length >2) {
        		Main.sendMessageToPlayer(p, "�cZu viele Angaben! �b/clan acceptinvite <CLANNAME>");
        	}else {
        		if(Main.getClanManager().getClanFromName(args[1]) == null) {
        			Main.sendMessageToPlayer(p, "�cDer Clan existiert nicht! Achte auf die Schreibweise des Namens.");
        		}else {
        			if(!Main.getInviteManager().PlayerhasInviteFromClan(p.getUniqueId(), args[1])) {
        				Main.sendMessageToPlayer(p, "�cDu hast keine Einladung vom Clan �6"+args[1]);
        			}else {
        				if(Main.getClanManager().getClanOfMember(p.getUniqueId())!= null) {
        					Main.sendMessageToPlayer(p, "�cDu kannst nicht einem anderen Clan beitreten, solange du Mitglied eines Clans bist!");
        				}else {
        					Invite inv = Main.getInviteManager().getInvite(p.getUniqueId(), args[1]);
        					inv.getClan().sendMessageToAllMembers("�aDer Spieler �6"+p.getName()+" �ahat die Clan-Einladung angenommen und ist nun Mitglied bei �6"+inv.getClan().getName());
        					inv.getClan().addMember(p.getUniqueId());
        					Main.getInviteManager().removeInvite(inv);
        					p.sendMessage("�aDu hast die Einladung vom Clan �6"+inv.getClan().getName()+" �aangenommen und bist nun Mitglied des Clans!");
        					
        				}
        			}
        		}
        	}
        }
     return false;   
    }
    
    public String ListToCommaString(List<Invite> invites) {
    	int i = invites.size();
    	String output = "�6";
		for(Invite in : invites){
			if(i==1){
				output = output+in.getClan().getName();

			}else{
				output = output+in.getClan().getName()+"�a, �6";

			}
			i = i-1;

		}
		return output;
		}

}
