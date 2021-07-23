package com.share1024.redisson.condition;

import com.share1024.redisson.util.PropertyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * \* @Author: yesheng
 * \* Date: 2021/7/23 17:03
 * \* Description:
 * \
 */
public class SingleConditional extends SpringBootCondition {
    private static final String SINGLE_PREFIX= "spring.redisson.single";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean isSingle = PropertyUtil.containPropertyPrefix(context.getEnvironment(),SINGLE_PREFIX);
        return isSingle ? ConditionOutcome.match():ConditionOutcome.noMatch("cannot find the " + SINGLE_PREFIX);
    }
}