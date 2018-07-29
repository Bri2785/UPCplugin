package com.fbi.plugins.briteideas.util;

import com.evnt.eve.modules.ImplController;

import com.fbi.fbdata.misc.UomConversionFpo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogicUOMConversions extends ImplController {
    public List<UomConversionFpo> getFromEaUOMs(){
        return this.getUomConversionRepository().findAllByToUOMId(1);
    }
}
