/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author joshcutler
 */
public class GrassBlock extends TerrainBlock {

    public GrassBlock(String name, Vector3f center, AssetManager assetManager)
    {
        super(name, center, assetManager);
        material.setColor("m_Color", ColorRGBA.Green);
    }
}