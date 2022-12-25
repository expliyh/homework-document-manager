package top.expli.exceptions;

public class PermissionDenied extends KnifeException{
    public PermissionDenied(String msg){
        super(msg);
    }
    public PermissionDenied(){
        super();
    }
}
