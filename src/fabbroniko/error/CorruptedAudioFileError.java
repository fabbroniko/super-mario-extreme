package fabbroniko.error;

/**
 * Exception thrown when an audio file is corrupted.
 * @author fabbroniko
 *
 */
public class CorruptedAudioFileError extends Exception {
	
	private final String file;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new error.
	 * @param fileP Path of the corrupted audio file.
	 */
	public CorruptedAudioFileError(final String fileP) {
		super();
		this.file = fileP;
	}
	
	@Override
	public String toString() {
		return "Something went wrong trying to play " + file + ".";
	}
}
