package me.donkeycore.dpl.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.donkeycore.dpl.Donkey;
import me.donkeycore.dpl.io.FileCreator;

public class PluginLoader {
	
	public static final File pluginFolder = new File(FileCreator.rootFolderFile.getAbsolutePath() + "/plugins/");
	{
		if (!pluginFolder.exists())
			pluginFolder.mkdirs();
		else
			loadPlugins();
	}
	
	protected void loadPlugins() {
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith("\\.dplugin");
			}
		};
		URLClassLoader u = null;
		try {
			List<URL> urls = new ArrayList<URL>();
			for(File f : pluginFolder.listFiles(filter))
				urls.add(f.toURI().toURL());
			u = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
			for(File f : pluginFolder.listFiles(filter)) {
				JarFile j = null;
				try {
					j = new JarFile(f);
					Enumeration<JarEntry> en = j.entries();
					while(en.hasMoreElements()) {
						JarEntry e = en.nextElement();
						String n = e.getName();
						if(n.endsWith("\\.class")) {
							String cn = n.replace('/', '.').substring(0, n.length() - 6);
							Class<?> c = Class.forName(cn);
							if(c.getSuperclass().getName().equals(DPlugin.class.getName())) {
								Object o = c.getConstructor().newInstance();
							}
						}
					}
				} catch(IOException e) {
					Donkey.printError(e);
				} finally {
					j.close();
				}
			}
		} catch(Throwable t) {
			Donkey.printError(t);
		} finally {
			try {
				u.close();
			} catch(IOException e) {
				Donkey.printError(e);
			}
		}
	}
}
