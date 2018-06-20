package vn.novahub.helpdesk.exception;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(long categoryId){
        super("CategoryNotFoundException with id = " + categoryId);
    }
}
