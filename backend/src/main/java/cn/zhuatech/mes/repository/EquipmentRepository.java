/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. */
package cn.zhuatech.mes.repository; import cn.zhuatech.mes.model.Equipment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface EquipmentRepository extends JpaRepository<Equipment,Long>{List<Equipment> findAllByOrderByCodeAsc();long countByStatus(Equipment.Status status);}
