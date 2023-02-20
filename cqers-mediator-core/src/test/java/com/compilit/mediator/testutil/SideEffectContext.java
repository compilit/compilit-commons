package com.compilit.mediator.testutil;

public class SideEffectContext {

  public static boolean isInvoked = false;
  private static int counter = 0;

  private SideEffectContext() {
  }

  public static boolean isInvoked(int times) {
    return isInvoked && counter == times;
  }

  public static synchronized void invoke() {
    isInvoked = true;
    counter++;
  }

  public static synchronized void reset() {
    isInvoked = false;
    counter = 0;
  }
}
