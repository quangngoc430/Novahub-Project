package vn.novahub.helpdesk.exception;

public class SkillNotFoundException extends Exception{

    public SkillNotFoundException(long skillId){
        super("SkillNotFoundException with id = " + skillId);
    }
}
