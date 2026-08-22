/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaktAnalysisService {
    public Result analyze(Request request) {
        double targetTakt = request.availableMinutes() * 60.0 / request.demandQty();
        double effectiveMinutes = Math.max(1, request.availableMinutes() - request.downtimeMinutes());
        double effectiveTakt = effectiveMinutes * 60.0 / request.demandQty();
        double gapPercent = (request.actualCycleSeconds() - effectiveTakt) / effectiveTakt * 100;
        String status = gapPercent > 15 ? "BEHIND" : gapPercent > 0 ? "AT_RISK" : "ON_TRACK";
        List<String> actions = new ArrayList<>();
        if (request.downtimeMinutes() > 0) actions.add("复盘停机原因并补充有效生产时间");
        if (request.actualCycleSeconds() > effectiveTakt) actions.add("对瓶颈工序执行节拍改善");
        if (actions.isEmpty()) actions.add("保持当前节拍并持续采集周期数据");
        return new Result(request.orderNo(), round(targetTakt), round(effectiveTakt),
            round(gapPercent), status, actions);
    }

    private double round(double value) { return Math.round(value * 100.0) / 100.0; }

    public record Request(@NotBlank String orderNo, @Positive int demandQty,
                          @Positive int availableMinutes,
                          @DecimalMin("0") double actualCycleSeconds,
                          @Min(0) int downtimeMinutes) {}
    public record Result(String orderNo, double targetTaktSeconds, double effectiveTaktSeconds,
                         double gapPercent, String status, List<String> actions) {}
}
