package domain;

public class Skill {
    private SkillType type;
    private SkillChange change;
    private SkillTarget target;
    private float amount; //

    public Skill(SkillType type, SkillChange change, SkillTarget target, float amount) {
        this.type = type;
        this.change = change;
        this.target = target;
        this.amount = amount;
    }

    // For barrier and status effect clear skills which do not need a change or an amount
    public Skill(SkillType type, SkillTarget target) {
        this(type, null, target, 100);
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Float.compare(skill.amount, amount) == 0 &&
                type == skill.type &&
                change == skill.change &&
                target == skill.target;
    }
}
