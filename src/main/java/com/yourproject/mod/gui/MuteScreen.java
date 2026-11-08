package com.yourproject.mod.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MuteScreen extends Screen {
    private final String targetPlayer;
    private String selectedReason = "";
    private String selectedTime = "";
    private ButtonWidget confirmButton;
    private final List<ButtonWidget> reasonButtons = new ArrayList<>();
    private final List<ButtonWidget> timeButtons = new ArrayList<>();
    private boolean workSentOnOpen = false;

    public MuteScreen(String targetPlayer) {
        super(Text.literal("Модерация: " + targetPlayer));
        this.targetPlayer = targetPlayer;
    }

    @Override
    protected void init() {
        super.init();
        reasonButtons.clear();
        timeButtons.clear();

        if (!workSentOnOpen) {
            sendServerCommand("work");
            workSentOnOpen = true;
        }

        int startX = this.width / 2 - 155;
        int startY = 48;
        String[] reasons = {
            "2.2", "2.3", "2.4", "2.5", "2.6",
            "2.7", "2.8", "2.9", "2.10", "2.11",
            "2.12", "2.13", "2.14", "2.15", "2.16",
            "2.17", "2.18", "2.19", "2.20"
        };

        for (int i = 0; i < reasons.length; i++) {
            String reason = reasons[i];
            int row = i / 5;
            int col = i % 5;
            ButtonWidget btn = ButtonWidget.builder(Text.literal(reason), button -> {
                selectedReason = reason;
                updateButtonStyles();
            }).dimensions(startX + col * 62, startY + row * 25, 58, 21).build();
            addDrawableChild(btn);
            reasonButtons.add(btn);
        }

        String[] times = {"30m", "60m", "180m", "6h", "12h"};
        int timeY = startY + 5 * 25 + 14;
        for (int i = 0; i < times.length; i++) {
            String time = times[i];
            ButtonWidget btn = ButtonWidget.builder(Text.literal(time), button -> {
                selectedTime = time;
                updateButtonStyles();
            }).dimensions(startX + i * 62, timeY, 58, 21).build();
            addDrawableChild(btn);
            timeButtons.add(btn);
        }

        confirmButton = ButtonWidget.builder(Text.literal("Подтвердить"), button -> executeMute())
            .dimensions(this.width / 2 - 100, this.height - 34, 200, 20).build();
        confirmButton.active = false;
        addDrawableChild(confirmButton);
    }

    private void updateButtonStyles() {
        if (confirmButton != null) {
            confirmButton.active = !selectedReason.isEmpty() && !selectedTime.isEmpty();
        }
    }

    private void executeMute() {
        if (selectedReason.isEmpty() || selectedTime.isEmpty()) return;
        sendServerCommand("tempmute " + targetPlayer + " " + selectedTime + " " + selectedReason);
        close();
    }

    private void sendServerCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendChatCommand(command);
        }
    }

    @Override
    public void close() {
        sendServerCommand("work");
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "Модерация игрока: " + targetPlayer,
            width / 2, 25, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Выбери пункт нарушения:",
            width / 2, 37, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Выбери длительность мута:",
            width / 2, 192, 0xFFFFFF);
        String status = "Пункт: " + (selectedReason.isEmpty() ? "—" : selectedReason)
            + "    Время: " + (selectedTime.isEmpty() ? "—" : selectedTime);
        context.drawCenteredTextWithShadow(textRenderer, status, width / 2, height - 52, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
}
