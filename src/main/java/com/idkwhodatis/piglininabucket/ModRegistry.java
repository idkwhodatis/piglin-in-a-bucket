package com.idkwhodatis.piglininabucket;

import com.idkwhodatis.piglininabucket.models.BucketOfPiglinItem;
import com.idkwhodatis.piglininabucket.models.PiglinData;

import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public final class ModRegistry{
    public static final String MOD_ID="piglin-in-a-bucket";

    public static final BucketOfPiglinItem BUCKET_OF_PIGLIN=Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID,"bucket-of-piglin"),
        new BucketOfPiglinItem(new Item.Settings().maxCount(1))
    );


    public static final ComponentType<PiglinData> PIGLIN_DATA=Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(MOD_ID,"piglin_data"),
        ComponentType.<PiglinData>builder()
            .codec(PiglinData.CODEC)
            .build()
    );

    public static void initialize(){}
}