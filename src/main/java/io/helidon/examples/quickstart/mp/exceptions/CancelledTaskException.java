package io.helidon.examples.quickstart.mp.exceptions;

import java.io.Serializable;

public class CancelledTaskException extends Exception implements Serializable {

    public CancelledTaskException(String msg, Exception e) {
        super(msg, e);
    }
}
