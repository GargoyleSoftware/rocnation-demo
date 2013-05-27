package co.gargoyle.rocnation.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 *
 * @author mwho
 * Maintains extrinsic info of a tab's construct
 */
public class TabInfo {
     public String tag;
     public Class<?> clss;
     public Bundle args;
     public Fragment fragment;
     public TabInfo(String tag, Class<?> clazz, Bundle args) {
         this.tag = tag;
         this.clss = clazz;
         this.args = args;
     }

}