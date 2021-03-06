// 
// Decompiled by Procyon v0.5.30
// 

package com.fbi.plugins.briteideas.repository;

//import com.fbi.commerce.shipping.type.ShipServiceEnum;
import com.fbi.fbo.impl.dataexport.QueryRow;

public final class Compatibility
{
    public static final int FB_VERSION_2017_01 = 100;
    
    public static String handleFirebirdCompatibility(final UPCRepository UPCRepository, final String method) {
        if (UPCRepository.getDatabaseVersion() < 100) {
            return "firebird/" + method;
        }
        return method;
    }
    
    public static boolean getBoolean(final UPCRepository UPCRepository, final QueryRow queryRow, final String property) {
        if (UPCRepository.getDatabaseVersion() >= 100) {
            return queryRow.getBoolean(property);
        }
        return queryRow.getInt(property) == 1;
    }
    
    public static String getServiceCode(final UPCRepository UPCRepository, final String service, final Integer carrierId, final String carrierName) {
        if (UPCRepository.getDatabaseVersion() < 100) {
            String code = carrierName;
            if (code.length() > 10) {
                code = code.substring(0, 10);
            }
            return code + carrierId;
        }
        return service;
    }
    

}
