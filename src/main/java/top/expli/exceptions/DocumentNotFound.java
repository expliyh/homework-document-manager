package top.expli.exceptions;

import top.expli.knives;

public class DocumentNotFound extends KnifeException{
    public DocumentNotFound(){
        super(knives.random());
    }
}
