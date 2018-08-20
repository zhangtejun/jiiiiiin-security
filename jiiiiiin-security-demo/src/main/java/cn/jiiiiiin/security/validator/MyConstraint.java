package cn.jiiiiiin.security.validator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = MyConstraint.MyConstraintValidator.class)
public @interface MyConstraint {

    final static Logger L = LoggerFactory.getLogger(MyConstraint.class);

    // 自定义校验注解必须要定义下面的3个属性
    /**
     * 校验不通过的时候，默认错误提示信息
     * @return
     */
    String message() default "{org.hibernate.validator.constraints.NotBlank.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * 继承javax的校验注解，以达到执行校验方法指定的效果
     * <p>
     * 泛型：
     * 1.需要校验那个注解
     * 2.需要校验的值的类型
     */
    public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

        // 这里可以注入任何ioc容器中的bean
        // spring 会自动识别ConstraintValidator接口的实现，自动添加到容器中进行管理

        @Override
        public void initialize(MyConstraint myConstraint) {
            // 校验器初始化钩子
            L.info("MyConstraint校验器初始化 {}", myConstraint);
        }

        /**
         * @param object                     待校验的值
         * @param constraintValidatorContext
         * @return 返回true标识校验成功
         */
        @Override
        public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
            L.info("开始校验 {} {}", object, constraintValidatorContext);
            if(!ObjectUtils.allNotNull(object)){
                return false;
            }
            return true;
        }
    }
}
