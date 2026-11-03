package com.wolfdrache.party.general;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NavItems {
    public static ItemStack nextArrow = ItemHelper.createNextArrow(true);
    public static ItemStack previousArrow = ItemHelper.createNextArrow(false);
    public static ItemStack returnItem = ItemHelper.createNavItem("§cZurück", Material.BARRIER);
    public static ItemStack innocentReportItem = ItemHelper.createNavItem("§aInkorrekt", Material.LIME_DYE);
    public static ItemStack warningReportItem = ItemHelper.createNavItem("§eWarnung", Material.YELLOW_DYE);
    public static ItemStack muteReportItem = ItemHelper.createNavItem("§6Mute", Material.ORANGE_DYE);
    public static ItemStack bannReportItem = ItemHelper.createNavItem("§cBan", Material.RED_DYE);
    public static ItemStack customReportItem = ItemHelper.createNavItem("§bEigenes", Material.OAK_SIGN);
    public static ItemStack deleteReportItem = ItemHelper.createNavItem("§4Löschen", Material.MAGMA_BLOCK);
    public static ItemStack disabledItem = ItemHelper.createNavItem("§7Deaktiviert", Material.BARRIER);
}
