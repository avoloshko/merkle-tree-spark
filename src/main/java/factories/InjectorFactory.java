package factories;

import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.ConfigurationModule;

public class InjectorFactory {
  public static Injector call() {
    return Guice.createInjector(
      new ConfigurationModule());
  }
}
