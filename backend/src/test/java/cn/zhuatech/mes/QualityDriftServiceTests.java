/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.mes;

import cn.zhuatech.mes.service.QualityDriftService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class QualityDriftServiceTests {
    private final QualityDriftService service = new QualityDriftService();

    @Test void stopsOnCompoundQualityDrift() {
        var result = service.detect(new QualityDriftService.Request("WC-01", new BigDecimal("98.5"), new BigDecimal("88"), 5,
            new BigDecimal("96"), new BigDecimal("8"), 15));
        assertThat(result.decision()).isEqualTo("STOP_AND_CHECK");
        assertThat(result.actions()).hasSize(4);
    }

    @Test void continuesStableProcess() {
        var result = service.detect(new QualityDriftService.Request("WC-02", new BigDecimal("98"), new BigDecimal("97.5"), 0,
            new BigDecimal("50"), new BigDecimal("1"), 50));
        assertThat(result.decision()).isEqualTo("CONTINUE");
    }
}
