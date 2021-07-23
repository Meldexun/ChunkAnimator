package lumien.chunkanimator.handler;

import lumien.chunkanimator.ChunkAnimator;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles {@link WorldEvent}s, updating {@link AnimationHandler} properties when the world
 * loads/unloads.
 *
 * @author Harley O'Connor
 */
@OnlyIn(Dist.CLIENT)
public final class LevelEventHandler {

    private static final AnimationHandler HANDLER = ChunkAnimator.instance.animationHandler;

    @SubscribeEvent
    public void worldUnload (final WorldEvent.Unload event) {
        if (!(event.getWorld() instanceof ClientLevel)) {
            return;
        }

        HANDLER.clear();
    }

}
