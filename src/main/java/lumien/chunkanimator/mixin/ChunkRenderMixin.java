package lumien.chunkanimator.mixin;

import lumien.chunkanimator.ChunkAnimator;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Harley O'Connor
 */
@Mixin(ChunkRenderDispatcher.ChunkRender.class)
public final class ChunkRenderMixin {

    @Inject(method = "setOrigin", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$ChunkRender;reset()V"
    ))
    public void setOrigin(int x, int y, int z, CallbackInfo ci) {
        ChunkAnimator.animationHandler().setOrigin((ChunkRenderDispatcher.ChunkRender) (Object) this, new BlockPos(x, y, z));
    }

}
