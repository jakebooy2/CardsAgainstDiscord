package me.tfkjake.cad.misc;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Globally available utility classes, mostly for string manipulation.
 * 
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class StringUtils {
  /**
   * Returns an array of strings, one for each line in the string after it has
   * been wrapped to fit lines of <var>maxWidth</var>. Lines end with any of
   * cr, lf, or cr lf. A line ending at the end of the string will not output a
   * further, empty string.
   * <p>
   * This code assumes <var>str</var> is not <code>null</code>.
   * 
   * @param str
   *          the string to split
   * @param fm
   *          needed for string width calculations
   * @param maxWidth
   *          the max line width, in points
   * @return a non-empty list of strings
   */
  public static List wrap(String str, FontMetrics fm, int maxWidth) {
    List lines = splitIntoLines(str);
    if (lines.size() == 0)
      return lines;

    ArrayList strings = new ArrayList();
    for (Iterator iter = lines.iterator(); iter.hasNext();)
      wrapLineInto((String) iter.next(), strings, fm, maxWidth);
    return strings;
  }

  /**
   * Given a line of text and font metrics information, wrap the line and add
   * the new line(s) to <var>list</var>.
   * 
   * @param line
   *          a line of text
   * @param list
   *          an output list of strings
   * @param fm
   *          font metrics
   * @param maxWidth
   *          maximum width of the line(s)
   */
  public static void wrapLineInto(String line, List list, FontMetrics fm, int maxWidth) {
    int len = line.length();
    int width;
    while (len > 0 && (width = fm.stringWidth(line)) > maxWidth) {
      int guess = len * maxWidth / width;
      String before = line.substring(0, guess).trim();
      String beforeTest = line.substring(0, guess).trim();
      for(int i = beforeTest.length() -1; i >= 0; --i){
          char c = beforeTest.charAt(i);
          if(Character.isWhitespace(c))
              break;
          before = before.substring(0, i) + before.substring(i + 1);
          guess--;
      }

      width = fm.stringWidth(before);
      int pos;
      if (width > maxWidth)
        pos = findBreakBefore(line, guess);
      else {
        pos = findBreakAfter(line, guess);
        if (pos != -1) {
          before = line.substring(0, pos).trim();
          if (fm.stringWidth(before) > maxWidth)
            pos = findBreakBefore(line, guess);
        }
      }
      if (pos == -1)
        pos = guess;

      list.add(line.substring(0, pos).trim());
      line = line.substring(pos).trim();
      len = line.length();
    }
    if (len > 0)
      list.add(line);
  }

  /**
   * Returns the index of the first whitespace character or '-' in <var>line</var>
   * that is at or before <var>start</var>. Returns -1 if no such character is
   * found.
   * 
   * @param line
   *          a string
   * @param start
   *          where to star looking
   */
  public static int findBreakBefore(String line, int start) {
    for (int i = start; i >= 0; --i) {
      char c = line.charAt(i);
      if (Character.isWhitespace(c) || c == '-')
        return i;
    }
    return -1;
  }

  /**
   * Returns the index of the first whitespace character or '-' in <var>line</var>
   * that is at or after <var>start</var>. Returns -1 if no such character is
   * found.
   * 
   * @param line
   *          a string
   * @param start
   *          where to star looking
   */
  public static int findBreakAfter(String line, int start) {
    int len = line.length();
    for (int i = start; i < len; ++i) {
      char c = line.charAt(i);
      if (Character.isWhitespace(c) || c == '-')
        return i;
    }
    return -1;
  }
  /**
   * Returns an array of strings, one for each line in the string. Lines end
   * with any of cr, lf, or cr lf. A line ending at the end of the string will
   * not output a further, empty string.
   * <p>
   * This code assumes <var>str</var> is not <code>null</code>.
   * 
   * @param str
   *          the string to split
   * @return a non-empty list of strings
   */
  public static List splitIntoLines(String str) {
    ArrayList strings = new ArrayList();

    int len = str.length();
    if (len == 0) {
      strings.add("");
      return strings;
    }

    int lineStart = 0;

    for (int i = 0; i < len; ++i) {
      char c = str.charAt(i);
      if (c == '\r') {
        int newlineLength = 1;
        if ((i + 1) < len && str.charAt(i + 1) == '\n')
          newlineLength = 2;
        strings.add(str.substring(lineStart, i));
        lineStart = i + newlineLength;
        if (newlineLength == 2) // skip \n next time through loop
          ++i;
      } else if (c == '\n') {
        strings.add(str.substring(lineStart, i));
        lineStart = i + 1;
      }
    }
    if (lineStart < len)
      strings.add(str.substring(lineStart));

    return strings;
  }

}