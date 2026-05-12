package top.sheepyuhang.fix_too_many_registered_channels.client.mixin;

import net.fabricmc.fabric.impl.networking.AbstractNetworkAddon;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.sheepyuhang.fix_too_many_registered_channels.FixTooManyRegisteredChannels;
import top.sheepyuhang.fix_too_many_registered_channels.client.FixTooManyRegisteredChannelsClient;

import java.util.Map;

/**
 * 拦截 Fabric API 网络 addon 的频道注册
 * 防止客户端向服务器发送过多模组频道信息
 * 只在连接到多人游戏服务器时才生效
 */
@Mixin(value = AbstractNetworkAddon.class, remap = false)
public class OutboundChannelRegistrationMixin {
    
    @Inject(
        method = "registerChannels",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRegisterChannels(Map<Identifier, ?> map, CallbackInfo ci) {
        if (FixTooManyRegisteredChannelsClient.getConfig().disableModChannels) {
            FixTooManyRegisteredChannels.LOGGER.info("[Outbound] Blocked registering {} mod channels", map.size());
            // 只保留 minecraft: 命名空间的原版频道
            map.entrySet().removeIf(entry -> !entry.getKey().getNamespace().equals("minecraft"));
            FixTooManyRegisteredChannels.LOGGER.info("[Outbound] Kept {} vanilla channels", map.size());
        }
    }
    
    @Inject(
        method = "registerChannel",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRegisterChannel(Identifier channelName, Object handler, CallbackInfoReturnable<Boolean> cir) {
        if (FixTooManyRegisteredChannelsClient.getConfig().disableModChannels && 
            !channelName.getNamespace().equals("minecraft")) {
            FixTooManyRegisteredChannels.LOGGER.debug("[Outbound] Blocked registering mod channel: {}", channelName);
            cir.setReturnValue(false);
        }
    }
}
