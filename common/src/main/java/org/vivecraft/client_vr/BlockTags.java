package org.vivecraft.client_vr;

import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import org.vivecraft.client.Xplat;

public class BlockTags {
    public final static Tag.Named<Block> VIVECRAFT_CLIMBABLE = tag("climbable");
    public final static Tag.Named<Block> VIVECRAFT_CROPS = tag("crops");

    public final static Tag.Named<Block> VIVECRAFT_MUSIC_BLOCKS = tag("music_blocks");

    private static Tag.Named<Block> tag(String name) {
        return Xplat.getBlockTag(name);
    }
}
