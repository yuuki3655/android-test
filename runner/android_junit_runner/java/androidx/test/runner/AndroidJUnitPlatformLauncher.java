package androidx.test.runner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import androidx.test.internal.runner.ClassPathScanner;
import androidx.test.internal.runner.listener.InstrumentationResultPrinter;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

// import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class AndroidJUnitPlatformLauncher extends MonitoringInstrumentation {

    private static String TAG = AndroidJUnitPlatformLauncher.class.getSimpleName();

    @Override
    public void onCreate(Bundle arguments) {
        Log.d(TAG, "onCreate");
        super.onCreate(arguments);
        start();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

        InstrumentationResultPrinter printer = new InstrumentationResultPrinter();

        Log.d(TAG, "My JUnit runner version is " + junit.runner.Version.id());

//        Log.d(TAG, "Waiting for Debugger");
//        Debug.waitForDebugger();

        Log.d(TAG, "Creating Launcher");
        Launcher launcher = LauncherFactory.create();

        Log.d(TAG, "Creating discovery request");
        LauncherDiscoveryRequest discoveryRequest = LauncherDiscoveryRequestBuilder.request()
                  .selectors(selectPackage("com.example.myjunit5experiment"))
//                .selectors(
//                        selectClasspathRoots(
//                                ClassPathScanner.getDefaultClasspaths(this).stream()
//                                .map(it -> new File(it).toPath())
//                                .collect(Collectors.toSet())
//                        )
//                )
                .filters(includeClassNamePatterns(".*"))
                .build();

        try {
            Class<?> testClass = Class.forName("com.example.myjunit5experiment.ExampleJunit4Test");
            Log.d(TAG, "test class = " + testClass.getSimpleName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        Log.d(TAG, "discovering tests");
        TestPlan testPlan = launcher.discover(discoveryRequest);

        for (TestIdentifier id : testPlan.getRoots()) {
            Log.d(TAG, "Test name: " + id.getDisplayName());
            for (TestIdentifier childId : testPlan.getChildren(id)) {
                Log.d(TAG, "  name: " + childId.getDisplayName());
            }
        }

        Log.d(TAG, "executing tests");
        SummaryGeneratingListener summary = new SummaryGeneratingListener();
        launcher.execute(testPlan, new TestExecutionListener(), summary);

        Log.d(TAG, "done: executed " + summary.getSummary().getTestsFoundCount() + " tests.");
        summary.getSummary().printTo(new PrintWriter(System.err, true));
        Bundle resultBundle = new Bundle();
        finish(Activity.RESULT_OK, resultBundle);
    }

    public static class TestExecutionListener implements org.junit.platform.launcher.TestExecutionListener {

        private static String TAG = TestExecutionListener.class.getSimpleName();

        @Override
        public void testPlanExecutionStarted(TestPlan testPlan) {
            Log.d(TAG, "execution started");
        }

        @Override
        public void testPlanExecutionFinished(TestPlan testPlan) {
            Log.d(TAG, "execution finished");
        }

        @Override
        public void dynamicTestRegistered(TestIdentifier testIdentifier) {
            Log.d(TAG, "dynamic test registered");
        }

        @Override
        public void executionSkipped(TestIdentifier testIdentifier, String reason) {
            Log.d(TAG, "execution skipped");
        }

        @Override
        public void executionStarted(TestIdentifier testIdentifier) {
            Log.d(TAG, "execution started: " + testIdentifier.getDisplayName());
        }

        @Override
        public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
            Log.d(TAG, "execution finished: " + testIdentifier.getDisplayName() + ", result = " + testExecutionResult.toString());
        }

        @Override
        public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
            Log.d(TAG, "reporting entry published");
        }
    }
}
