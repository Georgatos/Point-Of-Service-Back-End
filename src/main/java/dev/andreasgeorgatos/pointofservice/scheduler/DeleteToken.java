package dev.andreasgeorgatos.pointofservice.scheduler;

import ch.qos.logback.core.subst.Token;
import com.nimbusds.jose.crypto.impl.PRFParams;

public class DeleteToken implements Runnable{

    private String email;
    private Token token;

    @Override
    public void run() {

    }
}
