/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame.objects.vegetation;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Cylinder;

/**
 *
 * @author joshcutler
 */
public class Tree implements Vegetation
{
    protected Geometry _geometry;
    protected Material _material;
    protected Mesh _mesh;
    protected int _age;
    protected int _growth_rate; //Number of days to grow 1 unit
    protected boolean _update_visuals;

    public Tree(String name, int x, int y, AssetManager _asset_manager)
    {
        _mesh = new Cylinder(5, 5, 0.2f, 1, true);

        _material =  new Material(_asset_manager, "Common/MatDefs/Misc/SolidColor.j3md");
        _material.setColor("m_Color", ColorRGBA.Orange);
        _geometry = new Geometry(name, _mesh);
        _geometry.setMaterial(_material);
        _geometry.setLocalTranslation(new Vector3f(x, y, 1));
        _age = 0;
        _growth_rate = 10;
    }

    public Geometry getGeometry()
    {
        return _geometry;
    }

    public void updateState(boolean day_passed)
    {
        if (day_passed)
        {
            _age++;
            _update_visuals = true;
        }
    }

    public void updateVisuals()
    {
        if (_update_visuals)
        {
            float scale = (float)_age / (float)_growth_rate;
            _geometry.setLocalScale(1, 1, scale);
        }
    }
}
