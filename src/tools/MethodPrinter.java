package tools;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

public class MethodPrinter {
	public static void main(String[] args) {
		String prefix = "set";   //getter or setter
		Class<?> clazz = Integer.class;  //class parameter
		
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().startsWith(prefix)){
				System.out.println(StringUtils.uncapitalize(clazz.getSimpleName()) + "." + method.getName() + "();");
			}
		}
		
	}
}
