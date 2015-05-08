package me.donkeycore.dpl.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.io.FileCreator.FileConfiguration.FileRunConfiguration;
import me.donkeycore.dpl.statement.Statement;

/**
 * Allows you to create files, batch files, bash files, and Donkey files and manage them easily.
 * 
 * @since 1.0
 * @see FileCreator#getFile(File)
 * @see FileCreator#getFile(String)
 * @see FileCreator#getFile(String, String)
 * @see FileCreator#getBatchFile(String)
 * @see FileCreator#getBashScript(String)
 * @see FileCreator#getDonkeyClass(String)
 * @see FileCreator#createFile(String)
 * @see FileCreator#createNewFolder(String)
 * @see FileCreator#getFile(File)
 * @see FileConfiguration
 * @see FileRunConfiguration
 * @see FileBatchConfiguration
 * @see FileBashConfiguration
 * @see FileDonkeyConfiguration
 */
public class FileCreator {
	
	/**
	 * Simply loads the files and folders and is automatically called by {@link Donkey}. There is not a real need to use this.
	 * 
	 * @since 1.0
	 * @see FileCreator
	 * @see Donkey
	 */
	public static final void loadFilesAndFolders() {
		rootFolderFile.mkdirs();
		new File(rootFolder + "batch").mkdirs();
		new File(rootFolder + "bash").mkdirs();
		new File(rootFolder + "scripts").mkdirs();
		files.addAll(getFilesInDirectory("batch", "cmd", "bat"));
		files.addAll(getFilesInDirectory("bash", "sh", "command"));
		files.addAll(getFilesInDirectory("scripts", "donkey", "dpl"));
	}
	
	public static java.util.List<File> getFilesInDirectory(String subdirectory, String... ext) {
		String f;
		File folder = new File(rootFolder + subdirectory);
		File[] listOfFiles = folder.listFiles();
		java.util.List<File> fileList = new java.util.ArrayList<File>();
		java.util.List<String> extensions = java.util.Arrays.asList(ext);
		for(int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				f = listOfFiles[i].getName();
				String extension = "";
				for(String s : f.split("\\."))
					extension = s;
				if (extensions.size() > 0 && extensions.contains(extension))
					fileList.add(listOfFiles[i]);
			}
		}
		return fileList;
	}
	
	/**
	 * The list of files found in the bash, batch, and script directories
	 * 
	 * @since 1.0
	 */
	private static List<File> files = new ArrayList<File>();
	/**
	 * The root <code>donkey/</code> folder
	 * 
	 * @since 1.0
	 */
	private static final String rootFolder = "donkey/";
	/**
	 * The root folder of all subfolders and files
	 * 
	 * @since 1.0
	 */
	public static final File rootFolderFile = new File(rootFolder);
	
	/**
	 * Retrieve or create a file based on a {@link File} instance
	 * 
	 * @param file The {@link File} instance
	 * @return A {@link FileConfiguration} instance of the file to customize it
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 */
	public static FileConfiguration getFile(File file) {
		String ext = "";
		for(String s : file.getName().split("\\."))
			ext = s;
		String name = file.getName().replaceAll("\\." + ext + "$", "");
		switch(ext.toLowerCase()) {
			default:
				break;
			case "cmd":
			case "bat":
				return getBatchFile(name);
			case "sh":
				return getBashScript(name);
			case "donkey":
			case "dpl":
				return getDonkeyClass(name);
		}
		if (files.contains(file))
			return new FileConfiguration(file);
		File f = createFile(file.getName());
		return new FileConfiguration(f);
	}
	
	/**
	 * Retrieve or create a file based on the file's name and extension
	 * 
	 * @param file The name of the file
	 * @param ext The extension of the file
	 * @return A {@link FileConfiguration} instance of the file to customize it
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 */
	public static FileConfiguration getFile(String file, String ext) {
		switch(ext.toLowerCase()) {
			default:
				break;
			case "cmd":
			case "bat":
				return getBatchFile(file);
			case "command":
			case "sh":
				return getBashScript(file);
			case "donkey":
			case "dpl":
				return getDonkeyClass(file);
		}
		for(File f : files) {
			if (f.getName().equalsIgnoreCase(file + "." + ext))
				return new FileConfiguration(f);
		}
		File f = createFile(file + "." + ext);
		return new FileConfiguration(f);
	}
	
	/**
	 * Retrieve or create a batch file based on the file's name
	 * 
	 * @param file The name of the file, without the extension
	 * @return A {@link FileBatchConfiguration} instance of the file to customize it
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileBatchConfiguration
	 */
	public static FileBatchConfiguration getBatchFile(String file) {
		for(File f : files) {
			if (f.getName().equalsIgnoreCase(file + ".bat") || f.getName().equalsIgnoreCase(file + ".cmd"))
				return new FileBatchConfiguration(f);
		}
		File f = createFile(file + ".bat");
		return new FileBatchConfiguration(f);
	}
	
	/**
	 * Retrieve or create a bash script based on the file's name
	 * 
	 * @param file The name of the file, without the extension
	 * @return A {@link FileShellConfiguration} instance of the file to customize it
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileBashConfiguration
	 */
	public static FileBashConfiguration getBashScript(String file) {
		for(File f : files) {
			if (f.getName().equalsIgnoreCase(file + ".sh") || f.getName().equalsIgnoreCase(file + ".command"))
				return new FileBashConfiguration(f);
		}
		File f = createFile(file + ".sh");
		return new FileBashConfiguration(f);
	}
	
	/**
	 * Retrieve or create a Donkey class script based on the file's name
	 * 
	 * @param file The name of the file, without the extension
	 * @return A {@link FileDonkeyConfiguration} instance of the file to customize it
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileDonkeyConfiguration
	 */
	public static FileDonkeyConfiguration getDonkeyClass(String file) {
		for(File f : files) {
			if (f.getName().equalsIgnoreCase(file + ".donkey") || f.getName().equalsIgnoreCase(file + ".dpl"))
				return new FileDonkeyConfiguration(f);
		}
		File f = createFile(file + ".dpl");
		return new FileDonkeyConfiguration(f);
	}
	
	/**
	 * Create a file based on its name. This method is automatically called by all the methods to retrieve files when creating a file. <br/>
	 * <br />
	 * <b>
	 * Donkey scripts will be placed in the <code>/scripts/</code> folder <br />
	 * Batch scripts will be placed in the <code>/batch/</code> folder <br />
	 * Bash scripts will be placed in the <code>/bash/</code> folder <br />
	 * <i>Any other files will be placed in the root folder</i>
	 * </b>
	 * 
	 * @param name The name of the file, with the extension
	 * @return A file created and organized correctly
	 * @since 1.0
	 * @see FileCreator
	 */
	public static File createFile(String name) {
		int mode = 0;
		try {
			switch(name.split("\\.")[1].toLowerCase()) {
				case "donkey":
				case "dpl":
					mode = 1;
					break;
				case "cmd":
				case "bat":
					mode = 2;
					break;
				case "command":
				case "sh":
					mode = 3;
					break;
			}
		} catch(Exception e) {}
		String folder = FileCreator.rootFolder;
		if (mode == 1)
			folder = folder + "scripts/";
		else if (mode == 2)
			folder = folder + "batch/";
		else if (mode == 3)
			folder = folder + "bash/";
		File fold = new File(folder);
		if (!fold.exists())
			fold.mkdirs();
		File f = new File(folder + name);
		try {
			if (!f.exists()) {
				f.createNewFile();
				files.add(f);
			}
		} catch(IOException e) {}
		return f;
	}
	
	/**
	 * Creates a folder based on its path.
	 * 
	 * @param path The path of the folder, including the folder's name and extension. The path starts in the root folder.
	 * @return Whether a folder was created or not
	 * @since 1.0
	 * @see FileCreator
	 */
	public static boolean createNewFolder(String path) {
		return createNewFolder(new File(rootFolder + path));
	}
	
	/**
	 * Creates a folder based on its file.
	 * 
	 * @param f The file representing the folder to create.
	 * @return Whether a folder was created or not
	 * @since 1.0
	 * @see FileCreator
	 */
	public static boolean createNewFolder(File f) {
		return f.mkdirs();
	}
	
	/**
	 * Configures files and easily allows adding/clearing/running code <i>before</i> the code runs. {@link FileRunConfiguration} is used to edit the code <i>after</i> it runs.
	 * 
	 * @since 1.0
	 * @see FileCreator
	 * @see FileRunConfiguration
	 * @see FileConfiguration#FileConfiguration(File)
	 */
	public static class FileConfiguration {
		
		/**
		 * The file being manipulated
		 * 
		 * @since 1.0
		 */
		private final File f;
		
		/**
		 * Creates a {@link FileConfiguration} to configure a file
		 * 
		 * @param f The {@link File} to configure
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public FileConfiguration(File f) {
			this.f = f;
		}
		
		/**
		 * Retrieves the {@link File} being configured
		 * 
		 * @return The {@link File} that is being used
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public File getFile() {
			return this.f;
		}
		
		/**
		 * Clears all the code from the file.
		 * 
		 * @return The FileConfiguration instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public FileConfiguration withNoCode() {
			try {
				BufferedWriter w = new BufferedWriter(new FileWriter(f));
				w.write("");
				w.close();
			} catch(IOException e) {}
			return this;
		}
		
		/**
		 * Write code to the file with a new line automatically generated.
		 * 
		 * @param code The code to write
		 * @return The FileConfiguration instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public FileConfiguration withCode(String code) {
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f.getAbsoluteFile(), true)));
				out.append(code + "\n");
				out.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Rewrites everything within the file, excluding the last line.
		 * 
		 * @return The FileConfiguration instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public FileConfiguration withoutLastLine() {
			try {
				int lines = Statement.getMaxLine(getFile());
				String[] data = new String[lines];
				LineNumberReader lr = new LineNumberReader(new BufferedReader(new FileReader(getFile())));
				String s;
				int l = 1;
				while((s = lr.readLine()) != null) {
					data[l - 1] = s;
					l++;
				}
				lr.close();
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f.getAbsoluteFile(), true)));
				out.write("");
				for(int i = 0; i < data.length; i++) {
					if (i < lines - 1)
						out.append(data[i]);
				}
				out.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Run the code using {@link ProcessBuilder}
		 * 
		 * @return A {@link FileRunConfiguration} instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 */
		public FileRunConfiguration run() {
			try {
				java.util.ArrayList<String> args = new java.util.ArrayList<String>();
				args.add(f.getPath());
				ProcessBuilder pb = new ProcessBuilder(args);
				pb.redirectOutput(Redirect.INHERIT);
				pb.redirectError(Redirect.INHERIT);
				pb.redirectInput(Redirect.INHERIT);
				Process p = pb.start();
				p.waitFor();
				//Runtime.getRuntime().exec(f.getPath());
			} catch(Exception e) {
				e.printStackTrace();
			}
			return new FileRunConfiguration(this);
		}
		
		/**
		 * A configuration instance used <i>after</i> a file runs. {@link FileConfiguration} is used to edit code <i>before</i> the file runs.
		 * 
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileRunConfiguration#FileRunConfiguration(FileConfiguration)
		 */
		public final class FileRunConfiguration {
			
			/**
			 * The {@link FileConfiguration} attached to this instance
			 * 
			 * @since 1.0
			 */
			private final FileConfiguration fc;
			
			/**
			 * Create a {@link FileRunConfiguration} based on a {@link FileConfiguration} instance after its code has run.
			 * 
			 * @param fc The {@link FileConfiguration} instance
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileRunConfiguration
			 */
			public FileRunConfiguration(FileConfiguration fc) {
				this.fc = fc;
			}
			
			/**
			 * Retrieves the {@link File} being configured.
			 * 
			 * @return The {@link File} that is being used
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileRunConfiguration
			 */
			public File getFile() {
				return this.fc.getFile();
			}
			
			/**
			 * Deletes the file if it exists. Disables the use of <code>{@link FileRunConfiguration#thenClearCode() thenClearCode()}</code> and <code>{@link FileRunConfiguration#thenRunAgain() thenRunAgain()}</code>
			 * 
			 * @return The {@link FileRunConfiguration} instance
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileRunConfiguration
			 * @see FileRunConfiguration#thenClearCode()
			 * @see FileRunConfiguration#thenRunAgain()
			 */
			public FileRunConfiguration thenDeleteFile() {
				if (getFile().exists())
					getFile().delete();
				return this;
			}
			
			/**
			 * Creates the file if it doesn't exist. Allows the use of <code>{@link FileRunConfiguration#thenClearCode() thenClearCode()}</code> and <code>{@link FileRunConfiguration#thenRunAgain() thenRunAgain()}</code> if the file was deleted.
			 * 
			 * @return The {@link FileConfiguration} instance to edit the code, or <code>null</code> if the {@link File} already exists.
			 * @throws IOException If an I/O error occurred
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileRunConfiguration
			 * @see FileRunConfiguration#thenClearCode()
			 * @see FileRunConfiguration#thenRunAgain()
			 */
			public FileConfiguration thenCreateNewFile() throws IOException {
				if (!getFile().exists()) {
					getFile().createNewFile();
					return this.fc;
				}
				return null;
			}
			
			/**
			 * Clears the code of the file. Disabled if the file was deleted.
			 * 
			 * @return The {@link FileRunConfiguration} instance
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileRunConfiguration
			 */
			public FileRunConfiguration thenClearCode() {
				if (!getFile().exists())
					return this;
				try {
					BufferedWriter w = new BufferedWriter(new FileWriter(f));
					w.write("");
					w.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
				return this;
			}
			
			/**
			 * Runs the code's file again using the {@link FileConfiguration#run()} method.
			 * 
			 * @return The {@link FileRunConfiguration} instance
			 * @since 1.0
			 * @see FileCreator
			 * @see FileConfiguration
			 * @see FileConfiguration#run()
			 * @see FileRunConfiguration
			 */
			public FileRunConfiguration thenRunAgain() {
				if (!getFile().exists())
					return this;
				return this.fc.run();
			}
		}
	}
	
	/**
	 * A more specific version of {@link FileConfiguration} that is based on Batch files.
	 * 
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileBatchConfiguration#FileBatchConfiguration(File)
	 */
	public static final class FileBatchConfiguration extends FileConfiguration {
		
		/**
		 * Creates a {@link FileBatchConfiguration} to configure a Batch file
		 * 
		 * @param f The {@link File} to configure
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileBatchConfiguration
		 */
		public FileBatchConfiguration(File f) {
			super(f);
		}
		
		/**
		 * Easily print words on the screen. Equivalent to <code>withCode("echo " + words);</code>
		 * 
		 * @param words The words to print out on the screen
		 * @return The {@link FileBatchConfiguration} instance
		 */
		public FileBatchConfiguration echo(String words) {
			withCode("echo " + words);
			return this;
		}
		
		public FileBatchConfiguration withNoCode() {
			super.withNoCode();
			return this;
		}
		
		public FileBatchConfiguration withCode(String code) {
			if (code.equalsIgnoreCase("pause")) {
				super.withCode("echo Press Enter to continue . . . ");
				super.withCode("pause > nul");
				return this;
			}
			super.withCode(code);
			return this;
		}
	}
	
	/**
	 * A more specific version of {@link FileConfiguration} that is based on Bash files.
	 * 
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileBashConfiguration#FileBashConfiguration(File)
	 */
	public static final class FileBashConfiguration extends FileConfiguration {
		
		/**
		 * Creates a {@link FileBashConfiguration} to configure a Bash file
		 * 
		 * @param f The {@link File} to configure
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileBashConfiguration
		 */
		public FileBashConfiguration(File f) {
			super(f);
			if (f.length() < 1)
				withCode("#!/bin/bash");
		}
		
		/**
		 * Easily print words on the screen. Equivalent to <code>withCode("echo " + words);</code>
		 * 
		 * @param words The words to print out on the screen
		 * @return The {@link FileBashConfiguration} instance
		 */
		public FileBashConfiguration echo(String words) {
			withCode("echo " + words);
			return this;
		}
		
		public FileBashConfiguration withNoCode() {
			super.withNoCode();
			withCode("#!/bin/bash");
			return this;
		}
		
		public FileBashConfiguration withCode(String code) {
			super.withCode(code);
			return this;
		}
	}
	
	/**
	 * A more specific version of {@link FileConfiguration} that is based on Donkey class files.
	 * 
	 * @since 1.0
	 * @see FileCreator
	 * @see FileConfiguration
	 * @see FileDonkeyConfiguration#FileDonkeyConfiguration(File)
	 */
	public static final class FileDonkeyConfiguration extends FileConfiguration {
		
		/**
		 * Creates a {@link FileDonkeyConfiguration} to configure a Donkey class script
		 * 
		 * @param f The {@link File} to configure
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileDonkeyConfiguration
		 */
		public FileDonkeyConfiguration(File f) {
			super(f);
		}
		
		/**
		 * Easily print words on the screen. Equivalent to <code>withCode("println " + words);</code>
		 * 
		 * @param words The words to print out on the screen
		 * @return The {@link FileDonkeyConfiguration} instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileDonkeyConfiguration
		 */
		public FileDonkeyConfiguration println(String words) {
			withCode("println " + words);
			return this;
		}
		
		public FileDonkeyConfiguration withNoCode() {
			super.withNoCode();
			return this;
		}
		
		public FileDonkeyConfiguration withCode(String code) {
			super.withCode(code);
			return this;
		}
		
		/**
		 * Run the code by creating a new {@link Donkey} instance with the file and running the code.
		 * 
		 * @return A {@link FileRunConfiguration} instance
		 * @since 1.0
		 * @see FileCreator
		 * @see FileConfiguration
		 * @see FileDonkeyConfiguration
		 */
		public FileRunConfiguration run() {
			try {
				new Donkey(getFile()).runCode();
			} catch(Throwable e) {
				Donkey.printError(e);
			}
			return new FileRunConfiguration(this);
		}
	}
}