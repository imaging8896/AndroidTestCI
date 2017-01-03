<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:lxslt="http://xml.apache.org/xslt"
        xmlns:string="xalan://java.lang.String">
<xsl:output method="html" indent="yes" encoding="UTF-8"
  doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN" />
<xsl:decimal-format decimal-separator="." grouping-separator="," />
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 -->

<xsl:param name="TITLE">Connected Android Instrumentation Test Results.</xsl:param>

<!--

 Sample stylesheet to be used with Ant JUnitReport output.

 It creates a non-framed report that can be useful to send via
 e-mail or such.

-->
<xsl:template match="testsuites">
    <html>
        <head>
            <title><xsl:value-of select="$TITLE"/></title>
    
      <script type="text/javascript" language="JavaScript">
        var TestCases = new Array();
        var cur;
        <xsl:for-each select="./testsuite">
            <xsl:apply-templates select="properties"/>
        </xsl:for-each>

       </script>
       <script type="text/javascript" language="JavaScript"><![CDATA[
        function displayProperties (name) {
          var win = window.open('','JUnitSystemProperties','scrollbars=1,resizable=1');
          var doc = win.document;
          doc.open();
          doc.write("<html><head><title>Properties of " + name + "</title>");
          doc.write("<style>")
          doc.write("body {font:normal 68% verdana,arial,helvetica; color:#000000; }");
          doc.write("table tr td, table tr th { font-size: 68%; }");
          doc.write("table.properties { border-collapse:collapse; border-left:solid 1 #cccccc; border-top:solid 1 #cccccc; padding:5px; }");
          doc.write("table.properties th { text-align:left; border-right:solid 1 #cccccc; border-bottom:solid 1 #cccccc; background-color:#eeeeee; }");
          doc.write("table.properties td { font:normal; text-align:left; border-right:solid 1 #cccccc; border-bottom:solid 1 #cccccc; background-color:#fffffff; }");
          doc.write("h3 { margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica }");
          doc.write("</style>");
          doc.write("</head><body>");
          doc.write("<h3>Properties of " + name + "</h3>");
          doc.write("<div align=\"right\"><a href=\"javascript:window.close();\">Close</a></div>");
          doc.write("<table class='properties'>");
          doc.write("<tr><th>Name</th><th>Value</th></tr>");
          for (prop in TestCases[name]) {
            doc.write("<tr><th>" + prop + "</th><td>" + TestCases[name][prop] + "</td></tr>");
          }
          doc.write("</table>");
          doc.write("</body></html>");
          doc.close();
          win.focus();
        }
      ]]>
      </script>
        </head>
        <body>
            <a name="top"></a>
            <xsl:call-template name="pageHeader"/>

            <!-- Summary part -->
            <xsl:call-template name="summary"/>
            <hr size="1" width="95%" align="left"/>

            <!-- Package List part -->
            <xsl:call-template name="packagelist"/>
            <hr size="1" width="95%" align="left"/>

            <!-- For each package create its part -->
            <xsl:call-template name="packages"/>
            <hr size="1" width="95%" align="left"/>

            <!-- For each class create the  part -->
            <xsl:call-template name="classes"/>

        </body>
    </html>
</xsl:template>



    <!-- ================================================================== -->
    <!-- Write a list of all packages with an hyperlink to the anchor of    -->
    <!-- of the package name.                                               -->
    <!-- ================================================================== -->
    <xsl:template name="packagelist">
        <h2>Packages</h2>
        Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.
        <table class="details" border="1" cellpadding="5" cellspacing="2" width="95%">
            <xsl:call-template name="testsuite.test.header"/>
            <!-- list all packages recursively -->
            <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
                <xsl:sort select="@package"/>
                <xsl:variable name="testsuites-in-package" select="/testsuites/testsuite[./@package = current()/@package]"/>
                <xsl:variable name="testCount" select="sum($testsuites-in-package/@tests)"/>
                <xsl:variable name="errorCount" select="sum($testsuites-in-package/@errors)"/>
                <xsl:variable name="failureCount" select="sum($testsuites-in-package/@failures)"/>
                <xsl:variable name="bugCount" select="sum($testsuites-in-package/@bug)" />
                <xsl:variable name="skippedCount" select="sum($testsuites-in-package/@skipped)" />
                <xsl:variable name="timeCount" select="sum($testsuites-in-package/@time)"/>

                <!-- write a summary for the package -->
                <tr valign="top">
                    <!-- set a nice color depending if there is an error/failure -->
                    <xsl:attribute name="class">
                        <xsl:choose>
                            <xsl:when test="$failureCount &gt; 0">Failure</xsl:when>
                            <xsl:when test="$errorCount &gt; 0">Error</xsl:when>
                        </xsl:choose>
                    </xsl:attribute>
                    <td><a href="#{@package}"><xsl:value-of select="@package"/></a></td>
                    <td><xsl:value-of select="$testCount"/></td>
                    <td><xsl:value-of select="$errorCount"/></td>
                    <td><xsl:value-of select="$failureCount"/></td>
                    <td><xsl:value-of select="$bugCount"/></td>
                    <td><xsl:value-of select="$skippedCount" /></td>
                    <td>
                    <xsl:call-template name="display-time">
                        <xsl:with-param name="value" select="$timeCount"/>
                    </xsl:call-template>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>


    <!-- ================================================================== -->
    <!-- Write a package level report                                       -->
    <!-- It creates a table with values from the document:                  -->
    <!-- Name | Tests | Errors | Failures | Time                            -->
    <!-- ================================================================== -->
    <xsl:template name="packages">
        <!-- create an anchor to this package name -->
        <xsl:for-each select="/testsuites/testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
            <xsl:sort select="@package"/>
                <a name="{@package}"></a>
                <h3>Package <xsl:value-of select="@package"/></h3>

                <table class="details" border="1" cellpadding="5" cellspacing="2" width="95%">
                    <xsl:call-template name="testsuite.test.header.device"/>

                    <!-- match the testsuites of this package -->
                    <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test.device"/>
                </table>
                <a href="#top">Back to top</a>
                <p/>
                <p/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="classes">
        <xsl:for-each select="testsuite">
            <xsl:sort select="@name"/>
            <!-- create an anchor to this class name -->
            <a name="{@name}"></a>
            <h3>TestSuite <xsl:value-of select="@name"/> (<xsl:value-of select="properties/property[./@name = 'device']/@value"/>)(<xsl:value-of select="properties/property[./@name = 'Tera']/@value"/>)</h3>

            <table class="details" border="1" cellpadding="5" cellspacing="2" width="95%">
              <xsl:call-template name="testcase.test.header"/>
              <!--
              test can even not be started at all (failure to load the class)
              so report the error directly
              -->
                <xsl:if test="./error">
                    <tr class="Error">
                        <td colspan="4"><xsl:apply-templates select="./error"/></td>
                    </tr>
                </xsl:if>
                <xsl:apply-templates select="./testcase" mode="print.test"/>
            </table>
            <p/>

            <a href="#top">Back to top</a>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="summary">
        <h2>Summary </h2>

        <xsl:variable name="testCount" select="sum(testsuite/@tests)"/>
        <xsl:variable name="errorCount" select="sum(testsuite/@errors)"/>
        <xsl:variable name="failureCount" select="sum(testsuite/@failures)"/>
         <xsl:variable name="bugCount" select="sum(testsuite/@bug)" />
        <xsl:variable name="skippedCount" select="sum(testsuite/@skipped)" />
        <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>
        <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount - $bugCount) div $testCount"/>

        <table class="details" border="1" cellpadding="5" cellspacing="2" width="95%">
        <tr valign="top">
            <th>Tests</th>
            <th>Failures</th>
            <th>Errors</th>
            <th>Bugs</th>
            <th>Skipped</th>
            <th>Success rate</th>
            <th>Time</th>
        </tr>
        <tr valign="top">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="$failureCount &gt; 0">Failure</xsl:when>
                    <xsl:when test="$errorCount &gt; 0">Error</xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <td><xsl:value-of select="$testCount"/></td>
            <td><xsl:value-of select="$failureCount"/></td>
            <td><xsl:value-of select="$errorCount"/></td>
            <td><xsl:value-of select="$bugCount" /></td>
            <td><xsl:value-of select="$skippedCount" /></td>
            <td>
                <xsl:call-template name="display-percent">
                    <xsl:with-param name="value" select="$successRate"/>
                </xsl:call-template>
            </td>
            <td>
                <xsl:call-template name="display-time">
                    <xsl:with-param name="value" select="$timeCount"/>
                </xsl:call-template>
            </td>

        </tr>
        </table>
        <table border="0" width="95%">
        <tr>
        <td style="text-align: justify;">
        Note: <i>failures</i> are anticipated and checked for with assertions while <i>errors</i> are unanticipated.
        </td>
        </tr>
        </table>
    </xsl:template>

  <!--
   Write properties into a JavaScript data structure.
   This is based on the original idea by Erik Hatcher (ehatcher@apache.org)
   -->
  <xsl:template match="properties">
    cur = TestCases['<xsl:value-of select="../@package"/>.<xsl:value-of select="../@name"/>'] = new Array();
    <xsl:for-each select="property">
    <xsl:sort select="@name"/>
        cur['<xsl:value-of select="@name"/>'] = '<xsl:call-template name="JS-escape"><xsl:with-param name="string" select="@value"/></xsl:call-template>';
    </xsl:for-each>
  </xsl:template>

<!-- Page HEADER -->
<xsl:template name="pageHeader">
    <h1><xsl:value-of select="$TITLE"/></h1>
    <table width="100%">
    <tr>
        <td align="left"></td>
        <td align="right">Designed for use with <a href='http://www.junit.org'>JUnit</a> and <a href='http://ant.apache.org/ant'>Ant</a>.</td>
    </tr>
    </table>
    <hr size="1"/>
</xsl:template>

<xsl:template match="testsuite" mode="header">
    <tr valign="top">
        <th width="80%">TestSuite</th>
        <th>Tests</th>
        <th>Errors</th>
        <th>Failures</th>
        <th>Bugs</th>
        <th>Skipped</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<!-- class header -->
<xsl:template name="testsuite.test.header">
    <tr valign="top">
        <th width="80%">TestSuite</th>
        <th>Tests</th>
        <th>Errors</th>
        <th>Failures</th>
        <th>Bugs</th>
        <th>Skipped</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<xsl:template name="testsuite.test.header.device">
    <tr valign="top">
        <th width="50%">TestSuite</th>
        <th width="20%">Device</th>
        <th width="10%">Tera</th>
        <th>Tests</th>
        <th>Errors</th>
        <th>Failures</th>
        <th>Bugs</th>
        <th>Skipped</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<!-- method header -->
<xsl:template name="testcase.test.header">
    <tr valign="top">
        <th>#</th>
        <th width="20%">TestCase</th>
        <th>Status</th>
        <th width="70%">Type</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<xsl:template match="testsuite" mode="print.test.device">
  <xsl:variable name="altColor">
        <xsl:choose>
            <xsl:when test="@id mod 2 = 0">#FFFFFF</xsl:when>
            <xsl:otherwise>#D3DFEE</xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <tr valign="top" bgcolor="{$altColor}">
        <!-- set a nice color depending if there is an error/failure -->
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
                <xsl:when test="@errors[.&gt; 0]">Error</xsl:when>
            </xsl:choose>
        </xsl:attribute>

        <!-- print testsuite information -->
        <td><a href="#{@name}"><xsl:value-of select="@name"/></a></td>
        <td><xsl:value-of select="properties/property[./@name = 'device']/@value"/></td>
        <td><xsl:value-of select="properties/property[./@name = 'Tera']/@value"/></td>
        <td><xsl:value-of select="@tests"/></td>
        <td><xsl:value-of select="@errors"/></td>
        <td><xsl:value-of select="@failures"/></td>
        <td><xsl:value-of select="@bug" /></td>
        <td><xsl:value-of select="@skipped" /></td>
        <td>
            <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
    </tr>
</xsl:template>

<!-- class information -->
<xsl:template match="testsuite" mode="print.test">
    <tr valign="top">
        <!-- set a nice color depending if there is an error/failure -->
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
                <xsl:when test="@errors[.&gt; 0]">Error</xsl:when>
            </xsl:choose>
        </xsl:attribute>

        <!-- print testsuite information -->
        <td><a href="#{@name}"><xsl:value-of select="@name"/></a></td>
        <td><xsl:value-of select="@tests"/></td>
        <td><xsl:value-of select="@errors"/></td>
        <td><xsl:value-of select="@failures"/></td>
        <td><xsl:value-of select="@bug"/></td>
        <td><xsl:value-of select="@skipped" /></td>
        <td>
            <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
    </tr>
</xsl:template>

<xsl:template match="testcase" mode="print.test">
    <tr valign="top">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="failure | error">Error</xsl:when>
            </xsl:choose>
        </xsl:attribute>
        <td><xsl:number format="0" /></td>
        <td><xsl:value-of select="@name"/></td>
        <xsl:choose>
            <xsl:when test="failure">
                <td bgcolor="#FFFF00">Failure</td>
                <td><xsl:apply-templates select="failure"/></td>
            </xsl:when>
            <xsl:when test="error">
                <td bgcolor="#FF0000">Error</td>
                <td><xsl:apply-templates select="error"/></td>
            </xsl:when>
            <xsl:when test="bug">
              <td bgcolor="#FF33F6">Bug</td>
              <td><xsl:value-of select="."/></td>
            </xsl:when>
            <xsl:when test="skipped">
              <td bgcolor="#00FFFF">Skipped</td>
              <td><xsl:value-of select="."/></td>
            </xsl:when>
            <xsl:otherwise>
                <td bgcolor="#00FF00">Success</td>
                <td></td>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
    </tr>
</xsl:template>


<xsl:template match="failure">
    <xsl:call-template name="display-failures"/>
</xsl:template>

<xsl:template match="error">
    <xsl:call-template name="display-failures"/>
</xsl:template>

<!-- Style for the error, failure and skipped in the testcase template -->
<xsl:template name="display-failures">
    <xsl:choose>
        <xsl:when test="not(@message)">N/A</xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="@message"/>
        </xsl:otherwise>
    </xsl:choose>
    <!-- display the stacktrace -->
    <code>
        <br/><br/>
        <xsl:call-template name="br-replace">
            <xsl:with-param name="word" select="."/>
        </xsl:call-template>
    </code>
    <!-- the later is better but might be problematic for non-21" monitors... -->
    <!--pre><xsl:value-of select="."/></pre-->
</xsl:template>

<xsl:template name="JS-escape">
    <xsl:param name="string"/>
    <xsl:param name="tmp1" select="string:replaceAll(string:new(string($string)),'\\','\\\\')"/>
    <xsl:param name="tmp2" select="string:replaceAll(string:new(string($tmp1)),&quot;'&quot;,&quot;\\&apos;&quot;)"/>
    <xsl:param name="tmp3" select="string:replaceAll(string:new(string($tmp2)),&quot;&#10;&quot;,'\\n')"/>
    <xsl:param name="tmp4" select="string:replaceAll(string:new(string($tmp3)),&quot;&#13;&quot;,'\\r')"/>
    <xsl:value-of select="$tmp4"/>
</xsl:template>


<!--
    template that will convert a carriage return into a br tag
    @param word the text from which to convert CR to BR tag
-->
<xsl:template name="br-replace">
    <xsl:param name="word"/>
    <xsl:param name="splitlimit">32</xsl:param>
    <xsl:variable name="secondhalflen" select="(string-length($word)+(string-length($word) mod 2)) div 2"/>
    <xsl:variable name="secondhalfword" select="substring($word, $secondhalflen)"/>
    <!-- When word is very big, a recursive replace is very heap/stack expensive, so subdivide on line break after middle of string -->
    <xsl:choose>
      <xsl:when test="(string-length($word) > $splitlimit) and (contains($secondhalfword, '&#xa;'))">
        <xsl:variable name="secondhalfend" select="substring-after($secondhalfword, '&#xa;')"/>
        <xsl:variable name="firsthalflen" select="string-length($word) - $secondhalflen"/>
        <xsl:variable name="firsthalfword" select="substring($word, 1, $firsthalflen)"/>
        <xsl:variable name="firsthalfend" select="substring-before($secondhalfword, '&#xa;')"/>
        <xsl:call-template name="br-replace">
          <xsl:with-param name="word" select="concat($firsthalfword,$firsthalfend)"/>
        </xsl:call-template>
        <br/>
        <xsl:call-template name="br-replace">
          <xsl:with-param name="word" select="$secondhalfend"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains($word, '&#xa;')">
        <xsl:value-of select="substring-before($word, '&#xa;')"/>
        <br/>
        <xsl:call-template name="br-replace">
          <xsl:with-param name="word" select="substring-after($word, '&#xa;')"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$word"/>
      </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="display-time">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.000')"/>
</xsl:template>

<xsl:template name="display-percent">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.00%')"/>
</xsl:template>

</xsl:stylesheet>
