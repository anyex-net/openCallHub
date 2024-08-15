package com.och.api.controller;

import com.github.pagehelper.PageInfo;
import com.och.common.annotation.Log;
import com.och.common.base.BaseController;
import com.och.common.base.ResResult;
import com.och.common.enums.BusinessTypeEnum;
import com.och.common.enums.SipAgentStatusEnum;
import com.och.common.exception.CommonException;
import com.och.system.domain.query.agent.SipAgentAddQuery;
import com.och.system.domain.query.agent.SipAgentQuery;
import com.och.system.domain.vo.agent.SipAgentListVo;
import com.och.system.domain.vo.agent.SipAgentVo;
import com.och.system.service.ISipAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 坐席管理
 *
 * @author danmo
 * @date 2023年09月23日 10:41
 */
@Tag(name = "坐席管理")
@RestController
@RequestMapping("/system/v1/agent")
public class SipAgentController extends BaseController {

    @Autowired
    private ISipAgentService iSipAgentService;

    @Log(title = "新增坐席", businessType = BusinessTypeEnum.INSERT)
    @PreAuthorize("@authz.hasPerm('system:agent:add')")
    @Operation(summary = "新增坐席", method = "POST")
    @PostMapping("/add")
    public ResResult add(@RequestBody @Validated SipAgentAddQuery query) {
        iSipAgentService.add(query);
        return ResResult.success();
    }

    @Log(title = "修改坐席", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@authz.hasPerm('system:agent:edit')")
    @Operation(summary = "修改坐席", method = "POST")
    @PostMapping("/edit/{id}")
    public ResResult edit(@PathVariable("id") Long id, @RequestBody @Validated SipAgentAddQuery query) {
        query.setId(id);
        iSipAgentService.update(query);
        return ResResult.success();
    }

    @Log(title = "删除坐席", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("@authz.hasPerm('system:agent:delete')")
    @Operation(summary = "删除坐席", method = "POST")
    @PostMapping("/delete")
    public ResResult delete(@RequestBody SipAgentQuery query) {
        iSipAgentService.delete(query);
        return ResResult.success();
    }

    @Log(title = "坐席详情", businessType = BusinessTypeEnum.SELECT)
    @PreAuthorize("@authz.hasPerm('system:agent:get')")
    @Operation(summary = "坐席详情", method = "POST")
    @PostMapping("/get/{id}")
    public ResResult<SipAgentVo> getDetail(@PathVariable("id") Long id) {
        SipAgentVo detail = iSipAgentService.getDetail(id);
        return ResResult.success(detail);
    }

    @Log(title = "坐席列表", businessType = BusinessTypeEnum.SELECT)
    @PreAuthorize("@authz.hasPerm('system:agent:page:list')")
    @Operation(summary = "坐席列表", method = "POST")
    @PostMapping("/page/list")
    public ResResult<PageInfo<SipAgentListVo>> getPageList(@RequestBody SipAgentQuery query) {
        PageInfo<SipAgentListVo> list = iSipAgentService.getPageList(query);
        return success(list);
    }

    @Operation(summary = "根据条件查询坐席", method = "POST")
    @PostMapping("/getInfoByQuery")
    public ResResult<List<SipAgentVo>> getInfoByQuery(@RequestBody SipAgentQuery query) {
        List<SipAgentVo> lfsSipAgentVo = iSipAgentService.getInfoByQuery(query);
        return success(lfsSipAgentVo);
    }

    @Operation(summary = "根据sip号码查询坐席", method = "GET")
    @GetMapping("/getInfoByAgent/{agentNum}")
    public ResResult<SipAgentVo> getInfoByAgent(@PathVariable("agentNum") String agentNum) {
        SipAgentVo lfsSipAgentVo = iSipAgentService.getInfoByAgent(agentNum);
        return ResResult.success(lfsSipAgentVo);
    }

    @Log(title = "坐席签入", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@authz.hasPerm('system:agent:check:in')")
    @Operation(summary = "坐席签入", method = "POST")
    @PostMapping("/check/in/{id}")
    public ResResult checkIn(@PathVariable("id") Long id, @RequestBody SipAgentQuery query) {
        query.setId(id);
        iSipAgentService.updateStatus(id, SipAgentStatusEnum.READY.getCode());
        return ResResult.success();
    }

    @Log(title = "坐席签出", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@authz.hasPerm('system:agent:check:out')")
    @Operation(summary = "坐席签出", method = "POST")
    @PostMapping("/check/out/{id}")
    public ResResult checkOut(@PathVariable("id") Long id, @RequestBody SipAgentQuery query) {
        query.setId(id);
        iSipAgentService.updateStatus(id, SipAgentStatusEnum.OFF_ON.getCode());
        return ResResult.success();
    }

    @Log(title = "坐席忙碌", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@authz.hasPerm('system:agent:check:busy')")
    @Operation(summary = "坐席忙碌", method = "POST")
    @PostMapping("/check/busy/{id}")
    public ResResult checkBusy(@PathVariable("id") Long id, @RequestBody SipAgentQuery query) {
        query.setId(id);
        iSipAgentService.updateStatus(id, SipAgentStatusEnum.NOT_READY.getCode());
        return ResResult.success();
    }

    @Log(title = "坐席通话中", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@authz.hasPerm('system:agent:check:calling')")
    @Operation(summary = "坐席通话中", method = "POST")
    @PostMapping("/check/calling/{id}")
    public ResResult checkCalling(@PathVariable("id") Long id, @RequestBody SipAgentQuery query) {
        query.setId(id);
        iSipAgentService.updateStatus(id, SipAgentStatusEnum.TALKING.getCode());
        return ResResult.success();
    }


    @Operation(summary = "坐席状态变更", method = "POST")
    @PostMapping("/update/status/{id}")
    public ResResult updateStatus(@PathVariable("id") Long id, @RequestBody SipAgentQuery query) {
        query.setId(id);
        if (query.getStatus() == null) {
            throw new CommonException("状态不能为空");
        }
        iSipAgentService.updateStatus(query.getId(), query.getStatus());
        return ResResult.success();
    }
}
