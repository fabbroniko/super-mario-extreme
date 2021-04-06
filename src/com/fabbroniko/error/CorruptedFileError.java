package com.fabbroniko.error;

/**
 * Called when a file is corrupted (something went wrong while reading a file).
 * @author com.fabbroniko
 *
 */
public class CorruptedFileError extends RuntimeException {

	private final String fileName;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs the error.
	 * @param fileNameP Path of the corrupted file.
	 */
	public CorruptedFileError(final String fileNameP) {
		super();
		this.fileName = fileNameP;
	}
	
	@Override
	public String toString() {
		return "Something went wrong trying to read " + this.fileName;
	}
}
