public class Upgrade {
    public enum UpgradeType {
        INCREASED_FIRE_RATE("Rapid Fire", "Shoot 25% faster"),
        INCREASED_DASH_COOLDOWN("Swift Dashes", "Reduce dash cooldown"),
        EXTRA_HEALTH("Extra Life", "Gain 1 maximum health"),
        PIERCING_SHOTS("Piercing Shots", "Bullets pierce enemies"),
        TRIPLE_SHOT("Triple Shot", "Fire 3 bullets at once"),
        SLOW_ENEMIES("Slow Field", "Enemies move slower");
        
        private String name;
        private String description;
        
        UpgradeType(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    private UpgradeType type;
    
    public Upgrade(UpgradeType type) {
        this.type = type;
    }
    
    public UpgradeType getType() { return type; }
    
    public void apply(Player player) {
        switch (type) {
            case INCREASED_FIRE_RATE:
                player.increaseFireRate();
                break;
            case INCREASED_DASH_COOLDOWN:
                player.decreaseDashCooldown();
                break;
            case EXTRA_HEALTH:
                player.increaseMaxHealth();
                break;
            case PIERCING_SHOTS:
                player.setPiercingShots(true);
                break;
            case TRIPLE_SHOT:
                player.setTripleShot(true);
                break;
            case SLOW_ENEMIES:
                player.setSlowFieldActive(true);
                break;
        }
    }
    
    public static Upgrade getRandomUpgrade() {
        UpgradeType[] types = UpgradeType.values();
        return new Upgrade(types[(int)(Math.random() * types.length)]);
    }
    
    public static Upgrade[] getRandomUpgrades(int count) {
        Upgrade[] upgrades = new Upgrade[count];
        for (int i = 0; i < count; i++) {
            upgrades[i] = getRandomUpgrade();
        }
        return upgrades;
    }
}
