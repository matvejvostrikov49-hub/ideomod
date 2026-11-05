package com.yourproject.mod;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.yourproject.mod.gui.MuteScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class ModInitializerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("work")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                .executes(context -> {
                    String targetPlayer = StringArgumentType.getString(context, "player");
                    
                    MinecraftClient.getInstance().execute(() -> {
                        MinecraftClient.getInstance().setScreen(new MuteScreen(targetPlayer));
                    });
                    return 1;
                })));
        });
    }
}
