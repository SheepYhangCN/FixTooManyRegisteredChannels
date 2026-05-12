package top.sheepyuhang.fix_too_many_registered_channels.client.compat;

import top.sheepyuhang.fix_too_many_registered_channels.Config;
import top.sheepyuhang.fix_too_many_registered_channels.client.FixTooManyRegisteredChannelsClient;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.api.Option;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigMenu {public static Screen makeScreen(Screen parent) {
    return YetAnotherConfigLib.create(Config.INSTANCE, (defaults, config, builder) -> builder
            .title(Component.translatable("config.fix_too_many_registered_channels.title"))
            .category(ConfigCategory.createBuilder()
                    .name(Component.translatable("config.fix_too_many_registered_channels.category.general"))
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("config.fix_too_many_registered_channels.option.disableModChannels.name"))
                            .description(OptionDescription.of(Component.translatable("config.fix_too_many_registered_channels.option.disableModChannels.description")))
                            .binding(
                                    false,
                                    () -> FixTooManyRegisteredChannelsClient.getConfig().disableModChannels,
                                    value -> FixTooManyRegisteredChannelsClient.getConfig().disableModChannels = value
                            )
                            .controller(opt -> TickBoxControllerBuilder.create(opt))
                            .build())
                    .build()))
            .generateScreen(parent);
	}
}
