# PDF Crusher

`PDF Crusher` is a tool which tries to

1. minify pdf files, i.e., make them as small as possible, while
2. including all fonts they use inside them, i.e., make them as stand-alone as possible. and
3. making the pdfs as error-free as possible, i.e., making sure that any pdf viewer can correctly display them.

**It is currently under development and does not work yet.**

This is a side-project for me, which I work on whenever I feel tired or want to take a small rest.


## 1. How to use `PDF Crusher` from the Command Line

**Well, it does not work yet, there is no release yet!**

Run `java -jar pdfCrusher.jar ARGUMENTS`

The following arguments are supported:

- `source=/path/to/file` the path to the pdf file to be crushed
- `logger=global,ALL` show progress information (odd, but, ... well)


## 2. Requirements

If you are on Linux, the following utilities can improve the compression which can be achieved by
installing the following additional programs:

* [qpdf](http://qpdf.sourceforge.net/) (`sudo apt-get install qpdf`)
* [pdftk](https://en.wikipedia.org/wiki/PDFtk) (`sudo apt-get install pdftk`)
* [Ghostscript](http://ghostscript.com/) (`sudo apt-get install ghostscript`)


## 3. Use as `PDF Crusher` in your Java Code

You can import the class `PdfCrusher` and then create a job which takes a source file path as input and implements `Runnable`. Call `run()` and have your pdf crushed.

## 4. Licensing

This software itself is `GPL` licensed, it uses a set of other tools internally for which other licenses apply.
The packaged fat-jar of this software, e.g., also contains

1. [pdfbox](https://pdfbox.apache.org/) from Apache, which is under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
2. [itextpdf](http://itextpdf.com/) is under the Affero General Public License ([AGPL 3](http://itextpdf.com/agpl))