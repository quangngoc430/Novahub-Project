package vn.novahub.helpdesk.exception;

public class SkillIsExistException extends Exception {

    public SkillIsExistException(long skillId){
        super("SkillIsExistException with skillId = " + skillId);
    }

    public SkillIsExistException(String name){
        super("SkillIsExistException with name = " + name);
    }

    public SkillIsExistException(String name, long categoryId){
        super("SkillIsExistException with name = " + name + " , categoryId = " + categoryId);
    }

    public SkillIsExistException(String name, long accountId, long categoryId){
        super("SkillIsExistException with name = " + name + ", accountId = " + accountId + ", categoryId = " + categoryId);
    }

    public SkillIsExistException(String name, long level, long accountId, long categoryId){
        super("SkillIsExistException with name = " + name + ", level = " + level + ", accountId = " + accountId + ", categoryId = " + categoryId);
    }
}
