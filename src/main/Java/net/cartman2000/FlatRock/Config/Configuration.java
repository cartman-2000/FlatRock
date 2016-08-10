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

import net.cartman2000.FlatRock.FlatRock;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by cartman-2000 on 8/8/2016.
 */
public class Configuration {
    private ConfigurationNode rootNode = null;
    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    private File defaultConfig;
    private MainConfig mainConfig;
    private BlacklistedWorldConfig blacklistedWorldConfig;
    private BlockConfig blockConfig;


    public Configuration(File defaultConfig, ConfigurationLoader<CommentedConfigurationNode> configManager) {
        this.configManager = configManager;
        this.defaultConfig = defaultConfig;
        initialize();
    }

    private void initialize() {
        try {
            // Create the file if it doesn't exist
            if (!defaultConfig.exists()) {
                defaultConfig.getParentFile().mkdirs();
                defaultConfig.createNewFile();
                rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());
                FlatRock.getLogger().info("Creating a new config file for FlatRock at mods/FlatRock/FlatRock.conf");
            }
            else {
                rootNode = configManager.load();
            }

            mainConfig = new MainConfig(rootNode);
            blacklistedWorldConfig = new BlacklistedWorldConfig(rootNode);
            blockConfig = new BlockConfig(rootNode);

            // Save
            try {
                configManager.save(rootNode);
            } catch(IOException e) {
                // @todo handle properly
                e.printStackTrace();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }
    public BlacklistedWorldConfig getBlacklistedWorldConfig() {
        return blacklistedWorldConfig;
    }
    public BlockConfig getBlockConfig() {
        return blockConfig;
    }

    public void reload() throws IOException {
        initialize();
    }
}
