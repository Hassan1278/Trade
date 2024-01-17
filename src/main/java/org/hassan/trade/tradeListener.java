package org.hassan.trade;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class tradeListener implements Listener {
    public HashMap<Player,Player> players = new HashMap<>();
    public HashMap<Player,Player> players2 = new HashMap<>();
    public void add(Player p1, Player p2){
        players.put(p1,p2);
        players2.put(p2,p1);

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Trade") ){
            Player p = (Player) e.getWhoClicked();
            if(players.containsKey(p)){
                if((e.getSlot() <= 17 || e.getSlot() == 26 || e.getSlot() >= 56) || e.getClickedInventory().getHolder() != null ){
                    if(e.getSlot()==26 && e.getClickedInventory().getHolder() == null ){
                        accept(p, Objects.requireNonNull(e.getCurrentItem()));
                        e.setCancelled(true);
                    }
                }else{
                    e.setCancelled(true);
                }
            }else{
                if(e.getSlot() >= 26 || Objects.requireNonNull(e.getClickedInventory()).getHolder() != null ){
                    if(e.getSlot()==26 && Objects.requireNonNull(e.getClickedInventory()).getHolder() == null){
                        accept(p, Objects.requireNonNull(e.getCurrentItem()));
                        e.setCancelled(true);
                    }
                }else{
                    e.setCancelled(true);
                }
            }
        }
    }
    public void accept(Player p, ItemStack item){
        if(item.getType().equals(Material.REDSTONE_BLOCK)){
            item.setType(Material.EMERALD_BLOCK);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(p.getName());
            item.setItemMeta(meta);

        }else if(item.getType().equals(Material.EMERALD_BLOCK)){
            if(item.getItemMeta().getDisplayName().equals(p.getName())){
                    item.setType(Material.REDSTONE_BLOCK);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(null);
            }else{
                finishTrade(p.getOpenInventory().getTopInventory());
            }
        }
    }
    public void finishTrade(Inventory inv){
        List<HumanEntity> viewers = inv.getViewers();
        Player p1;
        Player p2;
        if(players.containsKey((Player)viewers.get(0))){
            p1 = (Player) viewers.get(0);
            p2 = (Player) viewers.get(1);
        }else{
            p1 = (Player) viewers.get(1);
            p2 = (Player) viewers.get(0);
        }
        p1.closeInventory();
        p2.closeInventory();
        for(int i = 0; i <18;i++){
            if(p2.getInventory().firstEmpty() == -1){
                p2.getWorld().dropItem(p2.getLocation(), Objects.requireNonNull(inv.getItem(i)));

            }else{
                p2.getInventory().addItem((inv.getItem(i)));
            }
            if(p1.getInventory().firstEmpty() == -1){
                p1.getWorld().dropItem(p2.getLocation(), Objects.requireNonNull(inv.getItem(i + 27)));


            }else {
                p1.getInventory().addItem((inv.getItem(i + 27)));
            }

        }
        players.remove(p1);

    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        Player p1 = (Player) e.getPlayer();
        Player p2;
        Inventory inv = p1.getOpenInventory().getTopInventory();

        if(players.containsKey(p1)){
            p2 = players.get(p1);
        }else{
            p2 = players2.get(p1);
        }
        p1.closeInventory();
        p2.closeInventory();
        for(int i = 0; i <18;i++){
            inv.getItem(i);
            p2.getInventory().addItem((inv.getItem(i)));
            inv.getItem(i + 27);
            p1.getInventory().addItem((inv.getItem(i+27 )));

        }
        if(players.containsKey(p1)){
            players.remove(p1);
        }else{
            players.remove(p2);
        }


    }

}

