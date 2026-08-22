/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="mes_inspection") public class Inspection extends BaseEntity {
    public enum Result { PENDING, PASSED, FAILED }
    @Column(nullable=false,unique=true,length=32) private String inspectionNo; @ManyToOne(optional=false,fetch=FetchType.LAZY) private WorkOrder workOrder;
    @Column(nullable=false,length=30) private String inspectionType; @Column(nullable=false) private int sampleQty; @Column(nullable=false) private int defectQty; @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Result result;
    @Column(length=50) private String inspector; @Column(nullable=false) private LocalDateTime createdAt;
    protected Inspection(){} public Inspection(String inspectionNo,WorkOrder workOrder,String inspectionType,int sampleQty,int defectQty,Result result,String inspector){this.inspectionNo=inspectionNo;this.workOrder=workOrder;this.inspectionType=inspectionType;this.sampleQty=sampleQty;this.defectQty=defectQty;this.result=result;this.inspector=inspector;this.createdAt=LocalDateTime.now();}
    public String getInspectionNo(){return inspectionNo;} public WorkOrder getWorkOrder(){return workOrder;} public String getInspectionType(){return inspectionType;} public int getSampleQty(){return sampleQty;} public int getDefectQty(){return defectQty;} public Result getResult(){return result;} public String getInspector(){return inspector;}
}
