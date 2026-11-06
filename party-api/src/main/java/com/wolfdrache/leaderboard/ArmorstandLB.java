package com.wolfdrache.leaderboard;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;

class ArmorstandLB {
    static void setNamespacedKey(Plugin plugin) {
        LEADERBOARD_KEY = new NamespacedKey(plugin, "leaderboard");
    }
    private static NamespacedKey LEADERBOARD_KEY;

    final EulerAngle headPose;
    final EulerAngle bodyPose;
    final EulerAngle leftArmPose;
    final EulerAngle rightArmPose;
    final EulerAngle leftLegPose;
    final EulerAngle rightLegPose;
    final boolean small;
    final Location location;

    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack itemInMainHand;
    private final ItemStack itemInOffHand;
    ArmorStand armorStand;

    ArmorstandLB(EulerAngle headPose, EulerAngle bodyPose, EulerAngle leftArmPose, EulerAngle rightArmPose, EulerAngle leftLegPose, EulerAngle rightLegPose, boolean small, Location location, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack itemInMainHand, ItemStack itemInOffHand) {
        this.headPose = headPose;
        this.bodyPose = bodyPose;
        this.leftArmPose = leftArmPose;
        this.rightArmPose = rightArmPose;
        this.leftLegPose = leftLegPose;
        this.rightLegPose = rightLegPose;
        this.small = small;
        this.location = location;

        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.itemInMainHand = itemInMainHand;
        this.itemInOffHand = itemInOffHand;
    }

    public void summon(int rank) {
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setHeadPose(headPose);
        armorStand.setBodyPose(bodyPose);
        armorStand.setLeftArmPose(leftArmPose);
        armorStand.setRightArmPose(rightArmPose);
        armorStand.setLeftLegPose(leftLegPose);
        armorStand.setRightLegPose(rightLegPose);
        armorStand.setSmall(small);
        armorStand.setVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.setCustomNameVisible(true);
        armorStand.getEquipment().setChestplate(chestplate);
        armorStand.getEquipment().setLeggings(leggings);
        armorStand.getEquipment().setBoots(boots);
        armorStand.getEquipment().setItemInMainHand(itemInMainHand);
        armorStand.getEquipment().setItemInOffHand(itemInOffHand);
        armorStand.getPersistentDataContainer().set(LEADERBOARD_KEY, PersistentDataType.INTEGER, rank);
    }

    public static boolean isLeaderboardArmorstand(ArmorStand armorStand) {
        if (armorStand == null) return false;
        return armorStand.getPersistentDataContainer().has(LEADERBOARD_KEY, PersistentDataType.INTEGER);
    }
}
