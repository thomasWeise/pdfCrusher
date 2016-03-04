package thomasWeise.pdfCrusher.documents;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.optimizationBenchmarking.utils.comparison.Compare;
import org.optimizationBenchmarking.utils.io.paths.PathUtils;

import thomasWeise.pdfCrusher.PdfCrusherJob;

/**
 * A class representing a document file.
 *
 * @author tweise
 */
public class DocumentFile implements Comparable<DocumentFile> {

  /** the owning job */
  private final PdfCrusherJob m_owner;

  /** is this document file valid? */
  private int m_errors;

  /** the file */
  private Path m_file;

  /** the attributes of the file */
  private long m_size;

  /** the unique file key */
  private Object m_key;

  /** the reference counter */
  private int m_refCount;

  /**
   * Instantiate the document file.
   *
   * @param owner
   *          the owning job
   * @param f
   *          the file
   */
  public DocumentFile(final Path f, final PdfCrusherJob owner) {
    super();

    final Logger logger;

    this.m_owner = owner;

    this._defFill();

    logger = this.m_owner.getLogger();

    try {
      this.m_errors = this._init(f, logger);
    } catch (final Throwable t) {
      this.m_errors = Integer.MAX_VALUE;
      if ((logger != null) && logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE,
            ("Error detected when checking file '" + f + //$NON-NLS-1$
                "'."), //$NON-NLS-1$
            t);
      }
    }

    this.m_refCount = 1;
    if ((logger != null) && logger.isLoggable(Level.INFO)) {
      logger.info((this.getClass().getSimpleName() + " created: ") + //$NON-NLS-1$
          this.toString());
    }
  }

  /** fill all fields with default values */
  void _defFill() {
    this.m_errors = Integer.MAX_VALUE;
    this.m_file = null;
    this.m_size = -1L;
    this.m_key = null;
  }

  /**
   * Get the amount of errors in the file
   *
   * @return the amount of errors in the file
   */
  public final int getErrors() {
    return this.m_errors;
  }

  /**
   * initialize the document file object
   *
   * @param f
   *          the file
   * @param logger
   *          the logger
   * @return true if everything went ok and the document file is valid,
   *         false otherwise
   */
  int _init(final Path f, final Logger logger) {
    BasicFileAttributes attributes;

    if (f == null) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.warning("Null file detected??");//$NON-NLS-1$
      }
      return Integer.MAX_VALUE;
    }

    try {
      this.m_file = PathUtils.normalize(f);
    } catch (final Throwable t) {
      if ((logger != null) && logger.isLoggable(Level.SEVERE)) {
        logger.log(Level.SEVERE, //
            ("Error when canonicalizing document file '" + f + //$NON-NLS-1$
                "'."), //$NON-NLS-1$
            t);
      }
      return Integer.MAX_VALUE;
    }

    if (this.m_file == null) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.warning("File '" + f + //$NON-NLS-1$
            "' canonicalizes to null.");//$NON-NLS-1$
      }
      return Integer.MAX_VALUE;
    }

    try {
      attributes = Files.readAttributes(this.m_file,
          BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    } catch (final Throwable error) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.log(Level.WARNING, "File '" + this.m_file + //$NON-NLS-1$
            "' does not exist.", error);//$NON-NLS-1$
      }
      return Integer.MAX_VALUE;
    }

    if (attributes != null) {
      this.m_key = attributes.fileKey();
    }
    if ((attributes == null) || (!(attributes.isRegularFile()))) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.warning("File '" + this.m_file + //$NON-NLS-1$
            "' is not a file .");//$NON-NLS-1$
      }
      return Integer.MAX_VALUE;
    }

    this.m_size = attributes.size();
    attributes = null;

    if (this.m_size <= 0l) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.warning("File '" + this.m_file + //$NON-NLS-1$
            "' is empty."); //$NON-NLS-1$
      }
      return Integer.MAX_VALUE;
    }

    if (this.m_size <= 128l) {
      if ((logger != null) && logger.isLoggable(Level.WARNING)) {
        logger.warning("File '" + this.m_file + //$NON-NLS-1$
            "' is too small with size " //$NON-NLS-1$
            + this.m_size + "."); //$NON-NLS-1$
      }
      return 1;
    }

    return 0;
  }

  /**
   * perform the size comparison
   *
   * @param a
   *          the first file
   * @param b
   *          the second file
   * @return the result
   */
  static final int _sizeCompare(final DocumentFile a,
      final DocumentFile b) {
    final long sa, sb;

    sa = a.m_size;
    sb = b.m_size;

    if (sa <= 0l) {
      if (sb > 0l) {
        return 1;
      }
      return 0;
    }
    if (sb <= 0) {
      return (-1);
    }

    return ((sa < sb) ? (-1) : ((sa > sb) ? 1 : 0));
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(final DocumentFile o) {
    int r;

    if (o == this) {
      return 0;
    }
    if (o == null) {
      return -1;
    }

    r = Integer.compare(this.m_errors, o.m_errors);
    if (r != 0) {
      return r;
    }

    return DocumentFile._sizeCompare(this, o);
  }

  /**
   * Get the document file
   *
   * @return the document file
   */
  public final Path getFile() {
    return this.m_file;
  }

  /**
   * Get the file size
   *
   * @return the file size
   */
  public final long getSize() {
    return this.m_size;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return ((((('"' + this.m_file.toString()) + "\",errors=") + //$NON-NLS-1$
        this.m_errors) + ",size=") + this.m_size);//$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final boolean equals(final Object o) {
    final DocumentFile other;
    final Path b;
    final Logger logger;

    if (o == this) {
      return true;
    }
    if (o instanceof DocumentFile) {
      other = ((DocumentFile) o);
      b = other.m_file;

      if (Compare.equals(this.m_file, b)) {
        return true;
      }
      if ((this.m_key != null)) {
        return Compare.equals(this.m_key, other.m_key);
      }
    }
    if (o instanceof Path) {
      if (Compare.equals(this.m_file, o)) {
        return true;
      }
      if (this.m_key != null) {
        try {
          return Compare
              .equals(this.m_key, //
                  Files.readAttributes(((Path) o),
                      BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS)
                  .fileKey());
        } catch (final Throwable error) {
          logger = this.m_owner.getLogger();
          if ((logger != null) && (logger.isLoggable(Level.WARNING))) {
            logger.log(Level.WARNING, //
                ((("Error when trying to obtain file key of file '" + //$NON-NLS-1$
                    o) + '\'') + '.'),
                error);
          }
        }
      }
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public final int hashCode() {
    return (this.m_file != null) ? this.m_file.hashCode() : 0;
  }

  /** delete this file */
  public void delete() {
    final Logger logger;

    if (this.m_file != null) {
      logger = this.m_owner.getLogger();
      if ((logger != null) && (logger.isLoggable(Level.FINE))) {
        logger.fine("Deleting file '" + this.m_file + //$NON-NLS-1$
            "'.");//$NON-NLS-1$
      }
      try {
        PathUtils.delete(this.m_file);
      } catch (final Throwable t) {
        if ((logger != null) && (logger.isLoggable(Level.SEVERE))) {
          logger.log(Level.SEVERE, //
              ("Error when deleting file '" + this.m_file + //$NON-NLS-1$
                  "'."), //$NON-NLS-1$
              t);
        }
      }
    }
  }

  /** add a reference */
  public final void addRef() {
    synchronized (this) {
      if (this.m_refCount > 0) {
        this.m_refCount++;
        return;
      }
    }
    throw new IllegalStateException(//
        "File has already been released.");//$NON-NLS-1$
  }

  /**
   * Delete this file if possible
   */
  public synchronized void release() {
    if ((--this.m_refCount) <= 0) {
      this.delete();
    }
  }
}
