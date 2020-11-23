package domain;

import java.util.Objects;

public class Skill {
    private SkillType type;
    private SkillChange change;
    private SkillTarget target;
    private double amount; //

    // Jackson requires a public constructor with no parameters
    public Skill() {}

    public Skill(SkillType type, SkillChange change, SkillTarget target, double amount) {
        this.type = type;
        this.change = change;
        this.target = target;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
}
