package logic.checks;

import domain.model.GameCharacter;
import domain.model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

public class NoWeaponCheck extends Check {
    private List<Weapon> weapons;

    public NoWeaponCheck(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    // This check prefers (perhaps a bit counter-intuitively) that a character does NOT have an unique weapon
    // (This is because I prefer limit breaking characters who do not have an unique weapon yet first so they can slowly
    // get to max level before receiving their unique weapon (if they ever do))
    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        boolean c1HasUniqueWeapon = characterHasUniqueWeapon(c1);
        boolean c2HasUniqueWeapon = characterHasUniqueWeapon(c2);

        if (!c1HasUniqueWeapon && c2HasUniqueWeapon) {
            // if c1 doesn't have an unique weapon and c2 has
            return -1;
        } else if (c1HasUniqueWeapon && !c2HasUniqueWeapon) {
            // if c2 doesn't have an unique weapon and c1 has
            return 1;
        } else {
            // if neither c1 or c2 has an unique weapon or both of them have unique weapons
            return 0;
        }
    }

    private boolean characterHasUniqueWeapon(GameCharacter chara) {
        List<Weapon> charasUniqueWeapons = weapons.stream()
                .filter(w -> chara.equals(w.getExclusiveCharacter()))
                .collect(Collectors.toList());

        return !charasUniqueWeapons.isEmpty();
    }
}
