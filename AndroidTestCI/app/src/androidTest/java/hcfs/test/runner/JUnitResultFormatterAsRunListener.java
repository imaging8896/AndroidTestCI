package hcfs.test.runner;

import android.support.test.internal.runner.listener.InstrumentationRunListener;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.lang.annotation.Annotation;

import hcfs.test.config.Path;
import hcfs.test.utils.FilesManager;
import hcfs.test.utils.XmlReportFormatter;

public class JUnitResultFormatterAsRunListener extends InstrumentationRunListener {

    private final XmlReportFormatter formatter;

    public JUnitResultFormatterAsRunListener() {
        String reportPath = Path.SDCARD_PATH + File.separator + "report";
        File file = new File(reportPath);
        if(file.isDirectory())
            FilesManager.deleteRecursively(file);
        if(!file.mkdir())
            throw new RuntimeException("Unable to mkdir for test result.");
        formatter = new XmlReportFormatter(reportPath);
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        for(Description childDesc : description.getChildren())
            formatter.startTestSuite(childDesc.getClassName());
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        formatter.endAllTestSuite();
    }

    @Override
    public void testStarted(Description description) throws Exception {
        formatter.startTest(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        formatter.endTest(description);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        formatter.addFailure(failure, failure.getException());
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        formatter.addFailure(failure, failure.getException());
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        for(Annotation annotation : description.getAnnotations()) {
//            Logs.d(THIS_CLASS, "testIgnored", annotation.annotationType().getCanonicalName());
            if("hcfs.test.annotation.Bug".equals(annotation.annotationType().getCanonicalName())) {
                formatter.addKnownBug(description);
                return;
            }
        }
        formatter.addIgnored(description);
    }
}
