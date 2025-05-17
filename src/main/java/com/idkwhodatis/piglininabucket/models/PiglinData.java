package com.idkwhodatis.piglininabucket.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;


public record PiglinData(
    Text customName,
    boolean isBaby,
    float health,
    DefaultedList<ItemStack> armorItems,
    DefaultedList<ItemStack> handItems,
    DefaultedList<ItemStack> inventory
){
    public List<Text> getTooltip(){
        List<Text> tooltip=new ArrayList<>();
        Text name=(customName!=null&&!customName.getString().isEmpty()) ? customName : null;
        Text stats=Text.translatable(isBaby ? "tooltip.piglin-in-a-bucket.baby" : "tooltip.piglin-in-a-bucket.adult").append(", "+String.format("%.1f",health)+" HP").formatted(Formatting.GRAY);
        if(name!=null){
            tooltip.add(name.copy().formatted(Formatting.GRAY));
        }
        tooltip.add(stats);
        return tooltip;
    }

    public static final Codec<DefaultedList<ItemStack>> ITEMSTACK_LIST_CODEC=ItemStack.CODEC.listOf().xmap(
        list->{
            DefaultedList<ItemStack> result=DefaultedList.ofSize(list.size(),ItemStack.EMPTY);
            for(int i=0;i<list.size();i++){
                result.set(i,list.get(i));
            }
            return result;
        },
        defaultedList->defaultedList.stream().toList()
    );

    public static final Codec<PiglinData> CODEC=RecordCodecBuilder.create(instance->instance.group(
        TextCodecs.CODEC.optionalFieldOf("name")
            .xmap(opt->opt.orElse(null),Optional::ofNullable)
            .forGetter(PiglinData::customName),
        Codec.BOOL.fieldOf("is_baby").forGetter(PiglinData::isBaby),
        Codec.FLOAT.fieldOf("health").forGetter(PiglinData::health),
        ITEMSTACK_LIST_CODEC.fieldOf("armor_items").forGetter(PiglinData::armorItems),
        ITEMSTACK_LIST_CODEC.fieldOf("hand_items").forGetter(PiglinData::handItems),
        ITEMSTACK_LIST_CODEC.optionalFieldOf("inventory")
            .xmap(opt->opt.orElse(DefaultedList.of()),Optional::of)
            .forGetter(PiglinData::inventory)
    ).apply(instance,PiglinData::new));


    public static PiglinData fromPiglin(PiglinEntity piglin){
        Text customName=piglin.getCustomName();
        if(customName==null){
            customName=Text.of("");
        }
        boolean isBaby=piglin.isBaby();
        float health=piglin.getHealth();

        DefaultedList<ItemStack> armorItems=DefaultedList.of();
        for(ItemStack item:piglin.getArmorItems()){
            armorItems.add((item==null||item.isEmpty()) ? new ItemStack(Items.COBBLESTONE) : item);
        }

        DefaultedList<ItemStack> handItems=DefaultedList.of();
        for(ItemStack item:piglin.getHandItems()){
            handItems.add((item==null||item.isEmpty()) ? new ItemStack(Items.COBBLESTONE) : item);
        }

        DefaultedList<ItemStack> inventory=DefaultedList.of();
        SimpleInventory inv=piglin.getInventory();
        for (int i=0;i<inv.size();i++){
            ItemStack item=inv.getStack((i));
            inventory.add((item==null||item.isEmpty()) ? new ItemStack(Items.COBBLESTONE) : item);
        }

        return new PiglinData(customName,isBaby,health,armorItems,handItems,inventory);
    }
}