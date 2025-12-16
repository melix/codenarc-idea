package org.codenarc.idea.error;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.idea.IdeaLogger;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Consumer;
import groovy.lang.GroovySystem;
import io.sentry.Sentry;
import io.sentry.SentryEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.awt.*;

@NullMarked
public class SentryErrorSubmitter extends ErrorReportSubmitter {
    /**
     * @return text that is used on the Error Submit button, e.g. "Report to JetBrains".
     */
    @Override
    public String getReportActionText() {
        return "Report CodeNarc IDEA Issue";
    }

    /**
     * @return the text to display in the UI in T&C of privacy policy (under the stack trace)
     */
    @Override
    public String getPrivacyNoticeText() {
        return "Please provide a brief description to explain how the error occurred. By submitting this bug report " +
            "you are agreeing for the displayed stacktrace to be shared with the developers via " +
            "<a href=\"https://sentry.io\">sentry.io</a>. Please also consider raising a bug directly on our " +
            "<a href=\"https://github.com/melix/codenarc-idea\">Github</a>.";
    }

    @Override
    public boolean submit(
            IdeaLoggingEvent[] events,
            @Nullable String additionalInfo,
            Component parentComponent,
            Consumer consumer
    ) {
        if (!Sentry.isEnabled()) {
            Sentry.init(options -> {
                options.setDsn("https://fd7d1b52354a4d5e91c09af65c80d9dc@o91700.ingest.sentry.io/5748879");
                options.setAttachStacktrace(true);
                options.setAttachServerName(false);

                options.setTag("OS Name", SystemInfo.OS_NAME);
                options.setTag("Java version", SystemInfo.JAVA_VERSION);
                options.setTag("Java vendor", SystemInfo.JAVA_VENDOR);
                options.setTag("Groovy version", GroovySystem.getVersion());
                options.setTag("IDE Name", ApplicationNamesInfo.getInstance().getProductName());
                options.setTag("IDE Full Name", ApplicationNamesInfo.getInstance().getFullProductNameWithEdition());
                options.setTag("IDE Version", ApplicationInfo.getInstance().getFullVersion());
                options.setTag("IDE Build", ApplicationInfo.getInstance().getBuild().asString());
                options.setTag("Is EAP", String.valueOf(ApplicationInfo.getInstance().isEAP()));

                var pluginDescriptor = PluginManager.getPluginByClass(this.getClass());
                if (pluginDescriptor != null) {
                    options.setTag("Plugin", pluginDescriptor.getName());
                    options.setTag("Version", pluginDescriptor.getVersion());
                }
            });
        }

        for (IdeaLoggingEvent e : events) {
            var sentryEvent = new SentryEvent(e.getThrowable());

            if (additionalInfo != null) {
                sentryEvent.setExtra("User Comments", additionalInfo);
            }

            if (StringUtil.isNotEmpty(IdeaLogger.ourLastActionId)) {
                sentryEvent.setExtra("Last Action", IdeaLogger.ourLastActionId);
            }

            ApplicationManager
                    .getApplication()
                    .invokeLater(() -> Sentry.captureEvent(sentryEvent));
        }

        return true; // return true to indicate that a process has begun to send data async
    }
}
