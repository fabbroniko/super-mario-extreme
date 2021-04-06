package com.fabbroniko.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fabbroniko.error.CorruptedFileError;
import com.fabbroniko.resources.Sound.SoundType;

public final class SoundParser extends ResourceParser{
	
	private String soundLocation = NO_STRING;
	private SoundType soundType = null;
	private boolean loop = false;
	
	private static final String ARG_LOCATION = "location";
	private static final String ARG_TYPE = "type";
	private static final String ARG_LOOP = "loop";
	
	private static final String ARGUMENTS_SPLITTER = "=";
	private static final int N_ARGS = 2;
	
	public SoundParser(final String resName) {
		super(resName);
		this.availableTags.add(ARG_LOCATION);
		this.availableTags.add(ARG_TYPE);
		this.availableTags.add(ARG_LOOP);
	}
	
	@Override
	public ResourceParser buildResourceFromFile(final String descFile) {
		final InputStream inputStream = getClass().getResourceAsStream(descFile);
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String tmpString;
		String[] tmpArray;
		
		try {
			while((tmpString = bufferedReader.readLine()) != null) {
				tmpArray = tmpString.split(ARGUMENTS_SPLITTER);
				if(tmpArray != null && tmpArray.length == N_ARGS && availableTags.contains(tmpArray[ARGUMENT_NAME_INDEX])){
					if(tmpArray[ARGUMENT_NAME_INDEX].equals(ARG_LOCATION)){
						this.soundLocation = tmpArray[VALUE_INDEX];
					}else if(tmpArray[ARGUMENT_NAME_INDEX].equals(ARG_TYPE)){
						this.soundType = Sound.SoundType.getTypeFromTag(tmpArray[VALUE_INDEX]);
					}else if(tmpArray[ARGUMENT_NAME_INDEX].equals(ARG_LOOP)){
						this.loop = tmpArray[VALUE_INDEX].equals("true") ? true : false;
					}
				}else{
					throw new CorruptedFileError(descFile);
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			throw new CorruptedFileError(descFile);
		}
		
		return this;
	}
	 
	@Override
	public void addResource() {
		if(soundLocation.equals(NO_STRING) || soundType == null){
			throw new CorruptedFileError(resName);
		}
		Sound.addNewSound(resName, soundLocation, soundType, loop);
	}
}
