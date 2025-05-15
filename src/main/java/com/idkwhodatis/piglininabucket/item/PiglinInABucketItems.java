package com.idkwhodatis.piglininabucket.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class PiglinInABucketItems{
    private PiglinInABucketItems(){}

    public static final Item PiglinInABucket=register("piglin-in-a-bucket",new Item(new Item.Settings()));

    public static <T extends Item>T register(String path,T item){
        return Registry.register(Registries.ITEM,Identifier.of("piglin-in-a-bucket",path),item);
    }

    public static void initialize(){
    }
}