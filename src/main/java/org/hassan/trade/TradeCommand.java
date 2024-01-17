package org.hassan.trade;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class TradeCommand implements CommandExecutor{
    HashMap<Player,Player> requests = new HashMap<>();
    tradeListener trade;
    public TradeCommand(tradeListener L){
        this.trade = L;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(sender instanceof Player){
            Player receiver;
            Player p = (Player) sender;
            if(args.length == 2){
                if (args[0].equalsIgnoreCase("request")){
                    receiver = Bukkit.getPlayer(args[1]);
                    assert receiver != null;
                    if(Bukkit.getOnlinePlayers().contains(receiver)){
                        p.sendMessage("Trade request sent to " + ChatColor.DARK_AQUA + args[1]);
                        requests.put(receiver,p);
                        Objects.requireNonNull(receiver).sendMessage(p.getName() + "sent you a trade request");

                    }else{
                        p.sendMessage(args[1] + " is not online/Does not exist");
                    }

                }

            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("accept")){
                    if(requests.containsKey(p)){
                        receiver = requests.get(p);
                        if(Bukkit.getOnlinePlayers().contains(receiver)){
                            Inventory tradeInv = Bukkit.createInventory(null,45, "Trade");
                            ItemStack Glas_rahmen = new ItemStack(Material.GLASS_PANE);
                            ItemStack redstone = new ItemStack(Material.REDSTONE_BLOCK);
                            for(int i = 18; i<26;i++){
                                tradeInv.setItem(i, Glas_rahmen);
                            }
                            tradeInv.setItem(26, redstone);
                            p.openInventory(tradeInv);
                            receiver.openInventory(tradeInv);
                            trade.add(p,receiver);

                        }else{
                            p.sendMessage("Player logged off");
                        }
                        requests.remove(p);


                    }else{
                        p.sendMessage("No trade requests are open");
                    }
                }else if(args[0].equalsIgnoreCase("deny")){
                    if(requests.containsKey(p)) {
                        receiver = requests.get(p);
                        requests.remove(p);
                        p.sendMessage("You denied the trade request");
                        receiver.sendMessage(p.getName() + "denied your request");
                    }


                }
            }else{
                p.sendMessage("Not valid usage: /trade <request:accept:deny> <playername>");
            }
            return true;

        }
        return false;
    }


}
