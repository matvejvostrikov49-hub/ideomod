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

    public MuteScreen(String targetPlayer) {
        super(Text.literal("Управление мутами: " + targetPlayer));
        this.targetPlayer = targetPlayer;
    }

    @Override
    protected void init() {
        this.clearChildren();
        reasonButtons.clear();
        timeButtons.clear();

        int startX = this.width / 2 - 155;
        int startY = 40;

        String[] reasons = {"2.2", "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9", "2.10", 
                            "2.11", "2.12", "2.13", "2.14", "2.15", "2.16", "2.17", "2.18", "2.19", "2.20"};
        
        for (int i = 0; i < reasons.length; i++) {
            String reason = reasons[i];
            int row = i / 5;
            int col = i % 5;
            
            ButtonWidget btn = ButtonWidget.builder(Text.literal(reason), button -> {
                this.selectedReason = reason;
                updateButtonStyles();
            }).dimensions(startX + (col * 62), startY + (row * 24), 58, 20).build();
            
            this.addSelectableChild(btn);
            reasonButtons.add(btn);
        }

        String[] times = {"30m", "60m", "120m", "180m", "6h", "12h", "24h", "72h"};
        int timeY = startY + (5 * 24) + 10;

        for (int i = 0; i < times.length; i++) {
            String time = times[i];
            int col = i % 4;
            int row = i / 4;

            ButtonWidget btn = ButtonWidget.builder(Text.literal(time), button -> {
                this.selectedTime = time;
                updateButtonStyles();
            }).dimensions(startX + (col * 78), timeY + (row * 24), 74, 20).build();

            this.addSelectableChild(btn);
            timeButtons.add(btn);
        }

        this.confirmButton = ButtonWidget.builder(Text.literal("Подтвердить"), button -> {
            executeMute();
        }).dimensions(this.width / 2 - 100, this.height - 35, 200, 20).build();
        
        this.confirmButton.active = false;
        this.addSelectableChild(this.confirmButton);
    }

    private void updateButtonStyles() {
        this.confirmButton.active = !selectedReason.isEmpty() && !selectedTime.isEmpty();
    }

    private void executeMute() {
        if (MinecraftClient.getInstance().player != null) {
            String command = String.format("mute %s %s %s", targetPlayer, selectedTime, selectedReason);
            MinecraftClient.getInstance().player.networkHandler.sendCommand(command);
            MinecraftClient.getInstance().player.networkHandler.sendCommand("work");
            this.close();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, "Выберите причину мута:", this.width / 2, 25, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Выберите время мута:", this.width / 2, 155, 0xFFFFFF);
        
        String status = String.format("Игрок: %s | Пункт: %s | Время: %s", targetPlayer, 
                selectedReason.isEmpty() ? "?" : selectedReason, selectedTime.isEmpty() ? "?" : selectedTime);
        context.drawCenteredTextWithShadow(this.textRenderer, status, this.width / 2, this.height - 55, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }
}
