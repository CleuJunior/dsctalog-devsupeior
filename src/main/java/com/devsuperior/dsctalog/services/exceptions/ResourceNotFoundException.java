package com.devsuperior.dsctalog.services.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg)
    {
        super(msg);
    }
}
