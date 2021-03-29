package fabbroniko.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ErrorManager {

	private final List<Pair<Integer, String>> errors;
	
	private static final ErrorManager MY_INSTANCE = new ErrorManager();
	
	public static final int ERROR_STOPPING_MUSIC = 0x00000001;
	public static final int ERROR_STARTING_MUSIC = 0x00000002;
	public static final int ERROR_INITIALIZING_MUSIC = 0x00000003;
	public static final int ERROR_THREAD_SLEEP = 0x00000004;
	public static final int ERROR_LOADING_IMAGE = 0x00000005;
	
	private ErrorManager() {
		errors = new ArrayList<>();
	}
	
	public static ErrorManager getInstance() {
		return MY_INSTANCE;
	}
	
	public void notifyError(final int errorID, final String details){
		errors.add(new Pair<>(errorID, details));
	}
	
	public List<Pair<Integer, String>> getErrors(){
		return this.errors;
	}
	
	public final class Pair<X, Y> {
		
		private X x;
		private Y y;
		
		private Pair(final X x, final Y y) {
			this.x = Objects.requireNonNull(x);
			this.y = Objects.requireNonNull(y);
		}
		
		public X getX() {
			return this.x;
		}
		
		public Y getY() {
			return this.y;
		}
	}
}
