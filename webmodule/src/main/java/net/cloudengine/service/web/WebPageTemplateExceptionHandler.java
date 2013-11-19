package net.cloudengine.service.web;

import java.io.IOException;
import java.io.Writer;

import net.cloudengine.util.UncheckedThrow;

import org.apache.commons.lang.exception.ExceptionUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class WebPageTemplateExceptionHandler implements TemplateExceptionHandler {

  public void handleTemplateException(TemplateException te, Environment env, Writer out) {
    try {
      out.write("<span style=\"cursor:help; color: red\" " +
                "title=\"" + ExceptionUtils.getMessage(te) + "\">" +
                "[error]" +
                "</span>\n");
    } catch (IOException ignored) {
    	UncheckedThrow.throwUnchecked(ignored);
    }
  }

}