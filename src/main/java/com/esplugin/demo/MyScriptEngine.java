package com.esplugin.demo;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.SearchScript;

public class MyScriptEngine implements ScriptEngine {
    private final static Logger logger = LogManager.getLogger(MyScriptEngine.class);

    @Override
    public String getType() {
        return "demoPlugin"; // script name
    }

    @Override
    public <T> T compile(String scriptName, String scriptSource, ScriptContext<T> context, Map<String, String> params) {
        logger.info("-----2---- use params the scriptName={} ,scriptSource={}, context={},params={}", scriptName, scriptSource, context.name, params.entrySet());

        if (!context.equals(SearchScript.CONTEXT)) {
            throw new IllegalArgumentException(getType() + " scripts cannot be used for context [" + context.name + "]");
        }


        // query script use "source" value with identifier
        if ("demoPlugin".equals(scriptSource)) {

            SearchScript.Factory factory = (p, lookup) -> new SearchScript.LeafFactory() {
                final String first;
                final String second;
                {
                    if (p.containsKey("first") == false) {
                        first = null;
                    }else {
                        first = p.get("first").toString();
                    }
                    if (p.containsKey("second") == false) {
                        second = null;
                    }else {
                        second = p.get("second").toString();
                    }

                    logger.info("this is first value : {} ,second : {} ", first, second);
                }

                @Override
                public SearchScript newInstance(LeafReaderContext context) throws IOException {
                    return new SearchScript(p, lookup, context) {
                        @Override
                        public double runAsDouble() {
                            // get es data
                            final String test = (String) lookup.source().get("test");
                            logger.info("get source test data : {} ", test);
                            if (first != null && first.equals(test)) {
                                return 1.0D;
                            }
                            return Double.MAX_VALUE;
                        }
                    };
                }

                @Override
                public boolean needs_score() {
                    return false;
                }
            };

            return context.factoryClazz.cast(factory);
        }
        throw new IllegalArgumentException("Unknown script name " + scriptSource);
    }

}
