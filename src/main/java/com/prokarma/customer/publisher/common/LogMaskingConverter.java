package com.prokarma.customer.publisher.common;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name = "LogMaskingConverter", category = "Converter")
@ConverterKeys({"spi", "trscId"})
public class LogMaskingConverter extends LogEventPatternConverter {

  private List<String> regExLastFour =
      Arrays.asList("\"\\bcustomerNumber\\b\"[ ]*:[ ]*\"([^\\\"]+)\"");

  private List<String> regFirstFour = Arrays.asList(
      "((0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](19|20)\\d\\d)|([A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,})");

  protected LogMaskingConverter(String name, String style) {
    super(name, style);
  }

  public static LogMaskingConverter newInstance(String[] options) {
    return new LogMaskingConverter("spi", Thread.currentThread().getName());
  }

  @Override
  public void format(LogEvent event, StringBuilder outputMessage) {
    String message = event.getMessage().getFormattedMessage();
    String maskedMessage = message;
    try {
      maskedMessage = maskEnd(maskStart(message));
    } catch (Exception e) {
      maskedMessage = message;
    }
    outputMessage.append(maskedMessage);
  }

  private String maskEnd(String message) {
    for (String expression : regExLastFour) {
      message = mask(message, expression, true);
    }
    return message;
  }

  private String maskStart(String message) {
    for (String expression : regFirstFour) {
      message = mask(message, expression, false);
    }
    return message;
  }

  private String mask(String message, String expression, boolean atEnd) {

    Matcher matcher = Pattern.compile(expression).matcher(message);
    StringBuffer buffer = new StringBuffer();

    while (matcher.find()) {
      StringBuilder matchedString =
          new StringBuilder(message.substring(matcher.start(), matcher.end()));
      if (atEnd) {
        matchedString.replace(matchedString.length() - 5, matchedString.length() - 1, "****");
      } else {
        matchedString.replace(0, 4, "****");
      }
      matcher.appendReplacement(buffer, matchedString.toString());
    }

    matcher.appendTail(buffer);

    return buffer.length() != 0 ? buffer.toString() : message;
  }

}
