package lumien.chunkanimator;

import lumien.chunkanimator.config.ChunkAnimatorConfig;
import lumien.chunkanimator.handler.AnimationHandler;
import lumien.chunkanimator.handler.LevelEventHandler;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

/**
 * @author lumien231
 */
@Mod(ChunkAnimator.MOD_ID)
public final class ChunkAnimator {

	public static final String MOD_ID = "chunkanimator";

	public static ChunkAnimator instance;

	public AnimationHandler animationHandler;

	public ChunkAnimator() {
		instance = this;

		final var loadingContext = ModLoadingContext.get();
		loadingContext.registerExtensionPoint(
				IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true)
		);
		loadingContext.registerConfig(ModConfig.Type.CLIENT, ChunkAnimatorConfig.SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
	}

	/**
	 * Performs setup tasks that should only be run on the client. {@link ChunkRenderDispatcher.RenderChunk#setOrigin(int, int, int)}
	 *
	 * @param event The {@link FMLClientSetupEvent} instance.
	 */
	private void setupClient(final FMLClientSetupEvent event) {
		this.animationHandler = new AnimationHandler();

		MinecraftForge.EVENT_BUS.register(new LevelEventHandler());
	}

}
