package com.locat.api.unit.utils;

import com.locat.api.global.utils.LocatSpelParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpelParserTest {

  @Test
  void testGetDynamicValue() {
    // Given
    String expression1 = "#a + #b";
    String[] parameterNames1 = {"a", "b"};
    Object[] args1 = {5, 3};

    String expression2 = "#a - #b";
    String[] parameterNames2 = {"a", "b"};
    Object[] args2 = {5, 3};

    String expression3 = "#a * #b";
    String[] parameterNames3 = {"a", "b"};
    Object[] args3 = {5, 3};

    // When
    Object result1 = LocatSpelParser.getDynamicValue(expression1, parameterNames1, args1);
    Object result2 = LocatSpelParser.getDynamicValue(expression2, parameterNames2, args2);
    Object result3 = LocatSpelParser.getDynamicValue(expression3, parameterNames3, args3);

    // Then
    assertAll(
        () -> assertEquals(8, result1),
        () -> assertEquals(2, result2),
        () -> assertEquals(15, result3));
  }

  @Test
  void testEvaluateExpression() {
    // Given
    String expression1 = "#a > #b";
    String[] parameterNames1 = {"a", "b"};
    Object[] args1 = {5, 3};

    String expression2 = "#a < #b";
    String[] parameterNames2 = {"a", "b"};
    Object[] args2 = {2, 3};

    String expression3 = "#a == #b";
    String[] parameterNames3 = {"a", "b"};
    Object[] args3 = {3, 3};

    String expression4 = "#a != #b";
    String[] parameterNames4 = {"a", "b"};
    Object[] args4 = {3, 4};

    // When
    Boolean result1 = LocatSpelParser.evaluateExpression(expression1, parameterNames1, args1);
    Boolean result2 = LocatSpelParser.evaluateExpression(expression2, parameterNames2, args2);
    Boolean result3 = LocatSpelParser.evaluateExpression(expression3, parameterNames3, args3);
    Boolean result4 = LocatSpelParser.evaluateExpression(expression4, parameterNames4, args4);

    // Then
    assertAll(
        () -> assertTrue(result1),
        () -> assertTrue(result2),
        () -> assertTrue(result3),
        () -> assertTrue(result4));
  }
}
