package core;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Represents core functionality used by all projects
 */
public class Core {

	public static void main(String[] args) {

		List<Class<Plugin>> pluginClasses = new ArrayList<>();

		for ( File indexFile : getIndexFiles() ) {
			pluginClasses.addAll(getPlugins(indexFile));
		}
		
		for ( Class<Plugin> pluginClass : pluginClasses ) {
			try {
				pluginClass.newInstance().speak();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public static List<File> getIndexFiles() {
		List<File> indexFiles = new ArrayList<>();
		
		try {
			Enumeration<URL> en = Core.class.getClassLoader().getResources("kunde");

			if (en.hasMoreElements()) {
				File kundeRootDir = new File(en.nextElement().toURI());
				for (File kundeDirectory : kundeRootDir.listFiles()) {
					indexFiles.add(new File(kundeDirectory, "plugins.properties"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return indexFiles;
	}

	@SuppressWarnings("unchecked")
	public static List<Class<Plugin>> getPlugins(File indexFile) {
		List<Class<Plugin>> plugins = new ArrayList<>();

		InputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(indexFile);
			br = new BufferedReader(new InputStreamReader(fis));

			String className;
			while ((className = br.readLine()) != null) {
				plugins.add((Class<Plugin>) Class.forName(className));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( br != null ) close(br);
			if ( fis != null ) close(fis);
		}

		return plugins;
	}
	
	private static void close(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
