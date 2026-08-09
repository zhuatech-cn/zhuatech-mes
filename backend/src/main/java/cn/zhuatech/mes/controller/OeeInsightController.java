/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.controller;

import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.OeeLossAnalysisService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mes/insights")
public class OeeInsightController {
    private final OeeLossAnalysisService service;

    public OeeInsightController(OeeLossAnalysisService service) {
        this.service = service;
    }

    @PostMapping("/oee-loss-analysis")
    public ApiResponse<OeeLossAnalysisService.Result> analyze(
        @Valid @RequestBody OeeLossAnalysisService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
