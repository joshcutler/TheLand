/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.objects;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import mygame.objects.vegetation.Tree;
import mygame.objects.vegetation.Vegetation;

/**
 *
 * @author joshcutler
 */
public class WorldObjectRenderer
{
    HashMap _world_objects = new HashMap();
    HashMap _vegetation = new HashMap();

    AssetManager _asset_manager;
    Node _vegetation_node;

    public WorldObjectRenderer(Node root, Camera cam, InputManager im, AssetManager asset_manager)
    {
        _asset_manager = asset_manager;

        _vegetation_node = new Node();
        root.attachChild(_vegetation_node);

        //Just create one Tree
        Tree tree = new Tree("Tree", 0, 0, _asset_manager);
        setVegetation(tree, 0, 0);
        _vegetation_node.attachChild(tree.getGeometry());
    }

    private Vegetation getVegetation(int x, int y)
    {
        Object v = _vegetation.get(x + "_" + y);
        if (v != null)
        {
            return (Vegetation)v;
        }
        return null;
    }

    private void setVegetation(Vegetation v, int x, int y)
    {
        _vegetation.put(x + "_" + y, v);
    }

    public void updateObjects(boolean new_day)
    {
        this.updateVegetation(new_day);
    }

    public void updateVegetation(boolean new_day)
    {
        Set keys = _vegetation.keySet();
        Iterator ix = keys.iterator();
        while (ix.hasNext())
        {
            Vegetation v = (Vegetation)_vegetation.get(ix.next());
            v.updateState(new_day);
            v.updateVisuals();
        }
    }
}
