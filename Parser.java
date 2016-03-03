package test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
/**
 * This class is thread safe.
 */
public class Parser {
	
	private static final int BUFFER_SIZE = 1000;
	private File file;
	
	public Parser(File file) {
		super();
		this.file = file;
	}
	
	/** We should not be having these getter setters. ideally we should be setting that data in the constructor
	 */
//	public synchronized void setFile(File f) {
//		file = f;
//	}
//	public synchronized File getFile() {
//		return file;
//	}
	public String getContent(boolean isUnicode) throws ParserException {
		try (FileReader ipStream = new FileReader(file)){
			StringBuilder builder = new StringBuilder();
			
			CharBuffer buf = CharBuffer.allocate(BUFFER_SIZE);
			while (ipStream.read(buf) > 0) {
				processBuffer(isUnicode, builder, buf);
			}
			return builder.toString();
		}catch(IOException e){
			throw new ParserException("Exception in reading file", e);
		}
	}

	private void processBuffer(boolean isUnicode, StringBuilder builder, CharBuffer buf) {
		while(buf.hasRemaining()){
			char val = buf.get();
			if (isValid(isUnicode, val)) {
				builder.append(val);
			}
		}
	}
	
	private boolean isValid(boolean isUnicode, int data) {
		return !isUnicode || data < 0x80;
	}

	// We do not need this method. Just that its public, so could be access by others, so havent deleted the same.
	public String getContentWithoutUnicode() throws ParserException {
		return getContent(true);
	}


	public void saveContent(String content) throws ParserException {
		try (FileOutputStream o = new FileOutputStream(file)){
			for (int i = 0; i < content.length(); i += 1) {
				o.write(content.charAt(i));
			}
		} catch (IOException e) {
			throw new ParserException("exception in writing", e);
		}
	}

	public static class ParserException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ParserException(String arg0, Throwable arg1) {
			super(arg0, arg1);
		}

	}
}
