package ca.spottedleaf.starlight.mixin.common.chunk;

import ca.spottedleaf.starlight.common.chunk.ExtendedChunk;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.GenerationUpgradeData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements ExtendedChunk {

    /**
     * Copies the nibble data from the protochunk.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Ljava/util/function/Consumer;)V",
            at = @At("TAIL")
    )
    public void onTransitionToFull(final ServerLevel serverLevel, final ProtoChunk protoChunk,
                                   @Nullable Consumer<LevelChunk> consumer, final CallbackInfo ci) {
        this.setBlockNibbles(((ExtendedChunk)protoChunk).getBlockNibbles());
        this.setSkyNibbles(((ExtendedChunk)protoChunk).getSkyNibbles());
        this.setSkyEmptinessMap(((ExtendedChunk)protoChunk).getSkyEmptinessMap());
        this.setBlockEmptinessMap(((ExtendedChunk)protoChunk).getBlockEmptinessMap());
    }

    /**
     * Initialises the nibble arrays to default values.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/ticks/LevelChunkTicks;Lnet/minecraft/world/ticks/LevelChunkTicks;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;Lnet/minecraft/world/level/levelgen/blending/GenerationUpgradeData;)V",
            at = @At("TAIL")
    )
    public void onConstruct(Level level, ChunkPos chunkPos, UpgradeData upgradeData, LevelChunkTicks levelChunkTicks, LevelChunkTicks levelChunkTicks2, long l, LevelChunkSection[] levelChunkSections, Consumer consumer, GenerationUpgradeData generationUpgradeData, CallbackInfo ci) {
        this.setBlockNibbles(StarLightEngine.getFilledEmptyLight(level));
        this.setSkyNibbles(StarLightEngine.getFilledEmptyLight(level));
    }
}
