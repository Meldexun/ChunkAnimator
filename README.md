# Chunk Animator [![](https://cf.way2muchnoise.eu/versions/chunkanimator.svg)![](http://cf.way2muchnoise.eu/full_chunkanimator_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/chunkanimator/)
When new chunks are loaded in vanilla minecraft they simply "appear instantly", with this mod they can be animated to appear from below, above, or the side. This mod shouldn't impact FPS considerably because all it does is render chunks "somewhere else".

This is a fork of [lumien231](https://github.com/lumien231)’s [Chunk Animator](https://github.com/lumien231/Chunk-Animator) which includes support for newer Minecraft versions, specifically 1.16.4/5 at the moment. I also plan on adding new features as requested (for any version), and if enough people want it I will port to other versions too.

Downlaod this fork from the [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/chunkanimator).

## Usage
Once you install this mod it will be in mode 0, that means that all chunks will appear from below. In the config file you can switch between 5 modes and also change the duration of the animation.  In addition you can also enable an option that makes chunks that are close to the player not animate.

You can also change the easing function that is used in the animation, [this](https://easings.net) is a website where you can compare them.

To learn more about how the animations can be configured, visit [this page](https://github.com/Harleyoc1/ChunkAnimator/wiki/Config) on the config.

## OptiFine
OptiFine is supported, however for the chunks to be animated you will need to make sure the `render regions` option is disabled (set to `false`). This can be found under the Performance screen in Video Settings, or as `ofRenderRegions` in the `optionsof.txt` in your Minecraft directory.

## Bugs and Feature Requests
If you find any bugs or have an idea for a new feature, please [open a new issue](https://github.com/Harleyoc1/ChunkAnimator/issues/new/choose) using the relevant template. For any other discussion or support, please use the [Discussions tab](https://github.com/Harleyoc1/ChunkAnimator/discussions).
