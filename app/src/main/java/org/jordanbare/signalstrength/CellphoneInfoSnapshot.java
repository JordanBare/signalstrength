package org.jordanbare.signalstrength;

import android.location.Location;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jordanbare on 1/3/18.
 */

public class CellphoneInfoSnapshot {
    final private Location mLocation;
    final private ArrayList<Map> mCellInfoMapList;
    final private String mNetworkType;
    final private String mPhoneRadioType;

    public CellphoneInfoSnapshot(Location location, ArrayList<Map> cellInfoMapList,
                                 String networkType, String phoneRadioType){
        this.mLocation = location;
        this.mCellInfoMapList = cellInfoMapList;
        this.mNetworkType = networkType;
        this.mPhoneRadioType = phoneRadioType;
    }

    public Location getLocation() {
        return mLocation;
    }

    public ArrayList<Map> getCellInfoMap() {
        return mCellInfoMapList;
    }

    public String getNetworkType() {
        return mNetworkType;
    }

    public String getPhoneRadioType() {
        return mPhoneRadioType;
    }
}
