package edu.washington.cse373;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * This is our base test class, with a couple definitions and extensions applied to make it easier
 * to write other tests.
 *
 * This class uses some weird code design to minimize the imports needed in subclasses---typically,
 * you should not nest unrelated classes such as FormattingTestReporter.
 */
@ExtendWith(BaseTest.FormattingTestReporterParameterResolver.class)
public class BaseTest implements WithAssertions {
    public static class FormattingTestReporter {
        public static final String OUTPUT_KEY = "output";
        public static final String DUMMY_CHARACTER = "¶";
        private final TestReporter reporter;

        public FormattingTestReporter(TestReporter reporter) {
            this.reporter = reporter;
        }

        public  void publish() {
            this.publish("");
        }

        public void publish(String s) {
            if (s.isBlank()) {
                s = s + DUMMY_CHARACTER;
            }
            this.reporter.publishEntry(OUTPUT_KEY, s);
        }

        public void publish(String s, Object... args) {
            publish(String.format(s, args));
        }
    }

    public static class FormattingTestReporterParameterResolver implements ParameterResolver {
        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return (parameterContext.getParameter().getType() == FormattingTestReporter.class);
        }

        @Override
        public FormattingTestReporter resolveParameter(ParameterContext parameterContext,
                                                       ExtensionContext extensionContext) {
            return new FormattingTestReporter(extensionContext::publishReportEntry);
        }
    }
}
