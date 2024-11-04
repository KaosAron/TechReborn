package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.TechReborn;
import net.aronkrebs.techreborn.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {
    public static final BlockEntityType<Pulverizer_BlockEntity> PULVERIZER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TechReborn.MOD_ID, "pulverizer_block_be"),
                    FabricBlockEntityTypeBuilder.create(Pulverizer_BlockEntity::new,
                            ModBlocks.PULVERIZER_BLOCK).build());

    public static final BlockEntityType<SolarGeneratorMK1_BlockEntity> SOLAR_GENERATOR_MK1_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TechReborn.MOD_ID, "solar_generator_mk1_be"),
                    FabricBlockEntityTypeBuilder.create(SolarGeneratorMK1_BlockEntity::new,
                            ModBlocks.SOLAR_GENERATOR_MK1).build());

    public static void registerBlockEntities() {
        TechReborn.LOGGER.info("Registering Block Entities for " + TechReborn.MOD_ID);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyStorage, PULVERIZER_BLOCK_ENTITY);
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.energyStorage, SOLAR_GENERATOR_MK1_BLOCK_ENTITY);
    }
}
