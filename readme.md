#### @Valid 参数校验
- 实体或者值对象的field加@xxx
- 方法入口参数加@Valid注解
- 自定义的注解需实现ConstraintValidator<IsPhoneNumber, String>接口
 
---

#### 异常统一拦截

- 根据类型捕获异常，最后返回前端
```java
@ControllerAdvice  
@ResponseBody  
public class GlobalExceptionHandler {  
    @ExceptionHandler(value = Exception.class)
```
---

#### 拦截器Interceptor
- 实现
```java
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
```
- 注册拦截器
```java
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
```
---

#### 方法参数注入
- 实现
```java
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
}

```
- 注册,属于参数解析
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
```
---