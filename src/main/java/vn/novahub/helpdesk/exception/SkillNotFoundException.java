package vn.novahub.helpdesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Skill is not exist")
public class SkillNotFoundException extends Exception{

    public SkillNotFoundException(long skillId){
        super("SkillNotFoundException with id = " + skillId);
    }
}
