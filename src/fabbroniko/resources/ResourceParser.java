package fabbroniko.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ResourceParser {

	protected String resName;
	protected final List<String> availableTags;
	
	protected static final String NO_STRING = "";
	protected static final int ARGUMENT_NAME_INDEX = 0;
	protected static final int VALUE_INDEX = 1;
	
	protected ResourceParser(final String resName) {
		this.resName = resName;
		availableTags = new ArrayList<>();
	}
	
	public abstract ResourceParser buildResourceFromFile(final String descFile) throws IOException;
	
	public abstract void addResource();
}
