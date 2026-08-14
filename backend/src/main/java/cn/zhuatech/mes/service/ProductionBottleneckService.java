/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionBottleneckService {
    public Result analyze(Request request) {
        BigDecimal cycleVarianceRate = request.actualCycleSeconds().subtract(request.standardCycleSeconds())
            .divide(request.standardCycleSeconds(), 4, RoundingMode.HALF_UP);
        int score = 0;
        if (cycleVarianceRate.compareTo(new BigDecimal("0.20")) >= 0) score += 40;
        else if (cycleVarianceRate.compareTo(new BigDecimal("0.08")) >= 0) score += 20;
        if (request.queueJobs() >= 10) score += 25;
        else if (request.queueJobs() >= 5) score += 12;
        if (request.downtimeMinutes() >= 60) score += 20;
        else if (request.downtimeMinutes() >= 20) score += 10;
        if (request.utilizationRate() >= 90) score += 20;
        score = Math.min(100, score);
        String level = score >= 65 ? "CRITICAL" : score >= 35 ? "WATCH" : "NORMAL";
        List<String> actions = new ArrayList<>();
        if (cycleVarianceRate.compareTo(new BigDecimal("0.20")) >= 0) actions.add("拆解工序周期并校准标准作业参数");
        if (request.queueJobs() >= 10) actions.add("调整工单顺序并将可替代工序分流到并行资源");
        if (request.downtimeMinutes() >= 20) actions.add("关联停机原因并触发设备维护工单");
        if (request.utilizationRate() >= 90) actions.add("评估增班或瓶颈设备扩容方案");
        if (actions.isEmpty()) actions.add("保持当前节拍并持续采集工序周期");
        return new Result(request.workCenterCode(), cycleVarianceRate, score, level, actions);
    }

    public record Request(@NotBlank String workCenterCode,
                          @DecimalMin("0.01") BigDecimal standardCycleSeconds,
                          @DecimalMin("0.01") BigDecimal actualCycleSeconds,
                          @Min(0) int queueJobs, @Min(0) int downtimeMinutes,
                          @Min(0) @Max(100) int utilizationRate) {}
    public record Result(String workCenterCode, BigDecimal cycleVarianceRate,
                         int bottleneckScore, String level, List<String> actions) {}
}
