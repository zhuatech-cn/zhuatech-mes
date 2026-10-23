/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.workbench;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class AiWorkbenchServiceTest {
  private final AiWorkbenchService service = new AiWorkbenchService();

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  private Map<String, Object> sample() {
    return Map.<String, Object>ofEntries(
        Map.entry("plannedSeconds", 3600),
        Map.entry("idealCycleSeconds", 10),
        Map.entry("totalUnits", 300),
        Map.entry("goodUnits", 270),
        Map.entry(
            "stops",
            List.of(
                Map.<String, Object>ofEntries(
                    Map.entry("id", "D1"), Map.entry("start", 0), Map.entry("end", 300)),
                Map.<String, Object>ofEntries(
                    Map.entry("id", "D2"), Map.entry("start", 200), Map.entry("end", 600)))));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void computesBusinessResult() {
    var result = service.execute(sample());
    assertEquals(600L, result.get("downtimeSeconds"));
    assertEquals(new BigDecimal("75"), result.get("oeePercent"));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversFeatureBoundary() {
    var invalid = new HashMap<>(sample());
    invalid.put("goodUnits", 301);
    assertThrows(IllegalArgumentException.class, () -> service.execute(invalid));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversAdditionalBoundaryOne() {
    var r =
        Map.<String, Object>of(
            "plannedSeconds",
            10,
            "idealCycleSeconds",
            1,
            "totalUnits",
            0,
            "goodUnits",
            0,
            "stops",
            List.of(Map.of("id", "D", "start", 0, "end", 10)));
    assertEquals(false, service.execute(r).get("performanceDefined"));
    assertEquals(false, service.execute(r).get("oeeDefined"));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void coversAdditionalBoundaryTwo() {
    var r =
        Map.<String, Object>of(
            "plannedSeconds",
            10,
            "idealCycleSeconds",
            1,
            "totalUnits",
            1,
            "goodUnits",
            1,
            "stops",
            List.of(Map.of("id", "D", "start", 0, "end", 10)));
    assertThrows(IllegalArgumentException.class, () -> service.execute(r));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsExcessiveNumericScale() {
    assertThrows(
        IllegalArgumentException.class,
        () -> WorkbenchInput.decimal(Map.of("v", new BigDecimal("1e-1000000")), "v", 0, 1));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            WorkbenchInput.decimal(
                Map.of("v", new BigDecimal("0.1234567890123456789012345678901")), "v", 0, 1));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void deterministicAndDoesNotMutateInput() {
    var input = sample();
    String before = input.toString();
    assertEquals(service.execute(input), service.execute(input));
    assertEquals(before, input.toString());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsNull() {
    assertThrows(IllegalArgumentException.class, () -> service.execute(null));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsMissingFields() {
    assertThrows(IllegalArgumentException.class, () -> service.execute(Map.of()));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsUnknownFields() {
    var input = new HashMap<>(sample());
    input.put("__unknown", true);
    assertThrows(IllegalArgumentException.class, () -> service.execute(input));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsOversizedText() {
    assertThrows(
        IllegalArgumentException.class,
        () -> service.execute(Map.of("payload", "x".repeat(64001))));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsDeepInput() {
    Map<String, Object> v = Map.of("x", 1);
    for (int i = 0; i < 14; i++) v = Map.of("x", v);
    final var input = v;
    assertThrows(IllegalArgumentException.class, () -> service.execute(input));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpRequiresAuthentication() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/evidence-oee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"plannedSeconds\":3600,\"idealCycleSeconds\":10,\"totalUnits\":300,\"goodUnits\":270,\"stops\":[{\"id\":\"D1\",\"start\":0,\"end\":300},{\"id\":\"D2\",\"start\":200,\"end\":600}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpAllowsAuthenticatedComputation() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/evidence-oee")
                .principal(new TestingAuthenticationToken("test-user", "unused", "ROLE_OPERATOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"plannedSeconds\":3600,\"idealCycleSeconds\":10,\"totalUnits\":300,\"goodUnits\":270,\"stops\":[{\"id\":\"D1\",\"start\":0,\"end\":300},{\"id\":\"D2\",\"start\":200,\"end\":600}]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void httpRejectsInvalidInput() throws Exception {
    var mvc = MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();
    mvc.perform(
            post("/api/workbench/evidence-oee")
                .principal(new TestingAuthenticationToken("test-user", "unused", "ROLE_OPERATOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void rejectsAnonymousAuthenticationObject() {
    var actor =
        new AnonymousAuthenticationToken(
            "test-key",
            "anonymous",
            List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                    "ROLE_ANONYMOUS")));
    assertEquals(
        401, new AiWorkbenchController(service).run(sample(), actor).getStatusCode().value());
  }
}
