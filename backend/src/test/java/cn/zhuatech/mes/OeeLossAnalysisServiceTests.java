/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes;

import cn.zhuatech.mes.service.OeeLossAnalysisService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OeeLossAnalysisServiceTests {
    private final OeeLossAnalysisService service = new OeeLossAnalysisService();

    @Test
    void identifiesPerformanceAsLargestLoss() {
        var result = service.analyze(new OeeLossAnalysisService.Request("WC-01", .92, .70, .98, 480));

        assertEquals(63.11, result.oeePercent());
        assertEquals("PERFORMANCE", result.largestLoss());
        assertEquals("CRITICAL", result.status());
        assertTrue(result.recoverableMinutesToWorldClass() > 100);
    }

    @Test
    void recognizesStableWorldClassOperation() {
        var result = service.analyze(new OeeLossAnalysisService.Request("WC-02", .97, .94, .97, 480));

        assertEquals("STABLE", result.status());
        assertEquals(0, result.recoverableMinutesToWorldClass());
    }
}
