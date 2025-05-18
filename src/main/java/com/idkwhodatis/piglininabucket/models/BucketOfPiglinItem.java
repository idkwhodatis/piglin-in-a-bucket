package com.idkwhodatis.piglininabucket.models;

import java.util.List;

import com.idkwhodatis.piglininabucket.ModRegistry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


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

    @Override
    public ActionResult useOnBlock(ItemUsageContext context){
        World world=context.getWorld();
        BlockPos pos=context.getBlockPos().offset(context.getSide());
        ItemStack item=context.getStack();

        if(world.isClient){
            return ActionResult.SUCCESS;
        }
        if(!item.contains(ModRegistry.PIGLIN_DATA)){
            return ActionResult.SUCCESS;
        }

        PiglinEntity piglin = EntityType.PIGLIN.spawn((ServerWorld) context.getWorld(),null,context.getBlockPos().offset(context.getSide()),SpawnReason.BUCKET,true,false);

        PiglinData piglinData=item.get(ModRegistry.PIGLIN_DATA);
        piglinData.toPiglin(piglin);

        if(!context.getPlayer().getAbilities().creativeMode){
            context.getPlayer().setStackInHand(context.getHand(),new ItemStack(Items.BUCKET));
        }

        world.playSound(null,pos,SoundEvents.ITEM_BUCKET_EMPTY,SoundCategory.PLAYERS,1.0F,1.0F);

        return ActionResult.SUCCESS;
    }
}
