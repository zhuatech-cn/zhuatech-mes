/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.controller;
import cn.zhuatech.mes.common.ApiResponse;
import cn.zhuatech.mes.service.AiDefectRootCauseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/mes/ai")
public class AiDefectRootCauseController {
    private final AiDefectRootCauseService service;
    public AiDefectRootCauseController(AiDefectRootCauseService service) { this.service = service; }
    @PostMapping("/defect-root-cause")
    public ApiResponse<AiDefectRootCauseService.Result> analyze(@Valid @RequestBody AiDefectRootCauseService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
