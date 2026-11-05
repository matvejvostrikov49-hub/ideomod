package com.yourproject.mod.mixin;

import com.yourproject.mod.gui.MuteScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        String rawText = message.getString();
        Pattern pattern = Pattern.compile("<(\\w+)>");
        Matcher matcher = pattern.matcher(rawText);

        if (matcher.find()) {
            String playerName = matcher.group(1);
            // Здесь логика связывания интерфейса с кликом в чате
        }
    }
}
