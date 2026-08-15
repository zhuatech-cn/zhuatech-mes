/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class QualityDriftService {
    public Result detect(Request request) {
        BigDecimal passRateDrop = request.baselinePassRate().subtract(request.currentPassRate()).max(BigDecimal.ZERO);
        int score = Math.min(40, passRateDrop.multiply(BigDecimal.valueOf(4)).intValue());
        score += Math.min(20, request.consecutiveDeviations() * 5);
        if (request.toolLifeUsedPercent().compareTo(new BigDecimal("90")) >= 0) score += 20;
        else if (request.toolLifeUsedPercent().compareTo(new BigDecimal("75")) >= 0) score += 10;
        score += Math.min(20, request.processParameterDeviationPercent().multiply(BigDecimal.valueOf(2)).intValue());
        if (request.sampleSize() < 20) score = Math.min(100, score + 10);
        score = Math.min(100, score);

        String decision = score >= 65 ? "STOP_AND_CHECK" : score >= 35 ? "REVIEW" : "CONTINUE";
        List<String> actions = new ArrayList<>();
        if (passRateDrop.compareTo(new BigDecimal("3")) >= 0) actions.add("隔离当前批次并复核首末件检验结果");
        if (request.toolLifeUsedPercent().compareTo(new BigDecimal("90")) >= 0) actions.add("检查或更换接近寿命上限的工装刀具");
        if (request.processParameterDeviationPercent().compareTo(new BigDecimal("5")) >= 0) actions.add("校准关键工艺参数并确认设备状态");
        if (request.sampleSize() < 20) actions.add("扩大抽样数量后再确认趋势结论");
        if (actions.isEmpty()) actions.add("维持生产并持续监测质量趋势");
        return new Result(request.workCenterCode(), passRateDrop, score, decision, actions);
    }

    public record Request(@NotBlank String workCenterCode,
                          @DecimalMin("0") @DecimalMax("100") BigDecimal baselinePassRate,
                          @DecimalMin("0") @DecimalMax("100") BigDecimal currentPassRate,
                          @Min(0) int consecutiveDeviations,
                          @DecimalMin("0") @DecimalMax("100") BigDecimal toolLifeUsedPercent,
                          @DecimalMin("0") BigDecimal processParameterDeviationPercent,
                          @Min(1) int sampleSize) {}
    public record Result(String workCenterCode, BigDecimal passRateDrop, int driftScore,
                         String decision, List<String> actions) {}
}
