package net.aronkrebs.techreborn.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.aronkrebs.techreborn.TechReborn;
import net.aronkrebs.techreborn.screen.renderer.EnergyInfoArea;
import net.aronkrebs.techreborn.util.MouseUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;
import java.util.Optional;

public class SolarGeneratorMK1Screen extends HandledScreen<SolarGeneratorMK1ScreenHandler> {
    private static final Identifier
            TEXTURE = new Identifier(TechReborn.MOD_ID, "textures/gui/solar_generator_mk1_gui.png");

    int energyStartX = 176;
    int energyStartY = 29;

    private EnergyInfoArea energyInfoArea;

    public SolarGeneratorMK1Screen(SolarGeneratorMK1ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX += 52;
        assignEnergyInfoArea();
    }

    private void assignEnergyInfoArea() {
        energyInfoArea = new EnergyInfoArea(
                ((width - backgroundWidth) / 2) + 12,
                ((height - backgroundHeight) / 2) + 12,
                handler.blockEntity.energyStorage,
                14, 42,
                TEXTURE);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderEnergyAreaTooltips(context, mouseX, mouseY, x, y);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        energyInfoArea.draw(context, energyStartX, energyStartY); //Energy Storage renderer
    }

    private void renderEnergyAreaTooltips(DrawContext context, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 11, 11, 14, 43)) {
            List<Text> tooltips = energyInfoArea.getTooltips();

            pMouseX -= x;
            pMouseY -= y;

            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltips, Optional.empty(), pMouseX, pMouseY);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}