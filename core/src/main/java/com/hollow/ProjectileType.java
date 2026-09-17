package com.hollow;

public enum ProjectileType {
    SoulBall(1800,2,false),
    ShadowBall(2000,4,true),
    Scream(0,2,true),
    ShadowScream(0,4,true),
    Wave(400,2,true)
    ;
    float speed;
    int damage;
    boolean peirce;
    ProjectileType(float speed,int damage,boolean peirce) {
        this.speed = speed;
        this.damage = damage;
        this.peirce = peirce;
    }



}
