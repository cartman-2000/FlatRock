/**
 * This file is part of FlatRock, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 cartman2000 https://github.com/cartman-2000/FlatRock
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.cartman2000.FlatRock.BlockProcessing;

import com.flowpowered.math.vector.Vector3i;
import net.cartman2000.FlatRock.FlatRock;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.World;

import java.util.Map;

/**
 * Created by cartman-2000 on 8/9/2016.
 */
public class BlockProcess {
    private BlockProcess() {
    }
    /**
     * Itterates through the blocks for flattening/Hole fixing.
     * @param player The player that initiated this.
     * @param top whether to process at the top of the map or the bottom.
     */
    public static void process(Player player, AtLocation top) {
        Vector3i blockPosition = player.getLocation().getBlockPosition();
        DimensionType dimensionType = player.getWorld().getDimension().getType();
        // Nether's generation height.
        // TODO: 8/10/2016 Find a way to get the generation height of the nether dimension.
        int maxHeight = 127;
        int maxPH = (top == AtLocation.BOTTOM ? 4 : maxHeight);
        int minPH = (top == AtLocation.BOTTOM ? 0 : maxHeight - 4);
        int radius = FlatRock.getConfig().getMainConfig().getRadius();
        World world = player.getWorld();
        int blockChangeCount = 0;

        // Inclusive radius.
        for (int x = blockPosition.getX() - radius + 1; x < blockPosition.getX() + radius; x++) {
            for (int y = minPH; y <= maxPH; y++) {
                for (int z = blockPosition.getZ() - radius + 1; z < blockPosition.getZ() + radius; z++) {
                    // Fix Holes, if enabled.
                    if ((y == maxHeight || y == 0) && FlatRock.getConfig().getMainConfig().isFixHoles()) {
                        if (!world.getBlockType(x, y, z).equals(BlockTypes.BEDROCK)) {
                            world.setBlockType(x, y, z, BlockTypes.BEDROCK, BlockChangeFlag.NONE,
                                    Cause.source(FlatRock.getFlatRock().getPluginContainer()).named(NamedCause.notifier(player)).build());
                            if (FlatRock.isDebug()) {
                                blockChangeCount++;
                            }
                            FlatRock.getLogger().info(String.format("Fixed hole in world: %1s, at: x: %2$d, y: %3$d, z: %4$d", world.getName(), x, y, z));
                        }
                    }
                    // Flatten bedrock, if enabled.
                    if (y != 0 && y != maxHeight && FlatRock.getConfig().getMainConfig().isEnableFlatten()) {
                        if (world.getBlockType(x, y, z).equals(BlockTypes.BEDROCK)) {
                            BlockType defaultReplaceBlockType = dimensionType == DimensionTypes.NETHER ? BlockTypes.NETHERRACK : BlockTypes.STONE;
                            if (FlatRock.getConfig().getMainConfig().isUseReplacementList()) {
                                BlockType newBlockType = defaultReplaceBlockType;
                                double rand = (Math.random() * FlatRock.getConfig().getBlockConfig().getMaxProbabilityValue());
                                for (Map.Entry<BlockType, BlockProbability> entry : FlatRock.getConfig().getBlockConfig().getBlockList().entrySet()) {
                                    if (entry.getValue().isInRange(rand)) {
                                        // Ignore changing the block type for the default common stone block type.
                                        if (!entry.getKey().equals(BlockTypes.STONE)) {
                                            newBlockType = entry.getKey();
                                        }
                                        if (FlatRock.isDebug()) {
                                            // Print replacement block, if not stone.
                                            if (!defaultReplaceBlockType.equals(newBlockType)) {
                                                FlatRock.getLogger().info(String.format("Replaced bedrock with: %1s", newBlockType.getId()));
                                            }
                                        }
                                        defaultReplaceBlockType = newBlockType;
                                        break;
                                    }
                                }
                            }
                            world.setBlockType(x, y, z, defaultReplaceBlockType, BlockChangeFlag.NONE,
                                    Cause.source(FlatRock.getFlatRock().getPluginContainer()).named(NamedCause.notifier(player)).build());
                            if (FlatRock.isDebug()) {
                                blockChangeCount++;
                            }
                        }
                    }
                }
            }
        }
        if (FlatRock.isDebug() && blockChangeCount != 0) {
            FlatRock.getLogger().info(String.format("Number of blocks modified in run: %1$d, initiated by: %2$s, at location: %3$s: x: %4$d, y: %5$d, z: %6$d"
                    , blockChangeCount, player.getName(), world.getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        }
    }
}
