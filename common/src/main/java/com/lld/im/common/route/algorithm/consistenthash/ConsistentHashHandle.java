package com.lld.im.common.route.algorithm.consistenthash;

import com.lld.im.common.enums.UserErrorCode;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.route.RouteHandle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class ConsistentHashHandle implements RouteHandle {
    private AbstractConsistentHash hash;

    public void setAbstractConsistentHash(AbstractConsistentHash abstractConsistentHash) {
        this.hash = abstractConsistentHash;
    }
    @Override
    public String routeServer(List<String> values, String key) {
        int size=values.size();
        if(size==0){
            throw new ApplicationException(UserErrorCode.SERVER_NOT_AVAILABLE);
        }
return hash.process(values,key);
    }
}
