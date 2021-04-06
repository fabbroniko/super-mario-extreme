package com.fabbroniko.resources;

import java.util.HashMap;
import java.util.Map;

public class Sound {

	private static final Map<String, Sound> sounds = new HashMap<>();
	
	private static final String NO_SUCH_SOUND = "No such Sound with the specified name.";
	
	private String fileLocation;
	private SoundType soundType;
	private boolean loop;
	
	public static Sound getSoundFromName(final String name) {
		if(!sounds.containsKey(name)){
			throw new IllegalArgumentException(NO_SUCH_SOUND);
		}
		return sounds.get(name);
	}
	
	public static void addNewSound(final String audioName, final String fileLocation, final SoundType soundType, final boolean loop) {
		if(sounds.containsKey(audioName)){
			throw new IllegalArgumentException();
		}
		sounds.put(audioName, new Sound(fileLocation, soundType, loop));
	}

	private Sound(final String fileLocation, final SoundType soundType, final boolean loop) {
		if(soundType == null || getClass().getResource(fileLocation) == null){
			throw new IllegalArgumentException();
		}
		
		this.fileLocation = fileLocation;
		this.soundType = soundType;
		this.loop = loop;
	}
	
	public String getFileLocation(){
		return this.fileLocation;
	}
	
	public SoundType getType() {
		return this.soundType;
	}
	
	public boolean mustLoop() {
		return this.loop;
	}
	
	public static void addSoundFromDescriptionFile(final String resName, final String descFile) {
		new SoundParser(resName).buildResourceFromFile(descFile).addResource();
	}
	
	@Override
	public boolean equals(final Object o){
		if(o == null || !(o instanceof Sound)){
			return false;
		}
		final Sound obj = (Sound)o;
		return obj.fileLocation.equals(this.fileLocation) && obj.soundType.equals(this.soundType) && obj.loop == this.loop;
	}
	
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[File Location: ");
		stringBuilder.append(fileLocation);
		stringBuilder.append(", Type: ");
		stringBuilder.append(soundType);
		stringBuilder.append(", Loop: ");
		stringBuilder.append(loop);
		stringBuilder.append("]");
		
		return stringBuilder.toString();
	}
	
	public enum SoundType{
		MUSIC("Music"),
		EFFECT("Effect");
		
		private String tag;
		
		private SoundType(final String tag) {
			this.tag = tag;
		}
		
		private String getTag() {
			return this.tag;
		}
		
		public static SoundType getTypeFromTag(final String tag) {
			for(final SoundType i:SoundType.values()){
				if(i.getTag().equals(tag)){
					return i;
				}
			}
			return null;
		}
	}
}
