/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dsl.jbang.core.commands.action;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.camel.dsl.jbang.core.commands.CamelJBangMain;
import org.apache.camel.dsl.jbang.core.commands.Run;
import org.apache.camel.dsl.jbang.core.common.CommandLineHelper;
import org.apache.camel.dsl.jbang.core.common.PathUtils;
import org.apache.camel.dsl.jbang.core.common.VersionHelper;
import org.apache.camel.util.StopWatch;
import org.apache.camel.util.StringHelper;
import org.apache.camel.util.TimeUtils;
import org.apache.camel.util.json.JsonArray;
import org.apache.camel.util.json.JsonObject;
import org.apache.camel.util.json.Jsoner;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

@CommandLine.Command(name = "message",
                     description = "Transform message from one format to another via an existing running Camel integration",
                     sortOptions = false, showDefaultValues = true)
public class TransformMessageAction extends ActionWatchCommand {

    @CommandLine.Option(names = { "--camel-version" },
                        description = "To run using a different Camel version than the default version.")
    String camelVersion;

    @CommandLine.Option(names = { "--body" }, required = true,
                        description = "Message body to send (prefix with file: to refer to loading message body from file)")
    String body;

    @CommandLine.Option(names = { "--header" },
                        description = "Message header (key=value)")
    List<String> headers;

    @CommandLine.Option(names = {
            "--source" },
                        description = "Instead of using external template file then refer to an existing Camel route source with inlined Camel language expression in a route. (use :line-number or :id to refer to the exact location of the EIP to use)")
    private String source;

    @CommandLine.Option(names = {
            "--language" },
                        description = "The language to use for message transformation")
    private String language;

    @CommandLine.Option(names = {
            "--component" },
                        description = "The component to use for message transformation")
    private String component;

    @CommandLine.Option(names = {
            "--dataformat" },
                        description = "The dataformat to use for message transformation")
    private String dataformat;

    @CommandLine.Option(names = {
            "--template" },
                        description = "The template to use for message transformation (prefix with file: to refer to loading message body from file)")
    private String template;

    @CommandLine.Option(names = { "--option" },
                        description = "Option for additional configuration of the used language, component or dataformat (key=value)")
    List<String> options;

    @CommandLine.Option(names = {
            "--output" },
                        description = "File to store output. If none provide then output is printed to console.")
    private String output;

    @CommandLine.Option(names = { "--show-exchange-properties" }, defaultValue = "false",
                        description = "Show exchange properties from the output message")
    boolean showExchangeProperties;

    @CommandLine.Option(names = { "--show-headers" }, defaultValue = "true",
                        description = "Show message headers from the output message")
    boolean showHeaders = true;

    @CommandLine.Option(names = { "--show-body" }, defaultValue = "true",
                        description = "Show message body from the output message")
    boolean showBody = true;

    @CommandLine.Option(names = { "--show-exception" }, defaultValue = "true",
                        description = "Show exception and stacktrace for failed transformation")
    boolean showException = true;

    @CommandLine.Option(names = { "--timeout" }, defaultValue = "20000",
                        description = "Timeout in millis waiting for message to be transformed")
    long timeout = 20000;

    @CommandLine.Option(names = { "--logging-color" }, defaultValue = "true", description = "Use colored logging")
    boolean loggingColor = true;

    @CommandLine.Option(names = { "--pretty" },
                        description = "Pretty print message body when using JSon or XML format")
    boolean pretty;

    @CommandLine.Option(names = { "--repos" },
                        description = "Additional maven repositories (Use commas to separate multiple repositories)")
    String repositories;

    private volatile long pid;

    private MessageTableHelper tableHelper;

    public TransformMessageAction(CamelJBangMain main) {
        super(main);
    }

    @Override
    public Integer doCall() throws Exception {
        if (dataformat == null) {
            // either source or language/template is required
            if (source == null && template == null && language == null && component == null) {
                printer().printErr("Either source or template and one of language/component must be configured");
                return -1;
            }
            if (source == null && (template == null || language == null && component == null)) {
                printer().printErr("Both template and one of language/component must be configured");
                return -1;
            }
        }

        // does files exists
        if (source != null && source.startsWith("file:")) {
            String s = source.substring(5);
            s = StringHelper.beforeLast(s, ":", s); // remove line number
            Path f = Path.of(s);
            if (!Files.exists(f)) {
                printer().printErr("Source file does not exist: " + f);
                return -1;
            }
        }
        if (template != null && template.startsWith("file:")) {
            Path f = Path.of(template.substring(5));
            if (!Files.exists(f)) {
                printer().printErr("Template file does not exist: " + f);
                return -1;
            }
        }

        Integer exit;
        try {
            // start a new empty camel in the background
            Run run = new Run(getMain());
            // requires camel 4.3 onwards
            if (camelVersion != null && VersionHelper.isLE(camelVersion, "4.2.0")) {
                printer().printErr("This requires Camel version 4.3 or newer");
                return -1;
            }
            exit = run.runTransformMessage(camelVersion, repositories);
            this.pid = run.spawnPid;
            if (exit == 0) {
                exit = super.doCall();
            }
        } finally {
            if (pid > 0) {
                // cleanup output file
                Path outputFile = getOutputFile(Long.toString(pid));
                PathUtils.deleteFile(outputFile);
                // stop running camel as we are done
                Path parent = CommandLineHelper.getCamelDir();
                Path pidFile = parent.resolve(Long.toString(pid));
                if (Files.exists(pidFile)) {
                    PathUtils.deleteFile(pidFile);
                }
            }
        }

        return exit;
    }

    @Override
    protected Integer doWatchCall() throws Exception {
        // ensure output file is deleted before executing action
        Path outputFile = getOutputFile(Long.toString(pid));
        PathUtils.deleteFile(outputFile);

        JsonObject root = new JsonObject();
        root.put("action", "transform");
        if (source != null) {
            root.put("source", source);
        }
        if (language != null) {
            root.put("language", language);
        }
        if (component != null) {
            root.put("component", component);
        }
        if (dataformat != null) {
            root.put("dataformat", dataformat);
        }
        if (template != null) {
            root.put("template", Jsoner.escape(template));
        }
        root.put("body", Jsoner.escape(body));
        if (headers != null) {
            JsonArray arr = new JsonArray();
            for (String h : headers) {
                JsonObject jo = new JsonObject();
                if (!h.contains("=")) {
                    printer().println("Header must be in key=value format, was: " + h);
                    return 0;
                }
                jo.put("key", StringHelper.before(h, "="));
                jo.put("value", StringHelper.after(h, "="));
                arr.add(jo);
            }
            root.put("headers", arr);
        }
        if (options != null) {
            JsonArray arr = new JsonArray();
            for (String h : options) {
                JsonObject jo = new JsonObject();
                if (!h.contains("=")) {
                    printer().println("Option must be in key=value format, was: " + h);
                    return 0;
                }
                jo.put("key", StringHelper.before(h, "="));
                jo.put("value", StringHelper.after(h, "="));
                arr.add(jo);
            }
            root.put("options", arr);
        }
        Path f = getActionFile(Long.toString(pid));
        try {
            PathUtils.writeTextSafely(root.toJson(), f);
        } catch (Exception e) {
            // ignore
        }

        JsonObject jo = waitForOutputFile(outputFile);
        if (jo != null) {
            printStatusLine(jo);
            String exchangeId = jo.getString("exchangeId");
            JsonObject message = jo.getMap("message");
            JsonObject cause = jo.getMap("exception");
            if (message != null || cause != null) {
                if (output != null) {
                    Path target = Path.of(output);
                    String json = jo.toJson();
                    if (pretty) {
                        json = Jsoner.prettyPrint(json, 2);
                    }
                    Files.writeString(target, json);
                }
                if (!showExchangeProperties && message != null) {
                    message.remove("exchangeProperties");
                }
                if (!showHeaders && message != null) {
                    message.remove("headers");
                }
                if (!showBody && message != null) {
                    message.remove("body");
                }
                if (!showException && cause != null) {
                    cause = null;
                }
                if (output == null) {
                    if (watch) {
                        clearScreen();
                    }
                    tableHelper = new MessageTableHelper();
                    tableHelper.setPretty(pretty);
                    tableHelper.setLoggingColor(loggingColor);
                    tableHelper.setShowExchangeProperties(showExchangeProperties);
                    String table = tableHelper.getDataAsTable(exchangeId, "InOut", null, null, message, cause);
                    printer().println(table);
                }
            }
        }

        // delete output file after use
        PathUtils.deleteFile(outputFile);

        return 0;
    }

    private void printStatusLine(JsonObject jo) {
        // timstamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String ts = sdf.format(new Date(jo.getLong("timestamp")));
        if (loggingColor) {
            AnsiConsole.out().print(Ansi.ansi().fgBrightDefault().a(Ansi.Attribute.INTENSITY_FAINT).a(ts).reset());
        } else {
            printer().print(ts);
        }
        // pid
        printer().print("  ");
        String p = String.format("%5.5s", this.pid);
        if (loggingColor) {
            AnsiConsole.out().print(Ansi.ansi().fgMagenta().a(p).reset());
            AnsiConsole.out().print(Ansi.ansi().fgBrightDefault().a(Ansi.Attribute.INTENSITY_FAINT).a(" --- ").reset());
        } else {
            printer().print(p);
            printer().print(" --- ");
        }
        // status
        printer().print(getStatus(jo));
        // elapsed
        String e = TimeUtils.printDuration(jo.getLong("elapsed"), true);
        if (loggingColor) {
            AnsiConsole.out().print(Ansi.ansi().fgBrightDefault().a(" (" + e + ")").reset());
        } else {
            printer().print("(" + e + ")");
        }
        printer().println();
    }

    private String getStatus(JsonObject r) {
        boolean failed = "failed".equals(r.getString("status")) || "error".equals(r.getString("status"));
        String status;
        if (failed) {
            status = "Failed (exception)";
        } else if (output != null) {
            status = "Output saved to file (success)";
        } else {
            status = "Message transformed (success)";
        }
        if (loggingColor) {
            return Ansi.ansi().fg(failed ? Ansi.Color.RED : Ansi.Color.GREEN).a(status).reset().toString();
        } else {
            return status;
        }
    }

    protected JsonObject waitForOutputFile(Path outputFile) {
        StopWatch watch = new StopWatch();
        while (watch.taken() < timeout) {
            try {
                // give time for response to be ready
                Thread.sleep(20);

                if (Files.exists(outputFile)) {
                    String text = Files.readString(outputFile);
                    return (JsonObject) Jsoner.deserialize(text);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}
