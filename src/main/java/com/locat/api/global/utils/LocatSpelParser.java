package com.locat.api.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocatSpelParser {

    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * SpEL을 이용하여 동적으로 값을 가져온다.
     * @param expression SpEL 표현식
     * @param parameterNames 파라미터 이름
     * @param args 파라미터 값
     * @return 동적으로 가져온 값
     */
    public static Object getDynamicValue(String expression, String[] parameterNames, Object[] args) {
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(expression).getValue(context);
    }

    /**
     * SpEL을 이용해 표현식을 평가합니다.
     * @param expression SpEL 표현식
     * @param parameterNames 파라미터 이름
     * @param args 파라미터 값
     * @return 평가 결과
     */
    public static Boolean evaluateExpression(String expression, String[] parameterNames, Object... args) {
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(expression).getValue(context, Boolean.class);
    }

}
