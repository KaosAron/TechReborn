package net.aronkrebs.techreborn.screen.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import team.reborn.energy.api.EnergyStorage;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code ist licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Details can be found in the license file in the root folder of this project
 */
public abstract class InfoArea {
    protected final Rect2i area;

    protected InfoArea(Rect2i area) {
        this.area = area;
    }

    public abstract void draw(DrawContext context, int energyStartX, int energyStartY);

    protected void drawText(DrawContext context, String text, int x, int y, int color) {
        // TextRenderer über MinecraftClient abrufen
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, text, x, y, color, false);
    }
}