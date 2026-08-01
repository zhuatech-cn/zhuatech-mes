# REST API

Copyright 2026 上海如静知华信息科技有限公司。

统一前缀为 `/api`，返回结构为 `success`、`message`、`data`、`timestamp`。除登录外均使用 `Authorization: Bearer <token>`。

| 方法 | 地址 | 角色 | 用途 |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | 公开 | 账号密码登录 |
| GET | `/api/auth/me` | 已登录 | 当前用户与工作中心 |
| GET | `/api/shopfloor/dashboard` | OPERATOR / ADMIN | 当前班组工单、设备和质量信息 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | OPERATOR / ADMIN | 提交合格数、不良数和工序报工 |
| GET | `/api/admin/dashboard` | PLANNER / QUALITY / ADMIN | 全工厂生产驾驶舱 |
| GET | `/api/admin/work-orders` | PLANNER / QUALITY / ADMIN | 按交期查询生产工单 |

报工请求示例：

```json
{
  "operationName": "扭矩锁付",
  "goodQty": 48,
  "defectQty": 1,
  "remark": "本班工艺参数正常"
}
```

服务端拒绝已完工工单继续报工，并校验合格数量不超过工单剩余数量。

## 生产节拍分析

`POST /api/shopfloor/takt-analysis`：计算目标节拍、扣除停机后的有效节拍、实际偏差和车间改善动作。
