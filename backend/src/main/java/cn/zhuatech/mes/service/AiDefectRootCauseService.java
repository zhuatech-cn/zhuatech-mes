/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.service;

import cn.zhuatech.mes.ai.OpenAiCompatibleGateway;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiDefectRootCauseService {
    private final OpenAiCompatibleGateway gateway;
    public AiDefectRootCauseService(OpenAiCompatibleGateway gateway) { this.gateway = gateway; }

    public Result analyze(Request request) {
        int risk = 10;
        List<String> causes = new ArrayList<>();
        List<String> containment = new ArrayList<>();
        if (request.defectRate().compareTo(request.baselineDefectRate().multiply(BigDecimal.valueOf(2))) > 0) {
            risk += 30; causes.add("缺陷率相对基线发生显著漂移"); containment.add("隔离当前时间窗产出并加严抽检");
        }
        if (request.temperatureDeviation().abs().compareTo(BigDecimal.valueOf(5)) > 0) {
            risk += 20; causes.add("工艺温度偏离控制范围"); containment.add("校准温控并复核工艺参数配方");
        }
        if (request.vibrationDeviation().abs().compareTo(BigDecimal.valueOf(20)) > 0) {
            risk += 20; causes.add("设备振动特征异常"); containment.add("安排设备点检并检查主轴、轴承或夹具");
        }
        if (request.toolingAgeHours() > 500) { risk += 15; causes.add("工装使用时长接近或超过维护周期"); }
        if (Boolean.TRUE.equals(request.operatorChanged())) { risk += 10; causes.add("缺陷窗口与人员切换时间重合"); }
        if (Boolean.TRUE.equals(request.materialBatchChanged())) { risk += 20; causes.add("缺陷窗口与物料批次切换重合"); containment.add("冻结可疑批次并执行来料复验"); }
        risk = Math.min(100, risk);
        if (causes.isEmpty()) causes.add("结构化信号未定位明确根因，需要扩大数据窗口");
        if (containment.isEmpty()) containment.add("保持监控并补充设备、物料和人员追溯数据");

        String context = "缺陷=%s，当前缺陷率=%s，基线=%s，风险=%d，候选原因=%s"
            .formatted(request.defectType(), request.defectRate(), request.baselineDefectRate(), risk, causes);
        var enhanced = gateway.complete("你是制造质量根因分析助手，请按人机料法环测给出假设、证据和验证步骤。", context);
        var metadata = gateway.metadata();
        return new Result(risk, risk >= 70 ? "STOP_AND_INVESTIGATE" : risk >= 40 ? "CONTAIN" : "MONITOR",
            List.copyOf(causes), List.copyOf(containment),
            enhanced.orElse("优先验证：" + causes.getFirst()), enhanced.isPresent() ? "EXTERNAL_MODEL" : "LOCAL_RULES",
            metadata.provider(), metadata.model());
    }

    public record Request(@NotBlank String defectType, @DecimalMin("0") BigDecimal defectRate,
                          @DecimalMin("0.01") BigDecimal baselineDefectRate, @NotNull BigDecimal temperatureDeviation,
                          @NotNull BigDecimal vibrationDeviation, @Min(0) int toolingAgeHours,
                          @NotNull Boolean operatorChanged, @NotNull Boolean materialBatchChanged) {}
    public record Result(int riskScore, String status, List<String> likelyCauses, List<String> containmentActions,
                         String aiExplanation, String aiMode, String provider, String model) {}
}
