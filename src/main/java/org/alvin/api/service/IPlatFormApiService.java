package org.alvin.api.service;

import java.util.Map;
import org.alvin.api.ui.renderer.PlatTypeItem;

public interface IPlatFormApiService {

    Map<String, String> getRequestParams(PlatTypeItem item) throws Exception;
    
    String getPublicParmas();

}
