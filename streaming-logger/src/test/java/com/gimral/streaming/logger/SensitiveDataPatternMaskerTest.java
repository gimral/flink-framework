package com.gimral.streaming.logger;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SensitiveDataPatternMaskerTest {
  private static String getResourcePath() {
    URL url =
        SensitiveDataPatternMaskerTest.class.getClassLoader().getResource("SensitivePatterns.json");
    assertNotNull(url, "Resource not found: SensitivePatterns.json");
    return url.toExternalForm();
  }

  private static String getResourcePath(String path) {
    URL url = SensitiveDataPatternMaskerTest.class.getClassLoader().getResource(path);
    assertNotNull(url, "Resource not found: " + path);
    return url.toExternalForm();
  }

  @Test
  void testMaskPassword() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "user=foo password=secret123";
    String masked = masker.mask(input);
    assertFalse(masked.contains("secret123"));
    assertTrue(masked.contains("***"));
  }

  @Test
  void testMaskSecret() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "api_secret: mysecretvalue";
    String masked = masker.mask(input);
    assertFalse(masked.contains("mysecretvalue"));
    assertTrue(masked.contains("[MASKED]"));
  }

  @Test
  void testMaskToken() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "token=abc123";
    String masked = masker.mask(input);
    assertFalse(masked.contains("abc123"));
    assertTrue(masked.contains("<hidden>"));
  }

  @Test
  void testNoMatch() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "no sensitive data here";
    String masked = masker.mask(input);
    assertEquals(input, masked);
  }

  @Test
  void testMultiplePatterns() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "password=foo secret=bar token=baz";
    String masked = masker.mask(input);
    assertTrue(masked.contains("***"));
    assertTrue(masked.contains("[MASKED]"));
    assertTrue(masked.contains("<hidden>"));
  }

  @Test
  void testMaskCreditCard() {
    SensitiveDataPatternMasker masker = new SensitiveDataPatternMasker(getResourcePath());
    String input = "My card is 1234567890123456 and should be masked.";
    String masked = masker.mask(input);
    assertFalse(masked.contains("1234567890123456"));
    assertTrue(masked.contains("1234******5678"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void testNoPatternPath(String path) {
    assertThrows(
        IllegalStateException.class,
        () -> {
          new SensitiveDataPatternMasker(path);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"MissingPattern.json", "EmptyPattern.json", "InvalidPattern.json"})
  void testEmptyPatternDefinition(String path) {
    assertThrows(
        IllegalStateException.class,
        () -> {
          new SensitiveDataPatternMasker(getResourcePath(path));
        });
  }
}
