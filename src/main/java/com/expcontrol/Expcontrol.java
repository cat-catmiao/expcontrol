package com.expcontrol;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Expcontrol extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("ExpControl has been enabled!"); // 输出插件启用日志
        getLogger().info("Author: xiaoyiluck"); // 输出作者信息
        getLogger().info("Version: 1.0.0"); // 输出版本信息
        getServer().getPluginManager().registerEvents(this, this); // 注册事件监听器
    }

    @Override
    public void onDisable() {
        getLogger().info("ExpControl has been disabled!"); // 输出插件禁用日志
    }

     //@param event 玩家经验变化事件
    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer(); // 获取触发事件的玩家
        int multiplier = getExpMultiplier(player); // 根据玩家权限获取经验倍率

        // 如果经验倍率大于 1，则按倍率调整经验值
        if (multiplier > 1) {
            int originalExp = event.getAmount(); // 获取原始经验值
            int newExp = originalExp * multiplier; // 计算新的经验值
            event.setAmount(newExp); // 设置调整后的经验值
        }
    }

     // @param player 玩家对象
     // @return 经验倍率，如果未找到匹配的权限节点，则返回 1（默认倍率）
    private int getExpMultiplier(Player player) {
        // 遍历玩家所有的有效权限节点
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            String permission = attachmentInfo.getPermission(); // 获取权限节点字符串

            // 检查权限节点是否以 "expcontrol." 开头
            if (permission.startsWith("expcontrol.")) {
                try {
                    // 解析权限节点后续部分为整数，并作为经验倍率返回
                    return Integer.parseInt(permission.substring("expcontrol.".length()));
                } catch (NumberFormatException ignored) {
                    // 如果权限节点格式不正确（无法解析为整数），则捕获异常并忽略
                }
            }
        }
        return 1; // 如果未找到匹配的权限节点，则返回默认倍率 1
    }

}
