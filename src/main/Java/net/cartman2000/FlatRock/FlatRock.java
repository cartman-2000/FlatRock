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
package net.cartman2000.FlatRock;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import net.cartman2000.FlatRock.Config.Configuration;
import net.cartman2000.FlatRock.Listeners.PlayerMovement;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cartman-2000 on 8/8/2016.
 */

@Plugin(id = "flatrock", name = "FlatRock", version = "1.0.0", description = "A plugin for flattening bedrock.")
final public class FlatRock {
    private static HashMap<Player, Vector3d> playerLocations = new HashMap<>();
    private static Configuration config;
    private static FlatRock flatRock;
    @Inject
    private Game game;
    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    private PluginContainer flatRockPContainer;


    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        flatRock = this;

        logger.info("FlatRock plugin initializing.");
        // Load config
        config = new Configuration(defaultConfig, configManager);
        registerEventListeners(game.getEventManager());
        logger.info("Finished Initializing plugin.");
    }

    /**
     * Set up the events the plugin uses.
     * @param eventManager
     */
    private void registerEventListeners(EventManager eventManager) {
        eventManager.unregisterListeners(this);
        eventManager.registerListeners(this, new PlayerMovement());
    }

    /**
     * Returns the Logger instance for this plugin.
     * @return Logger instance
     */
    public static Logger getLogger() {
        return flatRock.logger;
    }

    /**
     * Returns the plugin container for this mod.
     * @return FlatRock's plugin container.
     */
    public static PluginContainer getPluginContainer() {
        return flatRock.flatRockPContainer;
    }

    /**
     * Get Main class.
     * @return
     */
    public static FlatRock getFlatRock() {
        return flatRock;
    }

    /**
     * Returnes the configuration for this plugin.
     * @return Configuration
     */
    public static Configuration getConfig() {
        return config;
    }

    /**
     * Get Game.
     * @return
     */
    public static Game getGame() {
        return flatRock.game;
    }

    /**
     * Get the map of last player locations.
     * @return Returns player locations map.
     */
    public static Map<Player, Vector3d> getPlayerLocations() {
        return playerLocations;
    }

    /**
     * Is Debug mode enabled?
     * @return
     */
    public static boolean isDebug() {
        return config.getMainConfig().isDebug();
    }
}
