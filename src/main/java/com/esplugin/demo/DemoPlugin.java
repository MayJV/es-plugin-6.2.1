package com.esplugin.demo;


import java.util.Arrays;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;

public class DemoPlugin extends Plugin implements  ScriptPlugin {


    private final static Logger LOGGER = LogManager.getLogger(DemoPlugin.class);

    @Override
    public ScriptEngine getScriptEngine(Settings settings, Collection<ScriptContext<?>> contexts) {
        LOGGER.warn("----------This is my fisrt Plugin--------1---");
        LOGGER.info("----1.1---- contexts : {} ", Arrays.toString(contexts.toArray()));
        return new MyScriptEngine();
    }


}
