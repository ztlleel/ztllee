package com.lld.im.common.route.algorithm.loop;

import com.lld.im.common.enums.UserErrorCode;
import com.lld.im.common.exception.ApplicationException;
import com.lld.im.common.route.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoopHandle implements RouteHandle {
    private AtomicLong index=new AtomicLong();
    @Override
    public String routeServer(List<String> values, String key) {
        int size=values.size();
        if(size==0){
            throw new ApplicationException(UserErrorCode.SERVER_NOT_AVAILABLE);
        }
        //参考nacos的轮询的那两行的核心代码
        Long l = index.incrementAndGet() % size;
        if(l<0){
            l=0L;
        }
        return values.get(l.intValue());
    }
}
