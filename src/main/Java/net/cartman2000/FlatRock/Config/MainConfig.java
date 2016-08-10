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

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Created by cartman-2000 on 8/9/2016.
 */
public class MainConfig {
    private double minDistance;
    private int radius;
    private boolean useReplacementList;
    private boolean fixHoles;
    private boolean enableFlatten;
    private boolean debug;

    public MainConfig(ConfigurationNode rootNode) {
        if (rootNode.getNode("Main", "MinimumDistance").isVirtual()) {
            rootNode.getNode("Main", "MinimumDistance").setValue(1.0);
        }
        minDistance = rootNode.getNode("Main", "MinimumDistance").getInt();

        if (rootNode.getNode("Main", "Radius").isVirtual()) {
            rootNode.getNode("Main", "Radius").setValue(4);
        }
        radius = rootNode.getNode("Main", "Radius").getInt();

        if (rootNode.getNode("Main", "Use-Block-List").isVirtual()) {
            rootNode.getNode("Main", "Use-Block-List").setValue(true);
        }
        useReplacementList = rootNode.getNode("Main", "Use-Block-List").getBoolean();

        if (rootNode.getNode("Main", "Fix-Holes").isVirtual()) {
            rootNode.getNode("Main", "Fix-Holes").setValue(true);
        }
        fixHoles = rootNode.getNode("Main", "Fix-Holes").getBoolean();

        if (rootNode.getNode("Main", "Enable-Flatten").isVirtual()) {
            rootNode.getNode("Main", "Enable-Flatten").setValue(true);
        }
        enableFlatten = rootNode.getNode("Main", "Enable-Flatten").getBoolean();

        if (rootNode.getNode("Main", "Debug").isVirtual()) {
            rootNode.getNode("Main", "Debug").setValue(false);
        }
        debug = rootNode.getNode("Main", "Debug").getBoolean();
    }

    public double getMinDistance() {
        return minDistance;
    }
    public int getRadius() {
        return radius;
    }
    public void setUseReplacementList(boolean useReplacementList) {
        this.useReplacementList = useReplacementList;
    }
    public boolean isUseReplacementList() {
        return useReplacementList;
    }
    public boolean isFixHoles() {
        return fixHoles;
    }
    public boolean isEnableFlatten() {
        return enableFlatten;
    }
    public boolean isDebug() {
        return debug;
    }
}
