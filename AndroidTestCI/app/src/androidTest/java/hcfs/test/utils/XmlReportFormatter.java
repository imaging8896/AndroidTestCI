package hcfs.test.utils;

import android.os.Build;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner;
import org.apache.tools.ant.util.DOMElementWriter;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Hashtable;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hcfs.test.annotation.Bug;

public class XmlReportFormatter {

    //junit4 xsd : https://gist.github.com/kuzuha/232902acab1344d6b578

    private String pathToSaveReport;
    private final Hashtable<String, TestCase> testStarts = new Hashtable<>();
    private final Hashtable<String, TestSuite> testsuiteStarts = new Hashtable<>();

    public XmlReportFormatter(String pathToSaveReport) {
        this.pathToSaveReport = pathToSaveReport;
    }

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (final Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    public void startTestSuite(String name) {
        TestSuite testsuite = new TestSuite();
        testsuite.doc = getDocumentBuilder().newDocument();
        testsuite.element = testsuite.doc.createElement("testsuite");
        testsuiteStarts.put(name, testsuite);
        testsuite.element.setAttribute("name", name);
        //add the timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.TAIWAN);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        testsuite.element.setAttribute("timestamp", dateFormat.format(timestamp));
        //and the hostname.
//        testsuite.element.setAttribute(HOSTNAME, getHostname());
        // Output properties
        final Element propsElement = testsuite.doc.createElement("properties");
        Element propElement = testsuite.doc.createElement("property");
        propElement.setAttribute("name", "device");
        //MODEL:AOSP on BullHead
        //MANUFACTURER:LGE
        //VERSION.RELEASE:6.0
        propElement.setAttribute("value", android.os.Build.MODEL + " " + Build.VERSION.RELEASE);
        propsElement.appendChild(propElement);
        propElement = testsuite.doc.createElement("property");
        propElement.setAttribute("name", "Tera");
        propElement.setAttribute("value", "2.2.3." + CommandUtils.exec("getprop ro.build.version.incremental"));
        propsElement.appendChild(propElement);
        testsuite.element.appendChild(propsElement);
    }

    public void endAllTestSuite() {
        for(String className : testsuiteStarts.keySet()) {
            TestSuite testsuite = testsuiteStarts.get(className);
            testsuite.element.setAttribute("tests", "" + testsuite.testCount);
            testsuite.element.setAttribute("failures", "" + testsuite.failureCount);
            testsuite.element.setAttribute("errors", "" + testsuite.errorCount);
            testsuite.element.setAttribute("bug", "" + testsuite.bugCount);
            testsuite.element.setAttribute("skipped", "" + testsuite.skippedCount);
            testsuite.element.setAttribute("time", "" + testsuite.time);
            writeFile(className, testsuite.element);
        }
    }

    public void startTest(final Description description) {
        String className = description.getClassName();
        if(!testsuiteStarts.containsKey(className))
            startTestSuite(className);
        TestSuite testsuite = testsuiteStarts.get(className);
        TestCase testcase = new TestCase();
        testcase.startTime = System.currentTimeMillis();
        testcase.element = testsuite.doc.createElement("testcase");
        testcase.element.setAttribute("name", description.getMethodName());
        testcase.element.setAttribute("classname", className);
        testsuite.testCount++;
        testsuite.element.appendChild(testcase.element);
        testStarts.put(description.getDisplayName(), testcase);
    }

    public void endTest(final Description description) {
        if (!testStarts.containsKey(description.getDisplayName()))
            startTest(description);
        TestSuite testsuite = testsuiteStarts.get(description.getClassName());
        TestCase testcase = testStarts.get(description.getDisplayName());
        Double time = (System.currentTimeMillis() - testcase.startTime) / 1000.0;
        testcase.element.setAttribute("time", "" + time);
        testsuite.time += time;
    }

    public void addFailure(Failure failure, final Throwable t) {
        Description description = failure.getDescription();
        TestSuite testsuite = testsuiteStarts.get(description.getClassName());
        TestCase testcase = testStarts.get(description.getDisplayName());
        Element nested;
        if(t instanceof  java.lang.AssertionError) {
            nested = testsuite.doc.createElement("failure");
            testsuite.failureCount++;
        } else {
            nested = testsuite.doc.createElement("error");
            testsuite.errorCount++;
        }
        final String message = t.getMessage();
        if (message != null && message.length() > 0)
            nested.setAttribute("message", t.getMessage());
        nested.setAttribute("type", t.getClass().getName());
        final String strace = JUnitTestRunner.getFilteredTrace(t);
        final Text trace = testsuite.doc.createTextNode(strace);
        nested.appendChild(trace);
        testcase.element.appendChild(nested);
    }

    public void addKnownBug(Description description) {
        TestSuite testsuite = testsuiteStarts.get(description.getClassName());
        testsuite.bugCount++;
        if(!testStarts.containsKey(description.getDisplayName()))
            startTest(description);
        TestCase testcase = testStarts.get(description.getDisplayName());
        Element nested = testsuite.doc.createElement("bug");
        final Text msg = testsuite.doc.createTextNode(description.getAnnotation(Bug.class).value());
        nested.appendChild(msg);
        testcase.element.appendChild(nested);
    }

    public void addIgnored(Description description) {
        TestSuite testsuite = testsuiteStarts.get(description.getClassName());
        testsuite.skippedCount++;
        if(!testStarts.containsKey(description.getDisplayName()))
            startTest(description);
        TestCase testcase = testStarts.get(description.getDisplayName());
        Element nested = testsuite.doc.createElement("skipped");
        final Text msg = testsuite.doc.createTextNode(description.getAnnotation(Ignore.class).value());
        nested.appendChild(msg);
        testcase.element.appendChild(nested);
    }

    private void writeFile(String name, Element rootElement) {
        try {
            File reportFile = new File(pathToSaveReport + File.separator + name + ".xml");
            if(reportFile.exists()) {
                if(!reportFile.delete())
                    throw new BuildException("Unable to remove old log file");
            }
            if(!reportFile.createNewFile())
                throw new BuildException("Unable to create log file");
            try(Writer wri = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile), "UTF8"))) {
                wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch (final IOException exc) {
                throw new BuildException("Unable to write log file", exc);
            }
        } catch (IOException e) {
            throw new BuildException("Unable to write log file", e);
        }
    }

    private class TestCase {
        long startTime;
        Element element;
    }

    private class TestSuite {
        double time = 0;
        int testCount = 0;
        int failureCount = 0;
        int bugCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        Document doc;
        Element element;
    }
}
