package com.hollow;

public enum CrawlerType {
    Crawlid(100,3),
    Crystal_Crawler(60,5),
    MossCreep(120,2),
    Tiktik(80,4)
    ;
    float speed;
    int hp;

    CrawlerType(float speed,int hp) {
        this.speed = speed;
        this.hp = hp;

    }
}
