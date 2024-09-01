package net.aronkrebs.techreborn.block.entity;

import net.aronkrebs.techreborn.TechReborn;
import net.aronkrebs.techreborn.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<Pulverizer_BlockEntity> PULVERIZER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TechReborn.MOD_ID, "pulverizer_block_be"),
                    FabricBlockEntityTypeBuilder.create(Pulverizer_BlockEntity::new,
                            ModBlocks.PULVERIZER_BLOCK).build());

    public static final BlockEntityType<Coal_Generator_BlockEntity> COAL_GENERATOR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(TechReborn.MOD_ID, "coal_generator_be"),
                    FabricBlockEntityTypeBuilder.create(Coal_Generator_BlockEntity::new,
                            ModBlocks.COAL_GENERATOR).build());

    public static void registerBlockEntities() {
        TechReborn.LOGGER.info("Registering Block Entities for " + TechReborn.MOD_ID);
    }
}
