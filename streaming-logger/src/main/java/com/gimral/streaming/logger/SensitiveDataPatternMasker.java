package com.gimral.streaming.logger;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Collections;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class SensitiveDataMasker {
    public static final String DEFAULT_MASK = "***";
    private static final String PATTERNS_PATH_ENV = "SENSITIVE_PATTERNS_PATH";
    public static final List<MaskingPattern> MASKING_PATTERNS = loadMaskingPatterns();


    public static List<MaskingPattern> loadMaskingPatterns() {
        String path = System.getenv(PATTERNS_PATH_ENV);
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalStateException(PATTERNS_PATH_ENV + " environment variable is not set or empty");
        }
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(in);
            @SuppressWarnings("unchecked")
            List<Map<String, String>> patternList = (List<Map<String, String>>) config.get("patterns");

            if (patternList == null || patternList.isEmpty()) {
                throw new IllegalStateException("No patterns found in YAML configuration at " + path);
            }

            List<MaskingPattern> maskingPatterns = new ArrayList<>();
            for (Map<String, String> map : patternList) {
                String pattern = map.get("pattern");
                String replacement = map.getOrDefault("replacement", DEFAULT_MASK);
                MaskingPattern def = new MaskingPattern(pattern,replacement);

                maskingPatterns.add(def);
            }
            return Collections.unmodifiableList(maskingPatterns);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load sensitive patterns from YAML at " + path, e);
        }
    }

    public String mask(String input) {
        String masked = input;
        for (MaskingPattern maskingPattern : MASKING_PATTERNS) {
            try {
                Pattern pattern = maskingPattern.getPattern();
                Matcher matcher = pattern.matcher(masked);
                masked = matcher.replaceAll(maskingPattern.getReplacement());
            } catch (Exception e) {
                // Log error and continue with next pattern to prevent one bad pattern from
                // breaking masking
                // TODO: Add proper logging here
            }
        }
        return masked;
    }
}
