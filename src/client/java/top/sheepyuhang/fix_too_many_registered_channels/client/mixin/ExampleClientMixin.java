package top.sheepyuhang.fix_too_many_registered_channels.client.mixin;

import net.fabricmc.fabric.impl.networking.GlobalReceiverRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.sheepyuhang.fix_too_many_registered_channels.FixTooManyRegisteredChannels;
import top.sheepyuhang.fix_too_many_registered_channels.client.FixTooManyRegisteredChannelsClient;

@Mixin(value = GlobalReceiverRegistry.class, remap = false)
public class ExampleClientMixin {
    @Inject(
        method = "registerGlobalReceiver",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRegisterGlobalReceiver(Identifier channelName, Object handler, CallbackInfoReturnable<Boolean> cir) {
        // 只在连接到多人游戏服务器时才拦截
        if (FixTooManyRegisteredChannelsClient.getConfig().disableModChannels && isMultiplayer()) {
            FixTooManyRegisteredChannels.LOGGER.info("[Fabric API] Blocked channel registration: {}", channelName);
            cir.setReturnValue(false);
        }
    }

    /**
     * 检查当前是否在多人游戏环境中
     */
    private boolean isMultiplayer() {
        Minecraft client = Minecraft.getInstance();
        return client.getConnection() != null;
    }
}
