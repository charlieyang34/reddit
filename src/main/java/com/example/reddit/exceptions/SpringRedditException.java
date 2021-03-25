package com.example.reddit.exceptions;


import org.springframework.mail.MailException;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String message, Exception exception){
        super(message, exception);
    }

    public SpringRedditException(String Message) {
        super(Message);
    }

}
