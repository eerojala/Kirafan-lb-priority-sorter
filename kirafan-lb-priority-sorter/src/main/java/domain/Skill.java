package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Skill {
    private SkillType type;
    private SkillChange change;
    private SkillTarget target;
    private double power; //

    public static List<Skill> cloneSkills(List<Skill> skills) {
        /*
         * Creates a new list of skills from the given list.
         * Skills in the new list are not the same objects but have the same values (so the original list is not affected
         * when making changes in the edit window)
         * If the given list has no values, a completely new list is created anyway
         *
         * This is done that so if the user edit's a characters skill(s) but cancels the editing process by not pressing
         * the submit button and closing the window manually, the character's skill list remains unaffected (the edits
         * are done to the cloned list and when pressing the submit button the cloned list is assigned as the characters
         * new skill list)
         */
        List<Skill> clones = new ArrayList<>();
        skills.stream().forEach(s -> clones.add(s.clone()));
        return clones;
    }

    // Jackson requires a public constructor with no parameters
    public Skill() {}
    /*
     * Skills that have their skilltype as buffs/debuffs should have all parameters as non-null
     * Skills that have their skilltype as other effects should have all parameters as non-null except change
     *
     * See enum SkillType to see which skilltypes are buffs/debuffs and which ones are other effects
     *
     */
    public Skill(SkillType type, SkillChange change, SkillTarget target, double power) {
        this.type = type;
        this.change = change;
        this.target = target;
        this.power = power;
    }

    public SkillType getType() {
        return type;
    }

    public void setType(SkillType type) {
        this.type = type;
    }

    public SkillChange getChange() {
        return change;
    }

    public void setChange(SkillChange change) {
        this.change = change;
    }

    public SkillTarget getTarget() {
        return target;
    }

    public void setTarget(SkillTarget target) {
        this.target = target;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public Skill clone() {
        return new Skill(type, change, target, power);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return getType() == skill.getType() &&
                getChange() == skill.getChange() &&
                getTarget() == skill.getTarget();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getChange(), getTarget());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(type.toString());
        if (change != null) {
            sb.append(" ").append(change);
        }

        if (target != null) {
            sb.append(" ").append(target);
        }

        if (!SkillType.isMiscEffect(type)) {
            sb.append(" ").append(power);
        }

        return sb.toString();
    }
}
