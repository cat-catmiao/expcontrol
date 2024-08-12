package com.expcontrol;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * ExpControl 插件主类，负责管理玩家经验获取倍率。
 * 继承自 JavaPlugin 并实现 Listener 接口以处理相关事件。
 */
public class Expcontrol extends JavaPlugin implements Listener {

    /**
     * 插件启用时调用的方法。
     * 在这里我们注册了事件监听器并输出插件启用的日志信息。
     */
    @Override
    public void onEnable() {
        getLogger().info("ExpControl has been enabled!"); // 输出插件启用日志
        getLogger().info("Author: xiaoyiluck"); // 输出作者信息
        getLogger().info("Version: 1.0.0"); // 输出版本信息
        getServer().getPluginManager().registerEvents(this, this); // 注册事件监听器
    }

    /**
     * 插件禁用时调用的方法。
     * 在这里我们输出插件禁用的日志信息。
     */
    @Override
    public void onDisable() {
        getLogger().info("ExpControl has been disabled!"); // 输出插件禁用日志
    }

    /**
     * 处理玩家经验变化事件。
     * 当玩家获取经验时，该方法会被调用，并根据玩家的权限节点调整经验值。
     *
     * @param event 玩家经验变化事件
     */
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

    /**
     * 根据玩家的权限节点获取经验倍率。
     * 该方法遍历玩家所有的有效权限节点，寻找以 "expcontrol." 开头的节点，
     * 并尝试将其后续部分解析为整数，作为经验倍率。
     *
     * @param player 玩家对象
     * @return 经验倍率，如果未找到匹配的权限节点，则返回 1（默认倍率）
     */
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
