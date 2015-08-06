package com.smvc.framework.test;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;
import org.junit.Assert;
import org.junit.Test;

public class AspectJWavenTest {
    private static final Set<PointcutPrimitive> DEFAULT_SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }
    
    @Test
    public void testWaven() throws NoSuchMethodException, SecurityException
    {
        String expression = "execution(* com.smvc.test.*.*(..))";
        PointcutParser pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(DEFAULT_SUPPORTED_PRIMITIVES);

        PointcutExpression pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        
        boolean outputMatched = pointcutExpression.couldMatchJoinPointsInType(HelloWorldServiceImpl.class);
        
        Assert.assertTrue(outputMatched);
        
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodCall(HelloWorldServiceImpl.class.getDeclaredMethod("helloWorld"),HelloWorldService.class);
        
        Assert.assertTrue(shadowMatch.alwaysMatches());
    }
}
