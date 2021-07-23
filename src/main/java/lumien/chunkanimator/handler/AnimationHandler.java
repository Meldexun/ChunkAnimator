package lumien.chunkanimator.handler;

import com.mojang.blaze3d.shaders.Uniform;
import lumien.chunkanimator.config.ChunkAnimatorConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import penner.easing.*;

import java.util.Objects;
import java.util.WeakHashMap;

/**
 * This class handles setting up and rendering the animations.
 *
 * @author lumien231
 */
@OnlyIn(Dist.CLIENT)
public final class AnimationHandler {

	private final Minecraft mc = Minecraft.getInstance();
	private final WeakHashMap<ChunkRenderDispatcher.RenderChunk, AnimationData> timeStamps = new WeakHashMap<>();

	public void preRender(final ChunkRenderDispatcher.RenderChunk renderChunk, final Uniform uniform,
						  final float x, final float y, final float z) {
		final AnimationData animationData = timeStamps.get(renderChunk);

		if (animationData == null) {
			uniform.set(x, y, z);
			return;
		}

		final int mode = ChunkAnimatorConfig.MODE.get();
		final int animationDuration = ChunkAnimatorConfig.ANIMATION_DURATION.get();

		long time = animationData.timeStamp;

		// If preRender hasn't been called on this chunk yet, prepare to start the animation.
		if (time == -1L) {
			time = System.currentTimeMillis();

			animationData.timeStamp = time;

			// If using mode 4, set chunkFacing.
			if (mode == 4) {
				animationData.chunkFacing = this.mc.player != null ?
						this.getChunkFacing(this.getZeroedPlayerPos(this.mc.player).subtract(this.getZeroedCenteredChunkPos(renderChunk.getOrigin()))) : Direction.NORTH;
			}
		}

		final long timeDif = System.currentTimeMillis() - time;

		if (timeDif < animationDuration) {
			final int chunkY = renderChunk.getOrigin().getY();
			final int animationMode = mode == 2 ? (chunkY < Objects.requireNonNull(this.mc.level)
					.getLevelData().getHorizonHeight(this.mc.level) ? 0 : 1) : mode == 4 ? 3 : mode;

			switch (animationMode) {
				case 0 -> uniform.set(x, y - chunkY + this.getFunctionValue(timeDif, 0, chunkY, animationDuration), z);
				case 1 -> uniform.set(x, y + 256 - chunkY - this.getFunctionValue(timeDif, 0, 256 - chunkY, animationDuration), z);
				case 3 -> {
					Direction chunkFacing = animationData.chunkFacing;
					if (chunkFacing != null) {
						final Vec3i vec = chunkFacing.getNormal();
						final float mod = -(200F - this.getFunctionValue(timeDif, 0, 200, animationDuration));

						uniform.set(x + vec.getX() * mod, y, z +  vec.getZ() * mod);
					}
				}
			}
		} else {
			uniform.set(x, y, z);
			this.timeStamps.remove(renderChunk);
		}
	}

	/**
	 * Gets the function value for the given parameters based on {@link ChunkAnimatorConfig#EASING_FUNCTION}.
	 *
	 * @param t The first function argument.
	 * @param b The second function argument.
	 * @param c The third function argument.
	 * @param d The fourth function argument.
	 * @return The return value of the function.
	 */
	private float getFunctionValue(final float t, @SuppressWarnings("SameParameterValue") final float b, final float c, final float d) {
		return switch (ChunkAnimatorConfig.EASING_FUNCTION.get()) {
			case 0 -> // Linear
					Linear.easeOut(t, b, c, d);
			case 1 -> // Quadratic Out
					Quad.easeOut(t, b, c, d);
			case 2 -> // Cubic Out
					Cubic.easeOut(t, b, c, d);
			case 3 -> // Quartic Out
					Quart.easeOut(t, b, c, d);
			case 4 -> // Quintic Out
					Quint.easeOut(t, b, c, d);
			case 5 -> // Expo Out
					Expo.easeOut(t, b, c, d);
			case 6 -> // Sin Out
					Sine.easeOut(t, b, c, d);
			case 7 -> // Circle Out
					Circ.easeOut(t, b, c, d);
			case 8 -> // Back
					Back.easeOut(t, b, c, d);
			case 9 -> // Bounce
					Bounce.easeOut(t, b, c, d);
			case 10 -> // Elastic
					Elastic.easeOut(t, b, c, d);
			default -> Sine.easeOut(t, b, c, d);
		};

	}

	public void setOrigin(final ChunkRenderDispatcher.RenderChunk renderChunk, final BlockPos position) {
		if (this.mc.player == null)
			return;

		final BlockPos zeroedPlayerPos = this.getZeroedPlayerPos(this.mc.player);
		final BlockPos zeroedCenteredChunkPos = this.getZeroedCenteredChunkPos(position);

		if (!ChunkAnimatorConfig.DISABLE_AROUND_PLAYER.get() || zeroedPlayerPos.distSqr(zeroedCenteredChunkPos) > (64 * 64)) {
			timeStamps.put(renderChunk, new AnimationData(-1L, ChunkAnimatorConfig.MODE.get() == 3 ?
					this.getChunkFacing(zeroedPlayerPos.subtract(zeroedCenteredChunkPos)) : null));
		} else {
			timeStamps.remove(renderChunk);
		}
	}

	/**
	 * Gets the given player's position, setting their {@code y-coordinate} to {@code 0}.
	 *
	 * @param player The {@link LocalPlayer} instance.
	 * @return The zeroed {@link BlockPos}.
	 */
	private BlockPos getZeroedPlayerPos (final LocalPlayer player) {
		final BlockPos playerPos = player.blockPosition();
		return playerPos.offset(0, -playerPos.getY(), 0);
	}

	/**
	 * Gets the given {@link BlockPos} for the chunk, setting its {@code y-coordinate} to
	 * {@code 0} and offsetting its {@code x} and {@code y-coordinate} to by {@code 8}.
	 *
	 * @param position The {@link BlockPos} of the chunk.
	 * @return The zeroed, centered {@link BlockPos}.
	 */
	private BlockPos getZeroedCenteredChunkPos(final BlockPos position) {
		return position.offset(8, -position.getY(), 8);
	}

	/**
	 * Gets the direction the chunk is facing based on the given {@link Vec3i}
	 * from the relevant position to the chunk.
	 *
	 * @param dif The {@link Vec3i} distance from the relevant position to the chunk.
	 * @return The {@link Direction} of the chunk relative to the {@code dif}.
	 */
	private Direction getChunkFacing(final Vec3i dif) {
		int difX = Math.abs(dif.getX());
		int difZ = Math.abs(dif.getZ());

		return difX > difZ ? dif.getX() > 0 ? Direction.EAST : Direction.WEST : dif.getZ() > 0 ? Direction.SOUTH : Direction.NORTH;
	}

	public void clear () {
		// These should be cleared by GC, but just in case.
		this.timeStamps.clear();
	}

	private static class AnimationData {
		public long timeStamp;
		public Direction chunkFacing;

		public AnimationData(final long timeStamp, final Direction chunkFacing) {
			this.timeStamp = timeStamp;
			this.chunkFacing = chunkFacing;
		}
	}

}
