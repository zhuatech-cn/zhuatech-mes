/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.repository; import cn.zhuatech.mes.model.WorkCenter; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface WorkCenterRepository extends JpaRepository<WorkCenter,Long>{Optional<WorkCenter> findByCode(String code);}
