package vn.novahub.helpdesk.exception;

public class SkillIsExistException extends Exception {

    public SkillIsExistException(String name){
        super("SkillIsExistException with name = " + name);
    }
}
