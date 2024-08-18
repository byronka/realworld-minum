package com.renomad.realworld;

import com.renomad.minum.web.FullSystem;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var fs = FullSystem.initialize();
        new Routes(fs.getWebFramework()).register();
        fs.block();
    }

}