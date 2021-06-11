package lumien.chunkanimator.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import lumien.chunkanimator.ChunkAnimator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author Harley O'Connor
 */
@Mixin(WorldRenderer.class)
public final class WorldRendererMixin {

    private static final String RENDER_CHUNK_LAYER = "renderChunkLayer";

    @Group(name = RENDER_CHUNK_LAYER, min = 1, max = 1)
    @Inject(method = RENDER_CHUNK_LAYER, at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$ChunkRender;getOrigin()Lnet/minecraft/util/math/BlockPos;"
    ), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void preRenderChunk(RenderType renderType, MatrixStack matrixStack, double x, double y, double z, CallbackInfo ci,
                               boolean notTranslucent, ObjectListIterator<WorldRenderer.LocalRenderInformationContainer> renderChunkIterator,
                               WorldRenderer.LocalRenderInformationContainer currentRenderInfo, ChunkRenderDispatcher.ChunkRender chunkRender,
                               VertexBuffer vertexBuffer) {
        ChunkAnimator.animationHandler().preRender(chunkRender, matrixStack);
    }

    /**
     * OptiFine version of {@link #preRenderChunk(RenderType, MatrixStack, double, double, double, CallbackInfo, boolean, ObjectListIterator, WorldRenderer.LocalRenderInformationContainer, ChunkRenderDispatcher.ChunkRender, VertexBuffer)}
     * to accommodate slightly different locals.
     */
    @Dynamic
    @Group(name = RENDER_CHUNK_LAYER, min = 1, max = 1)
    @Inject(method = RENDER_CHUNK_LAYER, at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$ChunkRender;getOrigin()Lnet/minecraft/util/math/BlockPos;"
    ), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void preRenderChunk(RenderType renderType, MatrixStack matrixStack, double x, double y, double z, CallbackInfo ci,
                               boolean shaders, boolean smartAnimations, boolean notTranslucent,
                               ObjectListIterator<WorldRenderer.LocalRenderInformationContainer> renderChunkIterator,
                               WorldRenderer.LocalRenderInformationContainer currentRenderInfo, ChunkRenderDispatcher.ChunkRender chunkRender,
                               VertexBuffer vertexBuffer) {
        ChunkAnimator.animationHandler().preRender(chunkRender, null);
    }

}
