package factories;

import state.Context;

import java.io.Serializable;

public class InstanceFactory implements Serializable {
  public <T> T getInstance(Class<T> klass) {
    return Context.INSTANCE.getInjector().getInstance(klass);
  }
}
