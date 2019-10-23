package robotarena;

public class Shot {
  public long deathTime; 

  public int x1;
  public int y1;
  public int x2;
  public int y2;

  public Shot(int x1In,  int y1In, int x2In, int y2In) {
    deathTime = System.currentTimeMillis() + 250;

    x1 = x1In;
    y1 = y1In;
    x2 = x2In;
    y2 = y2In;
  }

  public boolean stillAlive() {
    return (System.currentTimeMillis() <= deathTime);
  }
}