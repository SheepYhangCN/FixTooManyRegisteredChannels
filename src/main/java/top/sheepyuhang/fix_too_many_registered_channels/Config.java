package top.sheepyuhang.fix_too_many_registered_channels;


import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;

public final class Config {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("fix_too_many_registered_channels.json");

    public static final ConfigClassHandler<Config> INSTANCE = ConfigClassHandler.createBuilder(Config.class)
        .id(Identifier.fromNamespaceAndPath("fix_too_many_registered_channels", "client_config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
                .setPath(CONFIG_FILE).build())   // Change "modid" to your mod ID.
        .build();

	@SerialEntry
	public boolean disableModChannels = false;
}
