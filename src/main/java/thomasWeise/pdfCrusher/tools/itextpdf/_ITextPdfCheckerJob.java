package thomasWeise.pdfCrusher.tools.itextpdf;

import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.pdf.PdfReader;

import thomasWeise.pdfCrusher.tools.PdfCheckerJob;
import thomasWeise.pdfCrusher.tools.PdfCheckerJobBuilder;

/** The ITextPdf-based pdf checker job */
final class _ITextPdfCheckerJob extends PdfCheckerJob {

  /**
   * the maximum error returned by {@link #check()}
   */
  private static final int MAX_RESULT = ((2
      * PdfCheckerJob.MAX_PAGE_ERRORS) + 4);

  /**
   * create the job.
   *
   * @param builder
   *          the builder
   */
  _ITextPdfCheckerJob(final PdfCheckerJobBuilder builder) {
    super(builder);
  }

  /** {@inheritDoc} */
  @Override
  protected int check() throws Throwable {
    final Logger logger;
    PdfReader reader;
    int size, i;
    long errors, pageErrors1, pageErrors2;

    pageErrors1 = pageErrors2 = errors = 0;

    try {
      try {
        reader = new PdfReader(Files.readAllBytes(this.getSourceFile()));
        try {
          size = reader.getNumberOfPages();

          for (i = size; i >= 1; i--) {
            try {
              reader.getPageContent(i);
            } catch (@SuppressWarnings("unused") final Throwable terror2) {
              if (pageErrors1 < PdfCheckerJob.MAX_PAGE_ERRORS) {
                ++pageErrors1;
              }
            }
            try {
              reader.getPageN(i).isPage();
            } catch (@SuppressWarnings("unused") final Throwable terror2) {
              if (pageErrors2 < PdfCheckerJob.MAX_PAGE_ERRORS) {
                ++pageErrors2;
              }
            }
          }

          errors += pageErrors1;
          errors += pageErrors2;

          try {
            reader.getAcroFields();
          } catch (@SuppressWarnings("unused") final Throwable terror) {
            ++errors;
          }

          try {
            reader.getCatalog();
          } catch (@SuppressWarnings("unused") final Throwable terror) {
            ++errors;
          }

          try {
            reader.getInfo();
          } catch (@SuppressWarnings("unused") final Throwable terror) {
            ++errors;
          }
        } finally {
          reader.close();
        }
      } catch (@SuppressWarnings("unused") final Throwable terror) {
        ++errors;
      }

    } catch (@SuppressWarnings("unused") final Throwable error) {
      errors = _ITextPdfCheckerJob.MAX_RESULT;
    }

    if (errors > 0) {
      logger = this.getLogger();
      if ((logger != null) && (logger.isLoggable(Level.INFO))) {
        logger.info(((((ITextPdfChecker.getInstance().toString() + //
            " discovered " + errors) + //$NON-NLS-1$
            " errors in document '") + this.getSourceFile()) + '\'') //$NON-NLS-1$
            + '.');
      }
    }

    return Math.max(0,
        ((int) (Math.min(_ITextPdfCheckerJob.MAX_RESULT, errors))));
  }

  /** {@inheritDoc} */
  @Override
  protected final int getMaxErrors() {
    return _ITextPdfCheckerJob.MAX_RESULT;
  }
}
