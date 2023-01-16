package testutil;

final class TestContext {

  private boolean isInteractedWith;

  TestContext() {
  }

  public synchronized void interact() {
    isInteractedWith = true;
  }

  public synchronized void reset() {
    isInteractedWith = false;
  }

  public boolean hasBeenInteractedWith() {
    return isInteractedWith;
  }
}
