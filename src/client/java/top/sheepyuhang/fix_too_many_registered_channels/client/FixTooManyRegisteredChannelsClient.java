package top.sheepyuhang.fix_too_many_registered_channels.client;

import net.fabricmc.api.ClientModInitializer;
import top.sheepyuhang.fix_too_many_registered_channels.Config;

public class FixTooManyRegisteredChannelsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        Config.INSTANCE.load();
    }
 
    public static Config getConfig() {
        return Config.INSTANCE.instance();
    }
}