package net.aronkrebs.techreborn;

import net.aronkrebs.techreborn.screen.ModScreenHandlers;
import net.aronkrebs.techreborn.screen.PulverizerMK1Screen;
import net.aronkrebs.techreborn.screen.SolarGeneratorMK1Screen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TechRebornClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        HandledScreens.register(ModScreenHandlers.SOLARGENERATORMK1_SCREEN_HANDLER, SolarGeneratorMK1Screen::new);
        HandledScreens.register(ModScreenHandlers.PULVERIZERMK1_SCREEN_HANDLER, PulverizerMK1Screen::new);

    }
}
