/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.dto;
import jakarta.validation.constraints.*; import java.time.*; import java.util.List;
public final class MesDto { private MesDto(){}
    public record Metric(String label,String value,String hint,String tone){}
    public record WorkOrderView(Long id,String orderNo,String productCode,String productName,String workCenter,String workshop,int plannedQty,int completedQty,int defectQty,LocalDate dueDate,String status,String batchNo,int progress){}
    public record EquipmentView(String code,String name,String workCenter,String status,int oee,LocalDateTime lastHeartbeat){}
    public record InspectionView(String inspectionNo,String orderNo,String productName,String inspectionType,int sampleQty,int defectQty,String result,String inspector){}
    public record Dashboard(List<Metric> metrics,List<WorkOrderView> workOrders,List<EquipmentView> equipment,List<InspectionView> inspections){}
    public record ReportRequest(@NotBlank String operationName,@Positive int goodQty,@PositiveOrZero int defectQty,@Size(max=200) String remark){}
    public record ReportResult(String orderNo,int completedQty,int defectQty,int progress,String status){}
}
