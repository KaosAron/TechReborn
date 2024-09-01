package net.aronkrebs.techreborn.screen.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import team.reborn.energy.api.EnergyStorage;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Details can be found in the license file in the root folder of this project
 */
public abstract class InfoArea extends DrawContext {
    protected final Rect2i area;

    protected InfoArea(Rect2i area, int x, int y, int width, int height) {
        super(new Rect2i(x, y, width, height));
        this.area = area;
    }

    public abstract void draw(MatrixStack stack);
}