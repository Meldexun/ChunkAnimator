package lumien.chunkanimator.mixin;

import lumien.chunkanimator.ChunkAnimator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Harley O'Connor
 */
@Mixin(ChunkRenderDispatcher.RenderChunk.class)
public final class RenderChunkMixin {

    @Inject(method = "setOrigin", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk;reset()V"
    ))
    public void setOrigin(int x, int y, int z, CallbackInfo ci) {
        ChunkAnimator.instance.animationHandler.setOrigin(
                (ChunkRenderDispatcher.RenderChunk) (Object) this,
                new BlockPos(x, y, z)
        );
    }

}
