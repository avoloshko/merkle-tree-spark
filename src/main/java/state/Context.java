package state;

import com.google.inject.Injector;
import factories.InjectorFactory;

public class Context {
  public static final Context INSTANCE = new Context();

  private Injector injector;

  private Context() { this.injector = InjectorFactory.call(); }

  public Injector getInjector() { return injector; }
}
