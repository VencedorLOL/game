package com.mygdx.game.lwjgl3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.audio.Lwjgl3Audio;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.mygdx.game.MainClass;
import com.mygdx.game.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

import static com.mygdx.game.Settings.print;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {

	public static void main(String[] args) {
		if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		Lwjgl3ApplicationConfiguration configg = getDefaultConfiguration();
		return new Lwjgl3Application(new MainClass(), configg)  {
			public Lwjgl3Audio createAudio(Lwjgl3ApplicationConfiguration config) {
				try{
					Field gimmeConfig = Lwjgl3Application.class.getDeclaredField("config");
					gimmeConfig.setAccessible(true);
		/*			Field modifier = getModifiersField();
					modifier.setAccessible(true);
					modifier.setInt(gimmeConfig, gimmeConfig.getModifiers() & ~Modifier.FINAL);*/
					Lwjgl3ApplicationConfiguration configHopefully = (Lwjgl3ApplicationConfiguration) gimmeConfig.get(this);
					print("got config??: " + configHopefully);
					Settings.setConfig(configHopefully);
				}catch (NoSuchFieldException | IllegalAccessException /*|  NoSuchMethodException | InvocationTargetException*/ e){print("couldnt get config"); e.printStackTrace();}
				return super.createAudio(config);
			}
		};
	}

	/*static Field getModifiersField() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
		getDeclaredFields0.setAccessible(true);
		Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
		for (Field each : fields)
			if ("modifiers".equals(each.getName()))
				return each;
		return null;
	}*/


	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("Game2");
		//// Vsync limits the frames per second to what your hardware can display, and helps eliminate
		//// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
		configuration.useVsync(false);
		//// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
		//// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
		// TODO: set this to like 120?
		//  ACTUAL TO_DO: SEPARATE RENDERING AND NOT RENDERING INTO TWO DIFFERENT THREADS.
		configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1 );
		//// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
		//// useful for testing performance, but can also be very stressful to some hardware.
		//// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
		configuration.setWindowedMode(640, 480);
		//// You can change these files; they are in lwjgl3/src/main/resources/ .
		configuration.setWindowIcon("anima.png", "anima2.png", "anima4.png", "anima8.png");
		//ty evan
		String basePath = "";
		Files.FileType baseFileType = null;
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				basePath = "Application Data/." + "vencedor"  + "/";
			} else {
				basePath = "AppData/Roaming/." + "vencedor" + "/";
			}
			baseFileType = Files.FileType.External;
		} else if (SharedLibraryLoader.isMac) {
			basePath = "Library/Application Support/" + "vencedor" + "/";
			baseFileType = Files.FileType.External;
		} else if (SharedLibraryLoader.isLinux) {
			String XDGHome = System.getenv("XDG_DATA_HOME");
			if (XDGHome == null) XDGHome = System.getProperty("user.home") + "/.local/share";

			String titleLinux = "vencedor".toLowerCase(Locale.ROOT).replace(" ", "-");
			basePath = XDGHome + "/." + "vencedor" + "/" + titleLinux + "/";

			baseFileType = Files.FileType.Absolute;
		}

		configuration.setPreferencesConfig( basePath, baseFileType );
		return configuration;
	}
}
