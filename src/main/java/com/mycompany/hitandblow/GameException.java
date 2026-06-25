package com.mycompany.hitandblow;

public class GameException extends RuntimeException
{
    public GameException( String msg , Throwable cause )
    {
        super( msg , cause );
    }
}
