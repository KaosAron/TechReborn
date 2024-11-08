package net.aronkrebs.techreborn.screen;

import net.aronkrebs.techreborn.TechReborn;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SolarGeneratorMK1ScreenHandler> SOLARGENERATORMK1_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(TechReborn.MOD_ID, "solar_generator_mk1_sh"),
                    new ExtendedScreenHandlerType<>(SolarGeneratorMK1ScreenHandler::new));

    public static final ScreenHandlerType<PulverizerMK1ScreenHandler> PULVERIZERMK1_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(TechReborn.MOD_ID, "pulverizer_mk1_sh"),
                    new ExtendedScreenHandlerType<>(PulverizerMK1ScreenHandler::new));

    public static void registerScreenHandlers() {
        TechReborn.LOGGER.info("Registering Screen Handlers for " + TechReborn.MOD_ID);
    }
}