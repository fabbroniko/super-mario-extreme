package com.fabbroniko.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fabbroniko.error.CorruptedFileError;
import com.fabbroniko.error.ResourceNotFoundError;

public class ResourceManager {

	private Map<String, String> resources = new HashMap<>();
	private Map<String, String> resourcesType = new HashMap<>();
	private ResourceIndexParser resourceIndexParser = new ResourceIndexParser();
	
	private String currentTag = NO_TAG;
	
	private static final ResourceManager MY_INSTANCE = new ResourceManager();
	private static final String RESOURCES_INDEX = "/resources.index";
	private static final String RESOURCE_SPLITTER = "=";
	private static final int NAME_INDEX = 0;
	private static final int LOCATION_INDEX = 1;
	private static final String NO_TAG = "";

	private ResourceManager() {
		try {
			loadResources();
			checkLocations();
			//System.out.println(resources);
			//System.out.println(resourcesType);
		} catch (IOException e) {
			throw new ResourceNotFoundError(RESOURCES_INDEX);
		}
	}
	
	public static ResourceManager getInstance() {
		return MY_INSTANCE;
	}
	
	private void loadResources() throws IOException {
		final InputStream inputStream = getClass().getResourceAsStream(RESOURCES_INDEX);
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String tmpString;
		String[] tmpArray;
		 
		while((tmpString = bufferedReader.readLine()) != null) {
			tmpArray = tmpString.split(RESOURCE_SPLITTER);
			if(tmpArray != null && tmpArray.length == 2){
				if(currentTag.equals(NO_TAG) || resources.containsKey(tmpArray[NAME_INDEX])){
					throw new CorruptedFileError(RESOURCES_INDEX);
				}
				resources.put(tmpArray[NAME_INDEX], tmpArray[LOCATION_INDEX]);
				resourcesType.put(tmpArray[NAME_INDEX], currentTag);
			}else if(resourceIndexParser.isOpeningTag(tmpString)){
				if(!currentTag.equals(NO_TAG) || !ResourceService.getInstance().getAvailableTags().contains(resourceIndexParser.getTagName(tmpString))){
					throw new CorruptedFileError(RESOURCES_INDEX);
				}
				currentTag = resourceIndexParser.getTagName(tmpString);
			}else if(resourceIndexParser.isClosingTag(tmpString)) {
				if(currentTag.equals(NO_TAG) || !currentTag.equals(resourceIndexParser.getTagName(tmpString))){
					throw new CorruptedFileError(RESOURCES_INDEX);
				}
				currentTag = NO_TAG;
			}
		}
		
		bufferedReader.close();
	}
	
	private void checkLocations() {
		for(final String i:resources.values()){
			if(i == null || getClass().getResource(i) == null) {
				throw new ResourceNotFoundError(i);
			}
		}
	}
	
	public Map<String, String> getResourcesFromTag(final String tag) {
		final Map<String, String> res = new HashMap<>();
		
		for(final String i:resourcesType.keySet()){
			if(resourcesType.get(i).equals(tag)){
				res.put(i, resources.get(i));
			}
		}
		
		return res;
	}
	
	private class ResourceIndexParser {
		
		private static final char OPENING_TAG = '<';
		private static final char CLOSING_TAG = '>';
		private static final char ENDING_TAG = '/';
		private static final int MIN_LENGTH = 3;
		private static final int FIRST_POSITION = 0;
		private static final int ENDING_TAG_POSITION = 1;
		
		public ResourceIndexParser() {}
		
		public boolean isOpeningTag(final String tag){
			return isValidTag(tag) && tag.charAt(ENDING_TAG_POSITION) != ENDING_TAG;
		}
		
		public boolean isClosingTag(final String tag) {
			return isValidTag(tag) && tag.charAt(ENDING_TAG_POSITION) == ENDING_TAG;
		}
		
		private boolean isValidTag(final String tag) {
			return tag != null && tag.length() > MIN_LENGTH && tag.charAt(FIRST_POSITION) == OPENING_TAG && tag.charAt(tag.length() - 1) == CLOSING_TAG;
		}
		
		public String getTagName(final String tag) {
			if(!isValidTag(tag)){
				return null;
			}
			
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(tag);
			stringBuilder.deleteCharAt(FIRST_POSITION);
			if(stringBuilder.charAt(FIRST_POSITION) == ENDING_TAG){
				stringBuilder.deleteCharAt(FIRST_POSITION);
			}
			stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
			return stringBuilder.toString();
		}
	}
	
}
