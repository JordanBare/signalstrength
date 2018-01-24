package org.jordanbare.signalstrength;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jordanbare on 1/3/18.
 */

public class CellInfoDataImpl {

    private final TelephonyManager telephonyManager;

        static final String CELL_INFO_KEY_IS_REGISTERED = "CELL_INFO_IS_REGISTERED",
                CELL_INFO_KEY_TIMESTAMP_VALUE = "CELL_INFO_TIMESTAMP_VALUE",
                CELL_INFO_KEY_CDMA_BSID = "CELL_INFO_CDMA_BSID",
                CELL_INFO_KEY_CDMA_BSLATITUTE= "CELL_INFO_CDMA_BSLATITUDE",
                CELL_INFO_KEY_CDMA_BSLONGITUDE = "CELL_INFO_BSLONGITUDE",
                CELL_INFO_KEY_CDMA_NETWORKID = "CELL_INFO_CDMA_NETWORKID",
                CELL_INFO_KEY_CDMA_SYSTEMID = "CELL_INFO_CDMA_SYSTEMID",
                CELL_INFO_KEY_CDMA_ASU_LEVEL = "CELL_INFO_CDMA_ASU_LEVEL",
                CELL_INFO_KEY_CDMA_RSSI_DBM = "CELL_INFO_CDMA_RSSI_DBM",
                CELL_INFO_KEY_CDMA_ECIO = "CELL_INFO_CDMA_ECIO",
                CELL_INFO_KEY_CDMA_LEVEL = "CELL_INFO_CDMA_LEVEL",
                CELL_INFO_KEY_CDMA_DBM = "CELL_INFO_CDMA_DBM",
                CELL_INFO_KEY_CDMA_EVDO_DBM = "CELL_INFO_CDMA_EVDO_DBM",
                CELL_INFO_KEY_CDMA_EVDO_ECIO = "CELL_INFO_CDMA_EVDO_ECIO",
                CELL_INFO_KEY_CDMA_EVDO_LEVEL = "CELL_INFO_CDMA_EVDO_LEVEL",
                CELL_INFO_KEY_CDMA_EVDO_SNR = "CELL_INFO_CDMA_EVDO_SNR",
                CELL_INFO_KEY_CDMA_LEVEL2 = "CELL_INFO_CDMA_LEVEL2",
                CELL_INFO_VALUE_CDMA = "CELL_INFO_VALUE_CDMA",
                CELL_INFO_KEY_GSM_CID = "CELL_INFO_GMS_CID",
                CELL_INFO_KEY_GSM_LAC = "CELL_INFO_GSM_LAC",
                CELL_INFO_KEY_GSM_MCC = "CELL_INFO_GSM_MCC",
                CELL_INFO_KEY_GSM_MNC = "CELL_INFO_GSM_MNC",
                CELL_INFO_KEY_GSM_ASU_LEVEL = "CELL_INFO_GSM_ASU_LEVEL",
                CELL_INFO_KEY_GSM_DBM = "CELL_INFO_GSM_DBM",
                CELL_INFO_KEY_GSM_LEVEL = "CELL_INFO_GSM_LEVEL",
                CELL_INFO_VALUE_GSM = "CELL_INFO_VALUE_GSM",
                CELL_INFO_KEY_LTE_CI = "CELL_INFO_LTE_CI",
                CELL_INFO_KEY_LTE_MCC = "CELL_INFO_LE_MCC",
                CELL_INFO_KEY_LTE_MNC = "CELL_INFO_LTE_MNC",
                CELL_INFO_KEY_LTE_PCI = "CELL_INFO_LTE_PCI",
                CELL_INFO_KEY_LTE_TAC = "CELL_INFO_LTE_TAC",
                CELL_INFO_KEY_LTE_ASU_LEVEL = "CELL_INFO_LTE_ASU_LEVEL",
                CELL_INFO_KEY_LTE_DBM = "CELL_INFO_LTE_DBM",
                CELL_INFO_KEY_LTE_LEVEL = "CELL_INFO_LTE_LEVEL",
                CELL_INFO_KEY_LTE_TIMING_ADVANCE = "CELL_INFO_LTE_TIMING_ADVANCE",
                CELL_INFO_VALUE_LTE = "CELL_INFO_VALUE_LTE",
                CELL_INFO_KEY_WCDMA_CID = "CELL_INFO_WCDMA_CID",
                CELL_INFO_KEY_WCDMA_LAC = "CELL_INFO_WCDMA_LAC",
                CELL_INFO_KEY_WCDMA_MCC = "CELL_INFO_WCDMA_MCC",
                CELL_INFO_KEY_WCDMA_MNC = "CELL_INFO_WCDMA_MNC",
                CELL_INFO_KEY_WCDMA_PSC = "CELL_INFO_WCDMA_PSC",
                CELL_INFO_KEY_WCDMA_ASU_LEVEL = "CELL_INFO_WCDMA_ASI_LEVEL",
                CELL_INFO_KEY_WCDMA_DBM = "CELL_INFO_WCDMA_DBM",
                CELL_INFO_KEY_WCDMA_LEVEL = "CELL_INFO_WCDMA_LEVEL",
                CELL_INFO_VALUE_WCDMA = "CELL_INFO_VALUE_WCDMA";


    public CellInfoDataImpl(Context context){
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }


    public ArrayList<Map> retrieveCellTowerData() {

        @SuppressLint("MissingPermission") List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        if (cellInfos != null) {

            ArrayList<Map> cellInfoMapList = new ArrayList<>();

            for (CellInfo cellInfo : cellInfos) {

                Map<Object, Serializable> networkValuesMap = new HashMap<>();

                // Common Fields
                networkValuesMap.put(CELL_INFO_KEY_IS_REGISTERED, (cellInfo.isRegistered()) ? "True" : "False");
                networkValuesMap.put(CELL_INFO_KEY_TIMESTAMP_VALUE, cellInfo.getTimeStamp());

                if (cellInfo instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                    // CellIdentity
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_BSID, (cellInfoCdma.getCellIdentity().getBasestationId()));
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_BSLATITUTE, cellInfoCdma.getCellIdentity().getLatitude());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_BSLONGITUDE, cellInfoCdma.getCellIdentity().getLongitude());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_NETWORKID, cellInfoCdma.getCellIdentity().getNetworkId());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_SYSTEMID, cellInfoCdma.getCellIdentity().getSystemId());
                    // CellSignalStrength
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_ASU_LEVEL, cellInfoCdma.getCellSignalStrength().getAsuLevel());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_RSSI_DBM, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_ECIO, cellInfoCdma.getCellSignalStrength().getCdmaEcio());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_LEVEL, cellInfoCdma.getCellSignalStrength().getCdmaLevel());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_DBM, cellInfoCdma.getCellSignalStrength().getDbm());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_EVDO_DBM, cellInfoCdma.getCellSignalStrength().getEvdoDbm());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_EVDO_ECIO, cellInfoCdma.getCellSignalStrength().getEvdoEcio());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_EVDO_LEVEL, cellInfoCdma.getCellSignalStrength().getEvdoLevel());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_EVDO_SNR, cellInfoCdma.getCellSignalStrength().getEvdoSnr());
                    networkValuesMap.put(CELL_INFO_KEY_CDMA_LEVEL2, cellInfoCdma.getCellSignalStrength().getLevel());
                    // Cell type
                    networkValuesMap.put(CELL_INFO_VALUE_CDMA, "CELL_INFO_CDMA");
                    cellInfoMapList.add(networkValuesMap);
                }
                else if (cellInfo instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    // CellIdentity
                    networkValuesMap.put(CELL_INFO_KEY_GSM_CID, cellInfoGsm.getCellIdentity().getCid());
                    networkValuesMap.put(CELL_INFO_KEY_GSM_LAC, cellInfoGsm.getCellIdentity().getLac());
                    networkValuesMap.put(CELL_INFO_KEY_GSM_MCC, cellInfoGsm.getCellIdentity().getMcc());
                    networkValuesMap.put(CELL_INFO_KEY_GSM_MNC, cellInfoGsm.getCellIdentity().getMnc());
                    // CellSignalStrength
                    networkValuesMap.put(CELL_INFO_KEY_GSM_ASU_LEVEL, cellInfoGsm.getCellSignalStrength().getAsuLevel());
                    networkValuesMap.put(CELL_INFO_KEY_GSM_DBM, cellInfoGsm.getCellSignalStrength().getDbm());
                    networkValuesMap.put(CELL_INFO_KEY_GSM_LEVEL, cellInfoGsm.getCellSignalStrength().getLevel());
                    // Cell type
                    networkValuesMap.put(CELL_INFO_VALUE_GSM, "CELL_INFO_GSM");
                    cellInfoMapList.add(networkValuesMap);
                }
                else if (cellInfo instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    // CellIdentity
                    networkValuesMap.put(CELL_INFO_KEY_LTE_CI, cellInfoLte.getCellIdentity().getCi());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_MCC, cellInfoLte.getCellIdentity().getMcc());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_MNC, cellInfoLte.getCellIdentity().getMnc());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_PCI, cellInfoLte.getCellIdentity().getPci());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_TAC, cellInfoLte.getCellIdentity().getTac());
                    // CellSignalStrength
                    networkValuesMap.put(CELL_INFO_KEY_LTE_ASU_LEVEL, cellInfoLte.getCellSignalStrength().getAsuLevel());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_DBM, cellInfoLte.getCellSignalStrength().getDbm());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_LEVEL, cellInfoLte.getCellSignalStrength().getLevel());
                    networkValuesMap.put(CELL_INFO_KEY_LTE_TIMING_ADVANCE, cellInfoLte.getCellSignalStrength().getTimingAdvance());
                    // Cell type
                    networkValuesMap.put(CELL_INFO_VALUE_LTE, "CELL_INFO_LTE");
                    cellInfoMapList.add(networkValuesMap);
                }
                else if (cellInfo instanceof CellInfoWcdma) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    // CellIdentity
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_CID, cellInfoWcdma.getCellIdentity().getCid());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_LAC, cellInfoWcdma.getCellIdentity().getLac());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_MCC, cellInfoWcdma.getCellIdentity().getMcc());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_MNC, cellInfoWcdma.getCellIdentity().getMnc());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_PSC, cellInfoWcdma.getCellIdentity().getPsc());
                    // CellSignalStrength
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_ASU_LEVEL, cellInfoWcdma.getCellSignalStrength().getAsuLevel());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_DBM, cellInfoWcdma.getCellSignalStrength().getDbm());
                    networkValuesMap.put(CELL_INFO_KEY_WCDMA_LEVEL, cellInfoWcdma.getCellSignalStrength().getLevel());
                    // Cell type
                    networkValuesMap.put(CELL_INFO_VALUE_WCDMA, "CELL_INFO_WCDMA");
                    cellInfoMapList.add(networkValuesMap);
                }
                /*
                else {
                    networkValuesMap.put(KEY_UNKNOWN_TYPE, cellInfo.toString());
                    networkTypeMap.put(SyncStateContract.Constants.UNKNOWN, networkValuesMap);
                }
                */
            }
            return cellInfoMapList;

        }
        return null;
    }

    public String retrieveNetworkType() {

        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "NETWORK_TYPE_GPRS";

            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "NETWORK_TYPE_EDGE";

            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "NETWORK_TYPE_CDMA";

            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "NETWORK_TYPE_1xRTT";

            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "NETWORK_TYPE_IDEN";
            // return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "NETWORK_TYPE_UMTS";

            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "NETWORK_TYPE_EVDO_0";

            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "NETWORK_TYPE_EVDO_A";

            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "NETWORK_TYPE_HSDPA";

            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "NETWORK_TYPE_HSUPA";

            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "NETWORK_TYPE_HSPA";

            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "NETWORK_TYPE_EVDO_B";

            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "NETWORK_TYPE_EHRPD";

            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "NETWORK_TYPE_HSPAP";
            // return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "NETWORK_TYPE_LTE";

            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "NETWORK_TYPE_TD_SCDMA";
            // return "4G";
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return "NETWORK_TYPE_IWLAN";

            case TelephonyManager.NETWORK_TYPE_GSM:
                return "NETWORK_TYPE_GSM";

            default:
                return "NETWORK_TYPE_UNKNOWN";
        }
    }

    public String retrievePhoneRadioType(){
        int phoneRadioType = telephonyManager.getPhoneType();
        switch(phoneRadioType){
            case TelephonyManager.PHONE_TYPE_GSM:
                return "PHONE_TYPE_GSM";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "PHONE_TYPE_CDMA";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "PHONE_TYPE_SIP";
            default:
                return "PHONE_TYPE_NONE";
        }
    }



}
