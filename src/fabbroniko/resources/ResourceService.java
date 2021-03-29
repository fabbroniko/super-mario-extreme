package fabbroniko.resources;

import java.util.ArrayList;
import java.util.List;

public final class ResourceService {

	private static final ResourceService MY_INSTANCE = new ResourceService();
	private final List<String> availableTags = new ArrayList<>();
	
	public static final String AUDIO_TAG = "Audio";
	public static final String CICCIO_TAG = "Ciccio";
	
	private ResourceService() {
		this.availableTags.add(AUDIO_TAG);
		this.availableTags.add(CICCIO_TAG);
	}
	
	public static ResourceService getInstance() {
		return MY_INSTANCE;
	}
	
	public List<String> getAvailableTags() {
		return new ArrayList<>(availableTags);
	}
}
