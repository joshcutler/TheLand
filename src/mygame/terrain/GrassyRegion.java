/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import mygame.terrain.blocks.DirtBlock;
import mygame.terrain.blocks.GrassBlock;
import mygame.terrain.blocks.StoneBlock;
import mygame.terrain.blocks.TerrainBlock;

/**
 *
 * @author joshcutler
 */
public class GrassyRegion extends RegionType
{
    public GrassyRegion()
    {
        _temperature = 75;
    }

    public String getName()
    {
        return "GrassyRegion";
    }

    @Override
    public TerrainBlock getBlock(String name, Vector3f center, AssetManager assetManager, TerrainBlock top, TerrainBlock bottom, TerrainBlock left, TerrainBlock right)
    {
        float base_chance_grass = 80;
        float base_chance_dirt = 20;
        float base_chance_stone = 0;

        if (top != null)
        {
            String class_name = top.getName();
            
            if (class_name.equals("DirtBlock"))
            {
                base_chance_dirt += 5;
                base_chance_grass -= 5;
            }
            else if (class_name.equals("StoneBlock"))
            {
                base_chance_stone += 30;
                base_chance_grass -= 30;
            }
        }
        if (bottom != null)
        {
            String class_name = bottom.getName();

            if (class_name.equals("DirtBlock"))
            {
                base_chance_dirt += 5;
                base_chance_grass -= 5;
            }
            else if (class_name.equals("StoneBlock"))
            {
                base_chance_stone += 30;
                base_chance_grass -= 30;
            }
        }
        if (left != null)
        {
            String class_name = left.getName();

            if (class_name.equals("DirtBlock"))
            {
                base_chance_dirt += 5;
                base_chance_grass -= 5;
            }
            else if (class_name.equals("StoneBlock"))
            {
                base_chance_stone += 30;
                base_chance_grass -= 30;
            }
        }
        if (right != null)
        {
            String class_name = right.getName();

            if (class_name.equals("DirtBlock"))
            {
                base_chance_dirt += 5;
                base_chance_grass -= 5;
            }
            else if (class_name.equals("StoneBlock"))
            {
                base_chance_stone += 30;
                base_chance_grass -= 30;
            }
        }

        double rand = Math.random()*100;
        if (rand > base_chance_grass + base_chance_dirt)
        {
            return new StoneBlock(name, center, assetManager);
        }
        else if(rand > base_chance_grass)
        {
            return new DirtBlock(name, center, assetManager);
        }
        else
        {
            return new GrassBlock(name, center, assetManager);
        }
    }
}
