package com.hollow;

public enum CockroachType {
    Hornless(100,600,5,1),
    Hornhead(60,1000,7,2);
    ;
    float speed;
    int hp;
    int lungeDamage;
    float lungeSpeed;

    CockroachType(float speed,float lungeSpeed,int hp,int lungeDamage) {
        this.speed = speed;
        this.hp = hp;
        this.lungeDamage = lungeDamage;
        this.lungeSpeed = lungeSpeed;

    }
}
