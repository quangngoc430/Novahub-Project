package vn.novahub.helpdesk.view;

public class View {

    public interface Public {}

    public interface AccountWithSkills {}

    public interface SkillWithLevel extends Public {}

    public interface DayOffAccountRespond {}

    public interface DayOffRespond extends DayOffAccountRespond {}

    public interface AccountWithSkillsAndCategory extends AccountWithSkills {}
}
