package com.idkwhodatis.piglininabucket.models;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.collection.DefaultedList;

public record PiglinData(
    Text customName,
    boolean isBaby,
    float health,
    DefaultedList<ItemStack> armorItems,
    DefaultedList<ItemStack> handItems,
    DefaultedList<ItemStack> inventory
){
    public Text getTooltip(){
        Text name=(customName!=null&&!customName.getString().isEmpty()) ? customName : null;
        String tooltip=isBaby?"Baby":"Adult"+", "+String.format("%.1f",health)+" HP";
        if(name!=null){
            tooltip=name+"\n"+tooltip;
        }
        return Text.literal(tooltip);
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
        TextCodecs.CODEC.fieldOf("name").forGetter(PiglinData::customName),
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
        boolean isBaby=piglin.isBaby();
        float health=piglin.getHealth();

        DefaultedList<ItemStack> armorItems=DefaultedList.of();
        piglin.getArmorItems().forEach(armorItems::add);

        DefaultedList<ItemStack> handItems=DefaultedList.of();
        piglin.getHandItems().forEach(handItems::add);

        DefaultedList<ItemStack> inventory=DefaultedList.of();
        SimpleInventory inv=piglin.getInventory();
        for (int i=0;i<inv.size();i++){
            inventory.add(inv.getStack(i));
        }

        return new PiglinData(customName,isBaby,health,armorItems,handItems,inventory);
    }
}