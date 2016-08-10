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
package net.cartman2000.FlatRock.Listeners;

import com.flowpowered.math.vector.Vector3d;
import net.cartman2000.FlatRock.BlockProcessing.AtLocation;
import net.cartman2000.FlatRock.BlockProcessing.BlockProcess;
import net.cartman2000.FlatRock.FlatRock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.DimensionTypes;

/**
 * Created by cartman-2000 on 8/9/2016.
 */
public class PlayerMovement {
    @Listener
    public void onPlayerMove(MoveEntityEvent event) {
        if (event.getTargetEntity() instanceof Player) {
            Player player = (Player) event.getTargetEntity();
            if (FlatRock.getConfig().getBlacklistedWorldConfig().getMapList().contains(player.getWorld().getName())) {
                // Exit here, this world has been blacklisted.
                return;
            }
            if (player.getWorld().getDimension().getType().equals(DimensionTypes.THE_END)) {
                // Exit if the player is in the end, no processing is supposed to take place here.
                return;
            }
            if (!FlatRock.getPlayerLocations().containsKey(player)) {
                FlatRock.getPlayerLocations().put(player, player.getLocation().getPosition());
            }

            Vector3d newLocation = player.getLocation().getPosition();
            Vector3d oldLocation;
            // Process the top of the map only in the Nether dimension.
            // TODO: 8/10/2016 Find a way to get the generation height of the nether dimension.
            if (player.getLocation().getBlockY() > 127 - 10 && player.getWorld().getDimension().getType().equals(DimensionTypes.NETHER)) {
                oldLocation = FlatRock.getPlayerLocations().get(player);
                if (newLocation.distance(oldLocation) > FlatRock.getConfig().getMainConfig().getMinDistance()) {
                    FlatRock.getPlayerLocations().put(player, newLocation);
                    // process for nether.
                    BlockProcess.process(player, AtLocation.TOP);
                }
            }
            if (player.getLocation().getBlockY() < 10) {
                oldLocation = FlatRock.getPlayerLocations().get(player);
                if (newLocation.distance(oldLocation) > FlatRock.getConfig().getMainConfig().getMinDistance()) {
                    FlatRock.getPlayerLocations().put(player, newLocation);
                    // process the blocks at the bottom of the map for overworld and nether.
                    BlockProcess.process(player, AtLocation.BOTTOM);
                }
            }
        }
    }
}
