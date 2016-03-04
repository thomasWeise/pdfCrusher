package thomasWeise.pdfCrusher.tools.pdfbox;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.NonSequentialPDFParser;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.optimizationBenchmarking.utils.io.paths.PathUtils;
import org.optimizationBenchmarking.utils.parallel.Execute;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;
import thomasWeise.pdfCrusher.utils.Image;

/** The pdfbox-based pdf checker job */
final class _PdfBoxCheckerJob extends PdfCheckerJob {

  /**
   * the maximum error returned by {@link #__checkPDDocument(PDDocument)}
   */
  private static final int CHECK_PD_DOCUMENT_MAX = (PdfCheckerJob.MAX_PAGE_ERRORS
      + 4);

  /**
   * the maximum error returned by {@link #__checkCOSDocument(COSDocument)}
   */
  private static final int CHECK_COS_DOCUMENT_MAX = 4;

  /**
   * the maximum error returned by {@link #__checkPDFParser(PDFParser)}
   */
  private static final int CHECK_PDF_PARSER_MAX = (3
      + _PdfBoxCheckerJob.CHECK_COS_DOCUMENT_MAX
      + _PdfBoxCheckerJob.CHECK_PD_DOCUMENT_MAX);

  /**
   * the maximum error returned by {@link #check()}
   */
  private static final int MAX_RESULT = (1
      + (2 * _PdfBoxCheckerJob.CHECK_PDF_PARSER_MAX));

  static {
    System.setProperty("org.apache.commons.logging.Log", //$NON-NLS-1$
        "org.apache.commons.logging.impl.NoOpLog"); //$NON-NLS-1$
  }

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _PdfBoxCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  protected int check() throws Throwable {
    final Logger logger;
    Future<Integer> parser, nonSequentialParser;
    long errors;

    errors = 0;

    try {
      parser = Execute.parallel(new __CheckPdfParser());
      nonSequentialParser = Execute.parallel(//
          new __CheckNonSequentialPdfParser());

      errors += Math.max(0L, parser.get().intValue());
      parser = null;
      errors += Math.max(0L, nonSequentialParser.get().intValue());
      nonSequentialParser = null;
    } catch (@SuppressWarnings("unused") final Throwable error) {
      errors = _PdfBoxCheckerJob.MAX_RESULT;
    }

    if (errors > 0) {
      logger = this.getLogger();
      if ((logger != null) && (logger.isLoggable(Level.INFO))) {
        logger.info(((((PdfBoxChecker.getInstance().toString() + //
            " discovered " + errors) + //$NON-NLS-1$
            " errors in document '") + this.getSourceFile()) + '\'') //$NON-NLS-1$
            + '.');
      }
    }

    return Math.max(0,
        ((int) (Math.min(_PdfBoxCheckerJob.MAX_RESULT, errors))));
  }

  /** {@inheritDoc} */
  @Override
  protected final int getMaxErrors() {
    return _PdfBoxCheckerJob.MAX_RESULT;
  }

  /**
   * check whether the PDFParser works well.
   *
   * @return the error count, between 0 and {@value #CHECK_PDF_PARSER_MAX}
   */
  final int _checkPDFParser() {
    PDFParser parser;
    try (final InputStream fis = PathUtils
        .openInputStream(this.getSourceFile())) {
      parser = new PDFParser(fis, null, false);
      return this.__checkPDFParser(parser);
    } catch (@SuppressWarnings("unused") final Throwable error) {
      return _PdfBoxCheckerJob.CHECK_PDF_PARSER_MAX;
    }
  }

  /**
   * check whether the PDFParser works well.
   *
   * @param parser
   *          the parser
   * @return the error count, between 0 and {@value #CHECK_PDF_PARSER_MAX}
   */
  private final int __checkPDFParser(final PDFParser parser) {
    Path path;
    int errors;

    errors = 0;
    try {
      try {
        if (this.useSystemTempDir()
            || ((path = (this.getSourceFile().getParent())) == null)) {
          path = PathUtils.getTempDir();
        }
        parser.setTempDirectory(PathUtils.getPhysicalFile(path));

        parser.parse(); // check the PDDocument
        try (final PDDocument doc = parser.getPDDocument()) {
          errors += _PdfBoxCheckerJob.__checkPDDocument(doc);
          try (final COSDocument cdoc = parser.getDocument()) {
            errors += _PdfBoxCheckerJob.__checkCOSDocument(cdoc);
          } catch (@SuppressWarnings("unused") final Throwable error) {
            errors += (1 + _PdfBoxCheckerJob.CHECK_COS_DOCUMENT_MAX);
          }
        } catch (@SuppressWarnings("unused") final Throwable error) {
          errors += (1 + _PdfBoxCheckerJob.CHECK_PD_DOCUMENT_MAX);
        }
      } finally {
        parser.clearResources();
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      return _PdfBoxCheckerJob.CHECK_PDF_PARSER_MAX;
    }

    return Math.max(0,
        Math.min(_PdfBoxCheckerJob.CHECK_PDF_PARSER_MAX, errors));
  }

  /**
   * check a COS document
   *
   * @param doc
   *          the document
   * @return the error count: between 0 and
   *         {@value #CHECK_COS_DOCUMENT_MAX}
   */
  private static final int __checkCOSDocument(final COSDocument doc) {
    int errors;

    errors = 0;

    try {
      if (doc.getDocumentID() == null) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    try {
      if (doc.getHeaderString() == null) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    try {
      if (doc.getObjects() == null) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    try {
      if (doc.getVersion() < 1f) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    return Math.max(0,
        Math.min(_PdfBoxCheckerJob.CHECK_COS_DOCUMENT_MAX, errors));
  }

  /**
   * check whether the PDFParser works well.
   *
   * @return the error count, between 0 and {@value #CHECK_PDF_PARSER_MAX}
   */
  final int _checkNonSequentialPDFParser() {
    NonSequentialPDFParser parser;
    try {
      parser = new NonSequentialPDFParser(
          PathUtils.getPhysicalFile(this.getSourceFile()), null);
      return this.__checkPDFParser(parser);
    } catch (@SuppressWarnings("unused") final Throwable error) {
      return _PdfBoxCheckerJob.CHECK_PDF_PARSER_MAX;
    }
  }

  /**
   * check a PD document
   *
   * @param doc
   *          the document
   * @return the error count: between 0 and {@value #CHECK_PD_DOCUMENT_MAX}
   */
  private static final int __checkPDDocument(final PDDocument doc) {
    int errors;

    errors = 0;

    try {
      if (doc.getDocumentCatalog() == null) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }
    try {
      if (doc.getDocumentInformation() == null) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }
    try {
      if (doc.getNumberOfPages() <= 0) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    try {
      if (doc.getPageMap().isEmpty()) {
        ++errors;
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      ++errors;
    }

    errors += _PdfBoxCheckerJob.__checkPDDocumentPages(doc);

    return Math.max(0,
        Math.min(errors, _PdfBoxCheckerJob.CHECK_PD_DOCUMENT_MAX));
  }

  /**
   * check the pages of a PD document
   *
   * @param doc
   *          the document
   * @return the error count: between 0 and {@value #MAX_PAGE_ERRORS}
   */
  private static final int __checkPDDocumentPages(final PDDocument doc) {
    final Image image;
    final ArrayList<PDPage> pages;
    Dimension dims;
    PDPage page;
    Graphics2D graphics;
    PDRectangle cropBox;
    PageDrawer drawer;
    int errors, size;

    errors = 0;

    try {
      size = doc.getNumberOfPages();
      if (size <= 0) {
        return PdfCheckerJob.MAX_PAGE_ERRORS;
      }

      pages = new ArrayList<>(size);
      doc.getDocumentCatalog().getPages().getAllKids(pages);
      if (size != pages.size()) {
        return Math.max(0, Math.min(PdfCheckerJob.MAX_PAGE_ERRORS,
            Math.abs(size - pages.size())));
      }

      image = Image.get();
      for (; (--size) >= 0;) {
        try {
          page = pages.remove(0);
          cropBox = page.findCropBox();
          dims = cropBox.createDimension();
          graphics = image.createGraphic(dims);
          try {
            drawer = new PageDrawer();
            try {
              drawer.drawPage(graphics, page, dims);
            } finally {
              drawer.dispose();
              drawer = null;
            }
          } finally {
            graphics.dispose();
            graphics = null;
          }

        } catch (@SuppressWarnings("unused") final Throwable terror2) {
          if ((++errors) >= PdfCheckerJob.MAX_PAGE_ERRORS) {
            return PdfCheckerJob.MAX_PAGE_ERRORS;
          }
        }
      }
    } catch (@SuppressWarnings("unused") final Throwable error) {
      return PdfCheckerJob.MAX_PAGE_ERRORS;
    }

    return errors;
  }

  /** check the pdf parser */
  private final class __CheckPdfParser implements Callable<Integer> {
    /** create */
    __CheckPdfParser() {
      super();
    }

    /** {@inheritDoc} */
    @Override
    public Integer call() {
      return Integer.valueOf(_PdfBoxCheckerJob.this._checkPDFParser());
    }
  }

  /** check the pdf parser */
  private final class __CheckNonSequentialPdfParser
      implements Callable<Integer> {
    /** create */
    __CheckNonSequentialPdfParser() {
      super();
    }

    /** {@inheritDoc} */
    @Override
    public Integer call() {
      return Integer
          .valueOf(_PdfBoxCheckerJob.this._checkNonSequentialPDFParser());
    }
  }
}
