package test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
/**
 * This class is thread safe.
 */
public class Parser {
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private File file;
	
	public Parser(File file) {
		super();
		this.file = file;
	}
	
	/**
	 * Get the contents of file with UTF-8 encoding
	 * @return
	 * @throws ParserException
	 */
	public String getContent() throws ParserException {
		return getContent(DEFAULT_ENCODING);
	}
	
	/**
	 * read the contents of a file with given encoding
	 * @param encoding
	 * @return
	 * @throws ParserException
	 */
	public String getContent(String encoding) throws ParserException {
		if(null == encoding || encoding.isEmpty()){
			throw new ParserException("Exception in reading file, invalid encoding" + encoding);
		}
		try {
			synchronized(file){
				return new String(Files.readAllBytes(file.toPath()), encoding);
			}
		} catch (IOException e) {
			throw new ParserException("Exception in reading file", e);
		}
	}
	
	/**
	 * replace the unicode characters in a file with EMPTY_STRING
	 * @param encoding
	 * @throws ParserException
	 */
	public void replaceUnicodeCharsInFile(String encoding) throws ParserException{
		String data = getContentWithoutUnicode(encoding);
		saveContent(data);
	}
	

	//we do not need to expose this method. As we do have a functionality where we are cleaning up a file.
	protected String getContentWithoutUnicode(String encoding) throws ParserException {
		String content =  getContent(encoding);
		return content.replace("\\u","");
	}


	/**
	 * Write given string to the file.
	 * @param content
	 * @throws ParserException
	 */
	//We may not need to have this method public.
	public void saveContent(String content) throws ParserException {
		synchronized(file){
			try (Writer o = new FileWriter(file)){
				o.write(content);
			} catch (IOException e) {
				throw new ParserException("exception in writing", e);
			}
		}
	}

	public static class ParserException extends Exception{

		private static final long serialVersionUID = 1L;

		public ParserException(String arg0, Throwable arg1) {
			super(arg0, arg1);
		}

		public ParserException(String string) {
			super(string);
		}

	}
}
