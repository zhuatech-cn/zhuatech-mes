/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. */
package cn.zhuatech.mes.repository; import cn.zhuatech.mes.model.WorkOrder; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface WorkOrderRepository extends JpaRepository<WorkOrder,Long>{List<WorkOrder> findAllByOrderByDueDateAsc();List<WorkOrder> findByWorkCenterCodeOrderByDueDateAsc(String code);long countByStatus(WorkOrder.Status status);}
