package net.aronkrebs.techreborn.screen.renderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code ist licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Details can be found in the license file in the root folder of this project
 */
public class EnergyInfoArea extends InfoArea {
    private long energyAmount;
    private final long energyCapacity;

    private final Identifier TEXTURE;

    public EnergyInfoArea(int xMin, int yMin, EnergyStorage energy, int width, int height, Identifier TEXTURE)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energyAmount = energy.getAmount();
        this.energyCapacity = energy.getCapacity();
        this.TEXTURE = TEXTURE;
    }

    // Methode zum Setzen des Energielevels
    public void setEnergyLevel(long energyAmount) {
        this.energyAmount = energyAmount;
    }

    public List<Text> getTooltips() {
        return List.of(Text.literal(energyAmount + "/" + energyCapacity + " E"));
    }

    @Override
    public void draw(DrawContext context, int energyStartX, int energyStartY) {
        final int height = area.getHeight();

        // Berechnung der Höhe basierend auf dem Energielevel
        int stored = (int)(height * (energyAmount / (float) energyCapacity));

        // Zeichne das Energiebild basierend auf dem gespeicherten Energielevel
        int yOffset = height - stored;
        context.drawTexture(
                TEXTURE,
                area.getX(),
                area.getY() + yOffset,
                energyStartX,
                energyStartY + yOffset,
                area.getWidth(),
                stored
        );
    }
}