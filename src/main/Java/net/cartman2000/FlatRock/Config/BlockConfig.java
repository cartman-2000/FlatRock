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
package net.cartman2000.FlatRock.Config;

import net.cartman2000.FlatRock.BlockProcessing.BlockProbability;
import net.cartman2000.FlatRock.FlatRock;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by cartman-2000 on 8/9/2016.
 */

public class BlockConfig {
    private Map<BlockType, BlockProbability> blockTypeList = new HashMap<>();
    private float maxProbabilityValue = 0.0f;

    public BlockConfig(ConfigurationNode rootNode) {
        float num = 0;
        if (rootNode.getNode("Blocks").isVirtual() || rootNode.getNode("Blocks").getChildrenMap().size() == 0) {
            FlatRock.getLogger().info("Populating block list in config.");
            rootNode.getNode("Blocks", "minecraft:stone").setValue(10000.0);
            rootNode.getNode("Blocks", "minecraft:diamond_ore").setValue(10.0);
            rootNode.getNode("Blocks", "minecraft:coal_ore").setValue(50.0);
            rootNode.getNode("Blocks", "minecraft:iron_ore").setValue(40.0);
            rootNode.getNode("Blocks", "minecraft:gold_ore").setValue(25.0);
            rootNode.getNode("Blocks", "minecraft:redstone_ore").setValue(25.0);
            rootNode.getNode("Blocks", "minecraft:lapis_ore").setValue(25.0);
            rootNode.getNode("Blocks", "minecraft:lava").setValue(5.5);
        }
        for (Object entry : rootNode.getNode("Blocks").getChildrenMap().keySet()) {
            Optional<BlockType> optBlock = Sponge.getRegistry().getType(BlockType.class, entry.toString());
            // Run validation checks before adding the block to the map.
            if (!optBlock.isPresent()) {
                FlatRock.getLogger().warn("Block ID: %1s is invalid, skipping.", entry.toString());
                continue;
            }
            if (rootNode.getNode("Blocks", entry.toString()).isVirtual()) {
                FlatRock.getLogger().warn("Weight value missing for Block ID: %1s, skipping.", entry.toString());
                continue;
            }
            float value = rootNode.getNode("Blocks", entry.toString()).getFloat();
            if (value <= 0) {
                FlatRock.getLogger().warn("Weight value is zero, or negative, for Block ID: %1s, skipping.", entry.toString());
                continue;
            }

            blockTypeList.put(optBlock.get(), new BlockProbability(num, num + value));
            num += value;
        }
        maxProbabilityValue = num;
        // Check to see if there's any records in the block type list map, and disable the feature if it's empty.
        if (blockTypeList.size() == 0) {
            FlatRock.getLogger().warn("There's no valid blocks in the replacement list, disabling this feature.");
            FlatRock.getConfig().getMainConfig().setUseReplacementList(false);
        }
    }

    public Map<BlockType, BlockProbability> getBlockList() {
        return blockTypeList;
    }
    public float getMaxProbabilityValue() {
        return maxProbabilityValue;
    }

}
