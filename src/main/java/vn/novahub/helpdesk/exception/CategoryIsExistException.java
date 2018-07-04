package vn.novahub.helpdesk.exception;

public class CategoryIsExistException extends Exception {

    public CategoryIsExistException(String name){
        super("CategoryIsExistException with name = " + name);
    }

    public CategoryIsExistException(long categoryId){
        super("CategoryIsExistException with id = " + categoryId);
    }
}
