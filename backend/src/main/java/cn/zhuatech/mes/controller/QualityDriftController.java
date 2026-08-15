/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.controller;

import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.QualityDriftService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mes/insights")
public class QualityDriftController {
    private final QualityDriftService service;
    public QualityDriftController(QualityDriftService service) { this.service = service; }

    @PostMapping("/quality-drift")
    public ApiResponse<QualityDriftService.Result> detect(@Valid @RequestBody QualityDriftService.Request request) {
        return ApiResponse.ok(service.detect(request));
    }
}
