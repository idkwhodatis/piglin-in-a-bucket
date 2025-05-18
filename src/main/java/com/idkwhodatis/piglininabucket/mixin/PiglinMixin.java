package com.idkwhodatis.piglininabucket.mixin;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.idkwhodatis.piglininabucket.ModRegistry;
import com.idkwhodatis.piglininabucket.models.PiglinData;


@Mixin(PiglinEntity.class)
public class PiglinMixin{
	@Inject(method="interactMob",at=@At("HEAD"),cancellable=true)
	private void onBucketUse(PlayerEntity player,Hand hand,CallbackInfoReturnable<ActionResult> cir){
		if(player.getWorld().isClient){
			return;
		}

		ItemStack handItem=player.getStackInHand(hand);
		if(!handItem.isOf(Items.BUCKET)){
			return;
		}

		PiglinEntity piglin=(PiglinEntity)(Object)this;
		PiglinData piglinData=PiglinData.fromPiglin(piglin);

		ItemStack bucketOfPiglin=new ItemStack(ModRegistry.BUCKET_OF_PIGLIN);
        bucketOfPiglin.set(ModRegistry.PIGLIN_DATA,piglinData);
        player.setStackInHand(hand,bucketOfPiglin);

        piglin.discard();

		player.getWorld().playSound(null,piglin.getBlockPos(),SoundEvents.ITEM_BUCKET_FILL,SoundCategory.PLAYERS,1.0f,1.0f);

		cir.setReturnValue(ActionResult.SUCCESS);
	}
}