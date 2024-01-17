package org.hassan.trade;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public  class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        tradeListener trader = new tradeListener();
        Objects.requireNonNull(getCommand("trade")).setExecutor(new TradeCommand(trader));
        getServer().getPluginManager().registerEvents(trader,this);
    }

    @Override
    public void onDisable() {

    }
}
