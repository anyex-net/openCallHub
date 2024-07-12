package com.och.esl.handler.route;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.och.common.annotation.EslRouteName;
import com.och.common.domain.CallInfo;
import com.och.common.domain.CallInfoDetail;
import com.och.common.domain.ChannelInfo;
import com.och.common.enums.ProcessEnum;
import com.och.common.enums.RouteTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @date 2023-11-10 17:20
 **/
@EslRouteName(RouteTypeEnum.SIP)
@Component
@Slf4j
public class FsSipRouteHandler extends FsAbstractRouteHandler {

    @Override
    public void handler(String address, CallInfo callInfo, String uniqueId, String routeValue) {
        log.info("转SIP callId:{} transfer to {}", callInfo.getCallId(), routeValue);
        String otherUniqueId = RandomUtil.randomNumbers(32);
        String[] destinationArr = routeValue.split(Constants.AT);
        callInfo.setCallee(destinationArr[0]);
        String gatewayAddress = destinationArr[1];
        //构建被叫通道
        ChannelInfo otherChannelInfo = ChannelInfo.builder().callId(callInfo.getCallId()).uniqueId(otherUniqueId).cdrType(2).type(3)
                .callTime(DateUtil.current()).otherUniqueId(uniqueId)
                .called(callInfo.getCallee()).caller(callInfo.getCaller()).display(callInfo.getCallerDisplay()).build();
        callInfo.addUniqueIdList(otherUniqueId);
        callInfo.setChannelInfoMap(otherUniqueId,otherChannelInfo);
        callInfo.setProcess(ProcessEnum.CALL_BRIDGE);

        CallInfoDetail detail = new CallInfoDetail();
        detail.setCallId(callInfo.getCallId());
        detail.setStartTime(DateUtil.current());
        detail.setOrderNum(callInfo.getDetailList() == null ? 0 : callInfo.getDetailList().size() + 1);
        detail.setTransferType(6);
        callInfo.addDetailList(detail);

        fsCallCacheService.saveCallInfo(callInfo);
        fsCallCacheService.saveCallRel(otherUniqueId,callInfo.getCallId());

        fsClient.makeCall(address,callInfo.getCallId(), callInfo.getCaller(),callInfo.getCallee(),otherUniqueId,callInfo.getCalleeTimeOut(), gatewayAddress);

    }
}
