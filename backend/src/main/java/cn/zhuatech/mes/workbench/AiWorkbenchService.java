/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.workbench;

import static cn.zhuatech.mes.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);

    keys(r, "plannedSeconds", "idealCycleSeconds", "totalUnits", "goodUnits", "stops");
    int planned = integer(r, "plannedSeconds", 1, 604800),
        units = integer(r, "totalUnits", 0, 1000000000),
        good = integer(r, "goodUnits", 0, units);
    double cycle = decimal(r, "idealCycleSeconds", 0.000001, 604800).doubleValue();
    var stops = new ArrayList<>(rows(r, "stops", 0, 300));
    Set<String> ids = new HashSet<>();
    for (var s : stops) {
      keys(s, "id", "start", "end");
      unique(ids, id(s, "id"));
      int a = integer(s, "start", 0, planned), b = integer(s, "end", 0, planned);
      if (a >= b) throw new IllegalArgumentException("停机区间须正向");
    }
    stops.sort(Comparator.comparingInt(s -> ((Number) s.get("start")).intValue()));
    long down = 0;
    int end = 0;
    for (var s : stops) {
      int a = ((Number) s.get("start")).intValue(), b = ((Number) s.get("end")).intValue();
      down += Math.max(0, b - Math.max(a, end));
      end = Math.max(end, b);
    }
    long running = planned - down;
    if (running == 0 && units > 0) throw new IllegalArgumentException("运行时间0却有产出，证据不一致");
    double a = running / (double) planned,
        p = running == 0 ? 0 : cycle * units / running,
        q = units == 0 ? 0 : good / (double) units;
    return Map.of(
        "downtimeSeconds",
        down,
        "runningSeconds",
        running,
        "availabilityPercent",
        round(a * 100),
        "performanceDefined",
        running > 0,
        "performancePercent",
        round(p * 100),
        "qualityDefined",
        units > 0,
        "qualityPercent",
        round(q * 100),
        "oeePercent",
        round(a * p * q * 100),
        "oeeDefined",
        running > 0 && units > 0,
        "performanceAbove100",
        p > 1);
  }
}
