package org.vivecraft.client_vr;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import org.vivecraft.client.Xplat;

public class ItemTags {
    public final static Tag.Named<Item> VIVECRAFT_ARROWS = tag("arrows");

    public final static Tag.Named<Item> VIVECRAFT_BRUSHES = tag("brushes");

    public final static Tag.Named<Item> VIVECRAFT_COMPASSES = tag("compasses");

    public final static Tag.Named<Item> VIVECRAFT_CROSSBOWS = tag("crossbows");

    public final static Tag.Named<Item> VIVECRAFT_FISHING_RODS = tag("fishing_rods");

    public final static Tag.Named<Item> VIVECRAFT_FOOD_STICKS = tag("food_sticks");

    public final static Tag.Named<Item> VIVECRAFT_HOES = tag("hoes");

    public final static Tag.Named<Item> VIVECRAFT_MAPS = tag("maps");

    public final static Tag.Named<Item> VIVECRAFT_SCYTHES = tag("scythes");

    public final static Tag.Named<Item> VIVECRAFT_SHIELDS = tag("shields");

    public final static Tag.Named<Item> VIVECRAFT_SPEARS = tag("spears");

    public final static Tag.Named<Item> VIVECRAFT_SWORDS = tag("swords");

    public final static Tag.Named<Item> VIVECRAFT_TELESCOPE = tag("telescope");

    public final static Tag.Named<Item> VIVECRAFT_THROW_ITEMS = tag("throw_items");

    public final static Tag.Named<Item> VIVECRAFT_TOOLS = tag("tools");

    private static Tag.Named<Item> tag(String name) {
        return Xplat.getItemTag(name);
    }
}
