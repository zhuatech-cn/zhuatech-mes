/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
export const workOrders=[
 {id:1,no:'MO-20260729-018',product:'智能执行器 AX120',code:'FG-AX120',center:'总装一线',workshop:'装配车间',plan:1200,done:864,defect:12,due:'07-30',batch:'B260729A',status:'生产中',progress:72,priority:'紧急'},
 {id:2,no:'MO-20260729-021',product:'精密减速器 DR80',code:'FG-DR80',center:'精加工二线',workshop:'机加车间',plan:680,done:272,defect:7,due:'07-31',batch:'B260729C',status:'生产中',progress:40,priority:'正常'},
 {id:3,no:'MO-20260730-006',product:'工业采集模块 IO16',code:'FG-IO16',center:'总装一线',workshop:'装配车间',plan:1500,done:0,defect:0,due:'08-01',batch:'B260730B',status:'已下达',progress:0,priority:'正常'},
 {id:4,no:'MO-20260728-015',product:'伺服驱动器 SV40',code:'FG-SV40',center:'功能测试线',workshop:'检测车间',plan:960,done:960,defect:9,due:'07-29',batch:'B260728D',status:'已完工',progress:100,priority:'正常'},
 {id:5,no:'MO-20260729-024',product:'运动控制器 MC8',code:'FG-MC08',center:'SMT 二线',workshop:'电子车间',plan:820,done:516,defect:5,due:'07-31',batch:'B260729E',status:'暂停',progress:63,priority:'关注'}]
export const equipment=[
 {code:'EQ-AS-011',name:'一号自动装配单元',center:'总装一线',status:'运行',oee:86,beat:'18.6s',note:'本班运行 5h 42m'},
 {code:'EQ-AS-014',name:'扭矩锁付工作站',center:'总装一线',status:'运行',oee:91,beat:'12.4s',note:'本班运行 6h 08m'},
 {code:'EQ-MC-027',name:'五轴加工中心',center:'精加工二线',status:'报警',oee:64,beat:'—',note:'主轴温度超过预警值'},
 {code:'EQ-TS-006',name:'综合性能测试台',center:'功能测试线',status:'待机',oee:78,beat:'31.2s',note:'等待下一批次'}]
export const inspections=[
 {no:'FAI-260730-003',order:'MO-20260730-006',product:'工业采集模块 IO16',type:'首件检验',sample:5,defect:0,result:'待检',inspector:'陆承'},
 {no:'FAI-260729-011',order:'MO-20260729-021',product:'精密减速器 DR80',type:'首件检验',sample:5,defect:1,result:'不合格',inspector:'周妍'},
 {no:'IPQC-260729-032',order:'MO-20260729-018',product:'智能执行器 AX120',type:'工序巡检',sample:20,defect:0,result:'合格',inspector:'周妍'},
 {no:'FQC-260729-018',order:'MO-20260728-015',product:'伺服驱动器 SV40',type:'完工检验',sample:32,defect:0,result:'合格',inspector:'陆承'}]
export const adminMetrics=[['今日计划','5,160','5 张工单 · 4 个工作中心','blue'],['生产达成率','67.2%','较昨日同期 +3.8%','green'],['一次合格率','98.6%','目标值 ≥ 98.0%','orange'],['设备异常','1','已持续 18 分钟','red']]
export const shopMetrics=[['本班计划','1,200','智能执行器 AX120','blue'],['已完成','864','当前达成 72%','green'],['本班不良','12','一次合格率 98.6%','orange'],['剩余工时','2h 18m','按当前节拍预测','slate']]
export const hourly=[120,186,276,364,452,548,650,756,864]
