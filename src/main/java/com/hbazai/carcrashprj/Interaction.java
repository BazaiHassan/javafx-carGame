package com.hbazai.carcrashprj;

public class Interaction {
    private EnemyCar enemyCar;
    private Life life;
    private PlayerCar playerCar;

    public Interaction(EnemyCar enemyCar, Life life, PlayerCar playerCar) {
        this.enemyCar = enemyCar;
        this.playerCar = playerCar;
        this.life = life;
    }

    public void decreaseLife() {
        if (playerCar.getLayoutY() == enemyCar.getLayoutY()) {
            System.out.println("Crush");
        }
    }
}
