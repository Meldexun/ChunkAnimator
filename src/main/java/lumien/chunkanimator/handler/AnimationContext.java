package lumien.chunkanimator.handler;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;

/**
 * Holds context parameters required for animating chunks.
 *
 * @author Harley O'Connor
 */
public record AnimationContext(
        ChunkRenderDispatcher.RenderChunk renderChunk,
        Uniform uniform,
        float x,
        float y,
        float z,
        AnimationHandler.AnimationData animationData,
        BlockPos origin,
        float timeDif,
        double horizonHeight
) { }
