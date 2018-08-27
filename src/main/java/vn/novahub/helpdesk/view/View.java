package vn.novahub.helpdesk.view;

public class View {

    public interface Public {}

    public interface AccountWithSkills {}

    public interface SkillWithLevel extends Public {}

    public interface AccountWithSkillsAndCategory extends AccountWithSkills {}
}
