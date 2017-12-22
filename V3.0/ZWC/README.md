Zeesoft Web Crawler
===================
Zeesoft Web Crawler (ZWC) is an open source library for Java application development.
It provides support for crawling web sites in order to extract data.
  
A demo implementation of the crawler is included within this library.
To start this demo, run the zwc.jar file with the parameter 'crawl', a parameter that specifies the URL to start and an optional parameter that specifies the full output file name (default 'data.txt').
Example: 'java -jar zwc.jar crawl http://www.w3.org/TR/html401/ C:\temp\output.txt'.
The demo will output a tab separated list of crawled URLs and the text that is found on those URLs.
  
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZWC/releases/zwc-0.9.1.zip) to download the latest ZWC release (version 0.9.1).  
All ZWC releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZWC/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZWC](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/ZWC.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zwc.test.TestPageReader
----------------------------------
This test shows how to create and use a *PageReader* instance.

**Example implementation**  
~~~~
// Create page reader
PageReader reader = new PageReader();
// Read a page
ZStringBuilder page = reader.getPageAtUrl("http://www.w3.org/TR/html401/");
~~~~

A *PageReader* can read a web page at a specified URL.

This test uses the *MockPage*. It is used to read and share the page at http://www.w3.org/TR/html401/ for tests.

Class references;  
 * [TestPageReader](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/TestPageReader.java)
 * [PageReader](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/page/PageReader.java)

**Test output**  
The output of this test shows a substring of the page.
~~~~
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<!-- $Id: cover.html,v 1.3 2017/10/02 10:22:04 denis Exp $ -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HTML 4.01 Specification</title>
<!-- Changed by: Ian B. Jacobs,  6-Dec-1998 -->
<link rel="next" href="about.html">
<link rel="contents" href="cover.html#minitoc">
<link rel="stylesheet" type="text/css" href= 
"http://www.w3.org/StyleSheets/TR/W3C-REC">
<link rel="STYLESHEET" href="style/default.css" type="text/css">
</head>
<body>
<div class="navbar" align="center">&nbsp;<a href="about.html">next</a> &nbsp;
<a href="#minitoc">table of contents</a> &nbsp; <a href="index/elements.html">
elements</a> &nbsp; <a href="index/attributes.html">attributes</a> &nbsp; <a
href="index/list.html">index</a> 
<hr></div>
<div class="head">
<p><a href="http://www.w3.org/"><img height="48" width="72" alt="W3C" src= 
"http://www.w3.org/Icons/w3c_home"></a></p>
<h1>HTML 4.01 Spe
~~~~

nl.zeesoft.zwc.test.TestPageParser
----------------------------------
This test shows how to create and use a *PageParser* instance.

**Example implementation**  
~~~~
// Create page reader
PageParser reader = new PageReader();
// Read a page
ZStringBuilder page = reader.getPageAtUrl("http://www.w3.org/TR/html401/");
// Create page parser
PageParser parser = new PageParser(page);
// Parse the page meta tags
List<ZStringBuilder> metaTags = parser.getTags("meta",true);
// Parse the page anchor tags
List<ZStringBuilder> anchorTags = parser.getTags("a",false);
~~~~

A *PageParser* can parse an HTML web page in order to obtain a list of HTML elements for a specified tag name.

This test uses the *MockPage*. It is used to read and share the page at http://www.w3.org/TR/html401/ for tests.

Class references;  
 * [TestPageParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/TestPageParser.java)
 * [PageParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/page/PageParser.java)

**Test output**  
The output of this test shows a subset of the meta and anchor tags for the page.
~~~~
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<a href="about.html">next</a>
<a href="#minitoc">table of contents</a>
<a href="index/elements.html">elements</a>
~~~~

nl.zeesoft.zwc.test.TestPageTextParser
--------------------------------------
This test shows how to create and use a *PageTextParser* instance.

**Example implementation**  
~~~~
// Create page reader
PageParser reader = new PageReader();
// Read a page
ZStringBuilder page = reader.getPageAtUrl("http://www.w3.org/TR/html401/");
// Create page text parser
PageTextParser parser = new PageTextParser(page);
// Parse the text
ZStringSymbolParser text = parser.getText();
~~~~

A *PageTextParser* can parse an HTML web page in order to obtain all text from the page.

This test uses the *MockPage*. It is used to read and share the page at http://www.w3.org/TR/html401/ for tests.

Class references;  
 * [TestPageTextParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/TestPageTextParser.java)
 * [PageTextParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/page/PageTextParser.java)

**Test output**  
The output of this test shows a substring of the text for the page.
~~~~
This specification defines the HyperText Markup Language (HTML), the publishing language of the World Wide Web. This specification defines HTML 4.01, 
~~~~

nl.zeesoft.zwc.test.TestRobotsParser
------------------------------------
This test shows how to create and use a *RobotsParser* instance.

**Example implementation**  
~~~~
// Create robots parser
RobotsParser robots = new RobotsParser(new PageReader(),"http://www.w3.org/TR/html401/");
// Get the disallowed URL list
List<String> disallowedUrls = robots.getDisallowedUrls();
~~~~

A *RobotsParser* can parse the robots.txt file and return the disallowed URL list.

Class references;  
 * [TestRobotsParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/TestRobotsParser.java)
 * [RobotsParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/page/RobotsParser.java)

**Test output**  
The output of this test shows a subset of the disallowed for the web site at http://www.w3.org/.
~~~~
http://www.w3.org/*/wp-admin/
http://www.w3.org/*/wp-includes/
http://www.w3.org/*/wp-content/plugins/
~~~~

nl.zeesoft.zwc.test.TestCrawler
-------------------------------
This test shows how to create and use a *Crawler* instance.

**Example implementation**  
~~~~
// Create crawler
Crawler crawler = new Crawler("http://www.w3.org/TR/html401/");
// Initialize crawler
String err = crawler.initialize();
// Start crawler
crawler.start();
// Get the crawled URLs
List<String> crawledUrls = crawler.getCrawledUrls();
// Get the crawled pages
TreeMap<String,ZStringBuilder> pages = crawler.getPages();
~~~~

A *Crawler* can crawl a web site in order to obtain all page data from the site.

Class references;  
 * [TestCrawler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/test/TestCrawler.java)
 * [Crawler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZWC/src/nl/zeesoft/zwc/Crawler.java)

**Test output**  
The output of this test shows a subset of the crawled URLs for the site under http://www.w3.org/TR/html401/.
~~~~
http://www.w3.org/TR/html401/
http://www.w3.org/TR/html401/about.html
http://www.w3.org/TR/html401/index/elements.html
http://www.w3.org/TR/html401/index/attributes.html
http://www.w3.org/TR/html401/index/list.html
~~~~

Test results
------------
All 5 tests have been executed successfully (7 assertions).  
Total test duration: 12634 ms (total sleep duration: 10000 ms).  

Memory usage per test;  
 * nl.zeesoft.zwc.test.TestPageReader: 874 Kb / 0 Mb
 * nl.zeesoft.zwc.test.TestPageParser: 773 Kb / 0 Mb
 * nl.zeesoft.zwc.test.TestPageTextParser: 788 Kb / 0 Mb
 * nl.zeesoft.zwc.test.TestRobotsParser: 787 Kb / 0 Mb
 * nl.zeesoft.zwc.test.TestCrawler: 767 Kb / 0 Mb
