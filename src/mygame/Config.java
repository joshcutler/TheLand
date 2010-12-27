/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame;

import java.util.logging.Logger;

/**
 *
 * @author joshcutler
 */
public class Config {
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    static final int SECONDS_PER_DAY = 10;
    //Map generation config
    public static final int BLOCK_REGION_SIZE = 25;
    public static final int BLOCK_REGION_MAX_Y = 1;
    public static final int BLOCK_REGION_MAX_X = 1;
    //Camera settings
    public static final int ZOOM_FOV_MAX = 100;
    public static final int ZOOM_FOV_MIN = 5;
    public static final boolean DISABLE_DEFAULT_CAMERA = true;
    //UI settings
    public static final boolean HUD_ENABLED = true;
}
