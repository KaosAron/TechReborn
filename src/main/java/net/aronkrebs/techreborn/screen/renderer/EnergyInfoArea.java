package net.aronkrebs.techreborn.screen.renderer;

import net.aronkrebs.techreborn.TechReborn;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code ist licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Details can be found in the license file in the root folder of this project
 */
public class EnergyInfoArea extends InfoArea {
    private final EnergyStorage energy;

    private static final Identifier
            TEXTURE = new Identifier(TechReborn.MOD_ID, "textures/gui/pulverizer_block_gui.png");

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null, 14, 42);
    }

    public EnergyInfoArea(int xMin, int yMin, EnergyStorage energy)  {
        this(xMin, yMin, energy, 14, 42);
    }

    public EnergyInfoArea(int xMin, int yMin, EnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Text> getTooltips() {
        double amountInKE = (double) Math.round(energy.getAmount() * 10.0) / 10000.0;
        double capacityInKE = (double) Math.round(energy.getCapacity() * 10.0) / 10000.0;

        return List.of(Text.literal(amountInKE + "k / " + capacityInKE + "k RF"));
    }


    @Override
    public void draw(DrawContext context, int energyStartX, int energyStartY) {
        final int height = area.getHeight();

        // Calculate the height of the part of the texture to be drawn, based on the energy level
        int stored = (int)(height * (energy.getAmount() / (float)energy.getCapacity()));

        // Calculate the position where the texture should start (from bottom to top)
        int yOffset = height - stored;

        // Render the texture based on the energy level

        context.drawTexture(
                TEXTURE,
                area.getX(),
                area.getY() + yOffset,
                176,
                29 + yOffset,
                14,
                stored
        );
    }
}