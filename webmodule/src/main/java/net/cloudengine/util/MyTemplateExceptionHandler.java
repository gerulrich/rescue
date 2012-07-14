package net.cloudengine.util;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.exception.ExceptionUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MyTemplateExceptionHandler implements TemplateExceptionHandler {

  public void handleTemplateException(TemplateException te, Environment env, Writer out) {
//    freemarkerlog.error("template error", te);
    try {
      out.write("<span style=\"cursor:help; color: red\" " +
                "title=\"" + ExceptionUtils.getMessage(te) + "\">" +
                "[error]" +
                "</span>\n");
    } catch (IOException ignored) { }
  }

}