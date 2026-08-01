/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.controller;

import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.TaktAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
public class TaktController {
    private final TaktAnalysisService service;
    public TaktController(TaktAnalysisService service) { this.service = service; }

    @PostMapping("/takt-analysis")
    public ApiResponse<TaktAnalysisService.Result> analyze(@Valid @RequestBody TaktAnalysisService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
