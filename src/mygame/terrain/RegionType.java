/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import mygame.terrain.blocks.TerrainBlock;

/**
 *
 * @author joshcutler
 */
public abstract class RegionType {
    protected int _temperature;

    /**
     * @return the _temperature
     */
    public int getTemperature() {
        return _temperature;
    }

    /**
     * @param temperature the _temperature to set
     */
    public void setTemperature(int temperature) {
        this._temperature = temperature;
    }

    public abstract TerrainBlock getBlock(String name, Vector3f center, AssetManager assetManager, TerrainBlock top, TerrainBlock bottom, TerrainBlock left, TerrainBlock right);
}