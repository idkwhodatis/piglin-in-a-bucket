package com.idkwhodatis.piglininabucket.models;

import java.util.List;

import com.idkwhodatis.piglininabucket.ModRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;


public class BucketOfPiglinItem extends Item{
    public BucketOfPiglinItem(Settings settings){
        super(settings);
    }
    
    @Override
    public void appendTooltip(ItemStack item,TooltipContext context,List<Text> tooltip,TooltipType type){
        if (item.contains(ModRegistry.PIGLIN_DATA)){
            PiglinData piglinData=item.get(ModRegistry.PIGLIN_DATA);
            tooltip.addAll(piglinData.getTooltip());
        }
    }
}
