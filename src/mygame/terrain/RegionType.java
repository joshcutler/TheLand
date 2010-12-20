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

    public static RegionType getRegionType(RegionType top, RegionType right, RegionType bottom, RegionType left)
    {
        float base_chance_stone = 20;
        float base_chance_grass = 80;

        if (top != null)
        {
            if (top.getName().equals("StonyRegion"))
            {
                base_chance_stone += 5;
                base_chance_grass -= 5;
            }
        }
        if (right != null)
        {
            if (right.getName().equals("StonyRegion"))
            {
                base_chance_stone += 5;
                base_chance_grass -= 5;
            }
        }
        if (bottom != null)
        {
            if (bottom.getName().equals("StonyRegion"))
            {
                base_chance_stone += 5;
                base_chance_grass -= 5;
            }
        }
        if (left != null)
        {
            if (left.getName().equals("StonyRegion"))
            {
                base_chance_stone += 5;
                base_chance_grass -= 5;
            }
        }
        if (Math.random()*100 > base_chance_grass)
        {
            return new StonyRegion();
        }
        else
        {
            return new GrassyRegion();
        }
    }

    public abstract TerrainBlock getBlock(String name, Vector3f center, AssetManager assetManager, TerrainBlock top, TerrainBlock bottom, TerrainBlock left, TerrainBlock right);
    public abstract String getName();
}