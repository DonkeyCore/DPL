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

/**
 * This singleton class loads all {@link DPlugin} classes found in JARs.<br>
 * Default plugin location folder: <code>donkey/plugins/</code>
 * 
 * @see DPlugin
 * @since 1.0
 */
public class PluginLoader {
	
	/**
	 * Private constructor to make a singleton class
	 * 
	 * @see PluginLoader
	 * @since 1.0
	 */
	private PluginLoader() {}
	
	/**
	 * Whether to accept more plugin loaders
	 * 
	 * @see PluginLoader
	 * @since 1.0
	 */
	private boolean accept = true;
	
	/**
	 * The default plugin loader
	 * 
	 * @since 1.0
	 */
	private static PluginLoader defloader = null;
	/**
	 * The current plugin loader
	 * 
	 * @since 1.0
	 */
	private static PluginLoader loader = null;
	/**
	 * The plugin folder in which the default plugin loader looks for JARs
	 * 
	 * @since 1.0
	 */
	private static final File pluginFolder = new File(FileCreator.rootFolderFile.getAbsolutePath() + "/plugins/");
	{
		if (!pluginFolder.exists())
			pluginFolder.mkdirs();
		else
			loadPlugins();
	}
	
	/**
	 * Called by {@link Donkey} to stop accepting new plugin loaders
	 * 
	 * @return The current plugin loader instance
	 */
	public static final PluginLoader stopAccepting() {
		getDefaultPluginLoader().accept = false;
		loader.accept = false;
		return loader;
	}
	
	/**
	 * Retrieve the default {@link PluginLoader} class. It always points to this specific class.
	 * 
	 * @return The default plugin loader
	 * @since 1.0
	 */
	public static final PluginLoader getDefaultPluginLoader() {
		if (defloader == null)
			defloader = new PluginLoader();
		if (loader == null)
			loader = defloader;
		return defloader;
	}
	
	/**
	 * Retrieve the current {@link PluginLoader} class. It is changeable by plugins.
	 * 
	 * @return The current plugin loader
	 * @since 1.0
	 */
	public static final PluginLoader getPluginLoader() {
		if(loader == null)
			getDefaultPluginLoader();
		return loader;
	}
	
	/**
	 * Set the current {@link PluginLoader} class.
	 * 
	 * @param l The new {@link PluginLoader} to replace the old one
	 * @return The old plugin loader
	 * @since 1.0
	 */
	//This method is never actually called by Donkey. Use it when making a wrapper if you need it.
	public static final PluginLoader setPluginLoader(PluginLoader l) {
		PluginLoader old = getPluginLoader();
		if(!old.accept)
			throw new IllegalStateException("Cannot change PluginLoader after startup!");
		loader = l;
		return old;
	}
	
	/**
	 * An {@link ArrayList} of registered {@link DPlugin} objects
	 * 
	 * @since 1.0
	 */
	private static final List<DPlugin> plugins = new ArrayList<DPlugin>();
	
	/**
	 * Return the list of enabled plugins.
	 * 
	 * @return An array of {@link DPlugin} objects
	 * @since 1.0
	 */
	public DPlugin[] getPlugins() {
		return plugins.toArray(new DPlugin[plugins.size()]);
	}
	
	/**
	 * Loads all {@link DPlugin} classes found in JARs.<br>
	 * Default plugin location folder: <code>donkey/plugins/</code>
	 * 
	 * @since 1.0
	 */
	protected void loadPlugins() {
		FilenameFilter filter = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".dplugin");
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
						if (n.endsWith(".class") && !n.contains("$")) {
							String cn = n.replace('/', '.').substring(0, n.length() - 6);
							Class<?> c = Class.forName(cn, false, u);
							if (c.getSuperclass().getName().equals(DPlugin.class.getName())) {
								Object o = c.getConstructor().newInstance();
								if(o instanceof DPlugin)
									((DPlugin) o).onLoad();
								plugins.add((DPlugin) o);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accept ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PluginLoader other = (PluginLoader) obj;
		if (accept != other.accept)
			return false;
		return true;
	}
}
