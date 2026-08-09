/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OeeLossAnalysisService {
    public Result analyze(Request request) {
        double oee = round(request.availability() * request.performance() * request.quality() * 100);
        String largestLoss = request.availability() <= request.performance() && request.availability() <= request.quality()
            ? "AVAILABILITY" : request.performance() <= request.quality() ? "PERFORMANCE" : "QUALITY";
        String status = oee < 65 ? "CRITICAL" : oee < 85 ? "IMPROVE" : "STABLE";
        int recoverableMinutes = (int) Math.ceil(Math.max(0, 85 - oee) * request.plannedMinutes() / 100D);
        List<String> actions = new ArrayList<>();
        if ("AVAILABILITY".equals(largestLoss)) actions.add("优先治理停机、换型和设备等待时间");
        if ("PERFORMANCE".equals(largestLoss)) actions.add("核对标准节拍并治理微停与降速运行");
        if ("QUALITY".equals(largestLoss)) actions.add("定位首件、过程参数和返工损失");
        if ("STABLE".equals(status)) actions.add("保持当前参数并固化最佳班组实践");
        return new Result(request.workCenterCode(), oee, largestLoss, recoverableMinutes, status, actions);
    }

    private double round(double value) {
        return Math.round(value * 100D) / 100D;
    }

    public record Request(@NotBlank String workCenterCode,
                          @DecimalMin("0") @DecimalMax("1") double availability,
                          @DecimalMin("0") @DecimalMax("1") double performance,
                          @DecimalMin("0") @DecimalMax("1") double quality,
                          @Min(1) int plannedMinutes) {}

    public record Result(String workCenterCode, double oeePercent, String largestLoss,
                         int recoverableMinutesToWorldClass, String status, List<String> actions) {}
}
